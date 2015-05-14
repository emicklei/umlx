package com.philemonworks.umlx;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import com.philemonworks.umlx.actions.AbstractRemoveAction;
import com.philemonworks.umlx.actions.AddAssociationsAction;
import com.philemonworks.umlx.actions.GenerationAction;
import com.philemonworks.umlx.actions.RemoveAttributesAction;
import com.philemonworks.umlx.actions.RemoveClassesAction;
import com.philemonworks.umlx.actions.RemoveConstructorsAction;
import com.philemonworks.umlx.actions.RemoveGettersAndSettersAction;
import com.philemonworks.umlx.actions.RemoveGettersAndSettersOfPrefixedAttributesAction;
import com.philemonworks.umlx.actions.RemoveInnerClassesAction;
import com.philemonworks.umlx.actions.RemoveInterfacesAction;
import com.philemonworks.umlx.actions.RemoveOperationsAction;
import com.philemonworks.umlx.actions.RemovePackagesAction;
import com.philemonworks.umlx.actions.SortElementsAction;

/**
 * GenerationSpec is a UMLX Generator specification object that is instanciated by parsing an XML file.
 * 
 * @author E.M.Micklei
 */
public class GenerationSpec {
	private static final String RESOURCE = "umlxgenerator.xml";
	private static final String[] SUPPORTEDACTIONS = new String[] { "removeattribute", "removeclass removeinterface", "removegettersandsetters",
			"removegettersandsettersofprefixedattributes", "removeconstructors", "removeinnerclasses", "removeoperation", "removepackage", "sortelements" };
	/**
	 * arg[0] =  home of Java classes
	 * arg[1] =  target for UMLX artifcats
	 * @param args
	 */
	public static void main(String[] args) {
		Generator gen = new GenerationSpec().read();
		if (gen == null)
			return;
		if (args.length > 0) {
			gen.setClassesHome(args[0]);
			gen.setUMLXHome(args[1]);
		} else {
			gen.initializeDefault();
		}
		gen.generateHTML();
	}
	private Document readDocument() throws Exception {
		InputStream is = getClass().getClassLoader().getResourceAsStream(RESOURCE);
		if (is == null) {
			System.out.println("No configuration found on classpath, trying to find it in the project home");
			is = new FileInputStream(System.getProperty("user.dir") + File.separator + RESOURCE);
			if (is == null)
				throw new RuntimeException("No configuration found on:" + RESOURCE);
		}
		DocumentBuilderFactory factoryBuilder = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factoryBuilder.newDocumentBuilder();
		Document doc = builder.parse(is);
		return doc;
	}
	private Generator read() {
		Generator gen = null;
		try {
			Document doc = this.readDocument();
			gen = this.buildGenerator(doc);
		} catch (Exception e) {
			System.out.println("[warn] " + e.getMessage());
			System.out.println("[info] using default generator configuration (no actions)");
			gen = new Generator(new String[2]); // locations will be set later
		}
		return gen;
	}
	private Generator buildGenerator(Document doc) {
		Generator gen = new Generator(new String[2]); // locations will be set later
		Node root = this.getGeneratorNode(doc);
		// process generator attributes
		Node attr = root.getAttributes().getNamedItem("html");
		if (attr != null)
			gen.setHtml("true".equals(attr.getNodeValue()));		
		attr = root.getAttributes().getNamedItem("umlx");
		if (attr != null)
			gen.setUmlx("true".equals(attr.getNodeValue()));		

		// process actions		
		Node actionsHolder = this.getActionsNode(root);
		if (actionsHolder == null) return gen; // no actions
		Node child = actionsHolder.getFirstChild();
		while (child != null) {
			this.handle(child, gen);
			child = child.getNextSibling();
		}
		return gen;
	}
	private Node getGeneratorNode(Document doc) {
		Node child = doc.getFirstChild();
		while (child != null && (!"generator".equals(child.getNodeName())))
			child = child.getNextSibling();
		return child;
	}
	/**
	 * Return the node that has actions as its children or null if no actions were specified.
	 * @param generatorNode Node root of the document
	 * @return Node
	 */
	private Node getActionsNode(Node generatorNode) {
		Node child = generatorNode.getFirstChild();
		while (child != null && (!"actions".equals(child.getNodeName())))
			child = child.getNextSibling();
		return child;
	}
	private void handle(Node action, Generator gen) {
		String tag = action.getNodeName();
		if ("#text".equals(tag)) {
			// ignore
		} else if ("removeattribute".equals(tag)) {
			this.addRemoveAction(new RemoveAttributesAction(), action.getAttributes(), gen);
		} else if ("removeclass".equals(tag)) {
			this.addRemoveAction(new RemoveClassesAction(), action.getAttributes(), gen);	
		} else if ("removeinterface".equals(tag)) {
			this.addRemoveAction(new RemoveInterfacesAction(), action.getAttributes(), gen);
		} else if ("removepackage".equals(tag)) {
			this.addRemoveAction(new RemovePackagesAction(), action.getAttributes(), gen);
		} else if ("removeoperation".equals(tag)) {
			this.addRemoveAction(new RemoveOperationsAction(), action.getAttributes(), gen);
		} else if ("addassociations".equals(tag)) {
			this.addAddAssociationsAction(action, gen);
		} else if ("removeinnerclasses".equals(tag)) {
			gen.addPostAction(new RemoveInnerClassesAction());
		} else if ("removeconstructors".equals(tag)) {
			gen.addPostAction(new RemoveConstructorsAction());			
		} else if ("removegettersandsetters".equals(tag)) {
			gen.addPostAction(new RemoveGettersAndSettersAction());
		} else if ("removegettersandsettersofprefixedattributes".equals(tag)) {
			this.addRemoveGettersAndSettersOfPrefixedAttributesAction(action, gen);
		} else if ("sortelements".equals(tag)) {
			gen.addPostAction(new SortElementsAction());
		} else {
			this.unknownAction(tag);
		}
	}
	private void unknownAction(String tag) {
		System.err.print("Unknown generator action:[" + tag + "],supported actions:");
		for (int a = 0; a < SUPPORTEDACTIONS.length; a++)
			System.err.print(SUPPORTEDACTIONS[a] + " ");
	}
	private void addRemoveAction(AbstractRemoveAction action, NamedNodeMap map, Generator gen) {
		Node attr = map.getNamedItem("name");
		if (attr != null)
			action.add(attr.getNodeValue());
		attr = map.getNamedItem("prefix");
		if (attr != null)
			action.addPrefix(attr.getNodeValue());
		attr = map.getNamedItem("suffix");
		if (attr != null)
			action.addSuffix(attr.getNodeValue());
		attr = map.getNamedItem("pattern");
		if (attr != null)
			action.setPattern(attr.getNodeValue());
		gen.addPostAction((GenerationAction) action);
	}
	private void addAddAssociationsAction(Node action, Generator gen) {
		AddAssociationsAction aaa = new AddAssociationsAction();
		Node endSpec = action.getFirstChild();
		while (endSpec != null) {
			if ("end".equals(endSpec.getNodeName())) {
				this.addAssociationEnd(endSpec.getAttributes(), aaa);
			}
			endSpec = endSpec.getNextSibling();
		}
		gen.addPostAction(aaa);
	}
	private void addAssociationEnd(NamedNodeMap map, AddAssociationsAction action) {
		String containerType, elementType, role;
		Node attr = map.getNamedItem("containertype");
		if (attr == null) {
			this.missingAttributeInNodeForAction("containertype", "association", "addassociations");
			return;
		} else
			containerType = attr.getNodeValue();
		attr = map.getNamedItem("role");
		if (attr == null) {
			this.missingAttributeInNodeForAction("role", "association", "addassociations");
			return;
		} else
			role = attr.getNodeValue();
		attr = map.getNamedItem("elementtype");
		if (attr == null) {
			this.missingAttributeInNodeForAction("elementtype", "association", "addassociations");
			return;
		} else
			elementType = attr.getNodeValue();
		action.addAssociationEnd(containerType, role, elementType);
	}
	private void missingAttributeInNodeForAction(String attributeName, String nodeName, Object action) {
		System.err.println("Missing attribute:" + attributeName + " in node:" + nodeName + " for action:" + action);
	}
	private void addRemoveGettersAndSettersOfPrefixedAttributesAction(Node action, Generator gen) {
		RemoveGettersAndSettersOfPrefixedAttributesAction rem = new RemoveGettersAndSettersOfPrefixedAttributesAction();
		Node attr = action.getAttributes().getNamedItem("prefix");
		if (attr != null) {
			rem.setPrefix(attr.getNodeValue());
			gen.addPostAction(rem);
		}
	}
}
