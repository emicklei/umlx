package com.philemonworks.umlx;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import sli.kim.classfile.ClassFileParseException;
import sli.kim.classfile.ClassFileReader;
import sli.kim.classfile.ClassInfo;
import sli.kim.classfile.Debug;
import com.philemonworks.umlx.actions.GenerationAction;
import com.philemonworks.umlx.elements.Class;
import com.philemonworks.umlx.elements.Interface;
import com.philemonworks.umlx.elements.Model;
import com.philemonworks.umlx.elements.Package;
import com.philemonworks.umlx.html.HTMLExporter;

/**
 * The class Generator is a tool to produce UMLX files from a set of classes and packages. It is common to use the
 * standardized UML notation for representing object designs. Many UML editors exists on the (free) market that support
 * that notation. These editors allow you to specify the model elements, build diagrams (e.g. class,package,state...)
 * and manage all of this. And ofcourse all editors have their own proprietary storage format. Some of them support
 * XMI, which is an interchange format. Recently, the UXF format is being proposed as the solution for exchanging
 * object designs between different tools.
 * <p>
 * UMLX is the result of an idea that specifications of model elements could be written in plain structured text.
 * Everything but the diagrams can be specified this way, once the textual structure is agreed upon. Typically this
 * information can be managed (created, updated, generated...) just as any source code for your project. For
 * constructing diagrams, such as a Class Diagram, one could pick a simple tool that reads UMLX and helps you build a
 * diagram. An example of such a tool is SUMO, developed by <a href="http://www.philemonworks.com">PhilemonWorks</a>
 * <p>
 * The following example shows the basic use of this class.
 *
 * <pre>
 *
 *
 *   	Generator gen = new Generator();
 *   	gen.setClassesHome({path to Java classes});
 *   	gen.setUMLXHome({path to generated UMLX files});
 *   	gen.setModelName({your model name});
 *   	gen.generateUMLX();
 *
 *
 * </pre>
 *
 * The generator use a TypeBuilder to extract the meta information of each Java class which are described using UMLX
 * model elements such as Class,Interface,Attribute and Operation. To extend and reduce the number of elements or the
 * details per element, several GenerationAction classes are available.
 *
 * @see com.philemonworks.umlx.GenerationAction For instance, the AddAssociationsAction tries to resolve association
 *      relations between classes.
 *      <p>
 *      [1.3] This release has an implementation of an HTML exporter. Use the method generateHTML from the Generator to create HTML diagrams.
 *      <p>
 * @author ernest.micklei@philemonworks.com
 */
public class Generator {
	/**
	 * Denotes the version of the Generator.
	 */
	public final static String VERSION = "1.3.3";
	/**
	 * Path where the Generator can find all packages and classes.
	 */
	private String classesHome;
	/**
	 * The exporter is able to export a UMLX Model
	 */
	private IExporter exporter;
	/**
	 * Stores the generated Model such that multiple Exporters can be used.
	 */
	private Model generatedModel;
	/**
	 * The name of the Model that contains are packages and classes.
	 */
	private String modelName = "Generated Model";
	/**
	 * Dictionary of package-name, Package instance pairs.
	 */
	private Map packages = new HashMap();
	/**
	 * Stores the list of GenerationAction instances that are executed AFTER reading all Model elements.
	 */
	private List postGenerationActions = new ArrayList();
	/**
	 * Path to which the Generator will write all UMLX files.
	 */
	private String umlxHome;
	/**
	 * Tell the generator to use the standard ClassLoader and Java Reflection to extract the meta information. If set to
	 * false, the generator uses a ClassFileReader
	 */
	private boolean useClassLoader = false;
	/**
	 * Tell the generator to export diagrams in HTML, default is true
	 */
	private boolean html = true;
	/**
	 * Tell the generator to export specifications in XML using the UMLX schema, default is false
	 */
	private boolean umlx = false;
	/**
	 * Default constructor
	 *
	 */
	public Generator(){
		super();
		this.initializeDefault();
	}
	/**
	 * Constructor for the Generator.
	 * arg[0] = directory where to find the compiled classes
	 * arg[1] = directory where the files will be generated
	 * @param args : String[]
	 */
	public Generator(String[] args){
		super();
		if (args.length == 0){
			this.initializeDefault();
		} else {
			this.setClassesHome(args[0]);
			this.setUMLXHome(args[1]);
		}
	}
	public void initializeDefault(){
		String here = System.getProperty("user.dir");
		this.setClassesHome(here + "/target/classes");
		this.setUMLXHome(here + "/target/site/umlx");
		Logger.getLogger(Generator.class).warn("No main arguments set; using default source (/target/classes) and target directory (/target/umlx)");
	}
	/**
	 * Add a new action to the list of post-generation actions.
	 *
	 * @param action
	 */
	public void addPostAction(GenerationAction action) {
		postGenerationActions.add(action);
	}
	/**
	 * Build a UMLX class from a Java class specified by its qualifiedName and the result to the package <b>pkg</b>
	 *
	 * @param qualifiedName
	 * @param pkg
	 * @throws ClassNotFoundException
	 */
	private void buildFromClassNameFor(String qualifiedName, Package pkg, String classFileLocation)
			throws ClassNotFoundException, ClassFileParseException, IOException {
		if (useClassLoader) {
			java.lang.Class javaClass = java.lang.Class.forName(qualifiedName, false, this.getClass().getClassLoader());
			if (!javaClass.isInterface()) {
				ClassBuilder builder = new ClassBuilder();
				Class umlxClass = builder.buildFrom(javaClass);
				pkg.addClass(umlxClass);
			} else {
				InterfaceBuilder builder = new InterfaceBuilder();
				Interface umlxInterface = builder.buildFrom(javaClass);
				pkg.addInterface(umlxInterface);
			}
		} else {
			Debug.setEnabled(false);
			InputStream is = new FileInputStream(classFileLocation);
			ClassInfo classInfo = new ClassInfo();
			new ClassFileReader().read(is, classInfo);
			TypeFromClassfileBuilder builder = new TypeFromClassfileBuilder();
			if (builder.isClass(classInfo)) {
				Class umlxClass = new TypeFromClassfileBuilder().buildClassFrom(classInfo);
				pkg.addClass(umlxClass);
			} else {
				Interface umlxInterface = new TypeFromClassfileBuilder().buildInterfaceFrom(classInfo);
				pkg.addInterface(umlxInterface);
			}
		}
	}
	private void buildFromClassNameStoredIn(String qualifiedName, String classFileLocation) {
		Package pkg = this.getPackage(Utils.packageNameFrom(qualifiedName));
		try {
			this.buildFromClassNameFor(qualifiedName, pkg, classFileLocation);
		} catch (Throwable ex) {
			Logger.getLogger(this.getClass()).error("Unable to find or load:" + qualifiedName, ex);
			// Create an empty class (we cannot tell whether it is an interface; start with an I? ...)
			Class umlxClass = new Class();
			umlxClass.name = Utils.shortNameFrom(qualifiedName);
			pkg.addClass(umlxClass);
		}
	}
	/**
	 * This will start all the work. The following steps are taken:
	 * <ol>
	 * <li>Collect all class files
	 * <li>Build UMLX Model elements (Package,Class,Attribute,Interface...)
	 * <li>Add all Package elements to the model.
	 * <li>Run all post generation actions.
	 * </ol>
	 *
	 * @return the Model
	 */
	private Model buildModel() {
		Logger.getLogger(this.getClass()).info("UMLX Generator " + VERSION + " started...");
		Logger.getLogger(this.getClass()).info("Collecting class files starting at " + classesHome);
		List allTypeNames = this.readTypeNamesFrom(classesHome);
		Logger.getLogger(this.getClass()).info("Found " + allTypeNames.size() + " class files");
		for (Iterator it = allTypeNames.iterator(); it.hasNext();) {
			String fullFilename = (String) it.next();
			String typeName = this.qualifiedTypeNameFrom(classesHome, fullFilename);
			this.buildFromClassNameStoredIn(typeName, fullFilename);
		}
		Model umlxModel = new Model();
		umlxModel.name = modelName;
		for (Iterator it = packages.keySet().iterator(); it.hasNext();) {
			Package each = (Package) packages.get((String) it.next());
			umlxModel.getPackages().add(each);
		}
		for (Iterator it = postGenerationActions.iterator(); it.hasNext();) {
			GenerationAction action = (GenerationAction) it.next();
			action.execute(umlxModel);
			Logger.getLogger(action.getClass()).info(action.report());
		}
		return umlxModel;
	}
	/**
	 * Answer the absolute path of Java type with respect to some home specfiication.
	 *
	 * @param home
	 * @param qualifiedTypeName
	 * @return String the path
	 */
	private String fullPathFrom(String home, String qualifiedTypeName) {
		String dotted = Utils.packageNameFrom(qualifiedTypeName);
		return home + File.separator + Utils.dotted2Path(dotted);
	}
	public void generate() {
		if (html) this.generateHTML();
		if (umlx) this.generateUMLX();
	}
	public void generateHTML() {
		HTMLExporter ex = new HTMLExporter();
		ex.setFilePath(this.getUMLXHome());
		ex.setNoNamespaceForTypes(true);
		ex.export(this.getModel());
	}
	/**
	 * Generate the UMLX Model and export it
	 */
	public void generateUMLX() {
		XMLExporter exporter = new XMLExporter();
		exporter.setFilePath(umlxHome);
		exporter.export(this.getModel());
	}
	/**
	 * Answer the home directory fromwhich the Java class files are found.
	 */
	public String getClassesHome() {
		return classesHome;
	}
	public IExporter getExporter() {
		return exporter;
	}
	/**
	 * Return the generated Model or build it if it is null.
	 *
	 * @return Model
	 */
	public Model getModel() {
		if (generatedModel == null)
			generatedModel = this.buildModel();
		return generatedModel;
	}
	/**
	 * Answer the name of the UMLX model.
	 *
	 * @return String
	 */
	public String getModelName() {
		return modelName;
	}
	/**
	 * Returns a UMLX package whos name is <b>packageName</b> Create a new if it is missing from the list of packages.
	 *
	 * @param packageName
	 * @return
	 */
	private Package getPackage(String packageName) {
		Package pkg = (Package) packages.get(packageName);
		if (pkg == null) {
			pkg = new Package();
			pkg.name = packageName; // might want to apply rules here
			pkg.namespace = packageName;
			packages.put(packageName, pkg);
		}
		return pkg;
	}
	/**
	 * Answer the home directory to which the UMLX specifications are generated.
	 */
	public String getUMLXHome() {
		return umlxHome;
	}
	/**
	 * Extract the qualified type name from an absolute filename with respect to some home specification.
	 *
	 * @param home
	 * @param fullFilename
	 * @return
	 */
	private String qualifiedTypeNameFrom(String home, String fullFilename) {
		String withoutHome = fullFilename.substring(home.length() + 1, fullFilename.length()); // +1 for the slash
		String dotted = withoutHome.replace('\\', '.');
		dotted = dotted.replace('/', '.');
		String withoutExtension = dotted.substring(0, dotted.lastIndexOf('.'));
		return withoutExtension;
	}
	private List readTypeNamesFrom(String sourceDir) {
		List typeNames = new ArrayList();
		File dir = new File(sourceDir);
		if (!dir.exists())
			throw new RuntimeException("Directory does not exists:" + sourceDir);
		this.readTypeNamesInto(dir, typeNames);
		return typeNames;
	}
	private void readTypeNamesInto(File currentDir, List typeNames) {
		String[] contents = currentDir.list();
		for (int f = 0; f < contents.length; f++) {
			String fullName = currentDir.getAbsolutePath() + File.separator + contents[f];
			File eachFile = new File(fullName);
			if (eachFile.isDirectory())
				this.readTypeNamesInto(eachFile, typeNames);
			else {
				if (fullName.endsWith(".class")) {
					typeNames.add(fullName);
				}
			}
		}
	}
	/**
	 * Set the home directory fromwhich the Java class files are found.
	 *
	 * @param classesHome
	 */
	public void setClassesHome(String classesHome) {
		this.classesHome = classesHome;
	}
	public void setExporter(IExporter exporter) {
		this.exporter = exporter;
	}
	/**
	 * Set the name of the UMLX model
	 *
	 * @param modelName
	 */
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	/**
	 * Set the home directory to which the UMLX specifications are generated.
	 *
	 * @param umlxHome
	 */
	public void setUMLXHome(String umlxHome) {
		this.umlxHome = umlxHome;
	}
	/**
	 * Set whether the generator should use the standard ClassLoader to access the reflection properties of each class.
	 * If no classloader should be used, then the generator uses its own ClassFile reader.
	 *
	 * @param useIt
	 */
	public void setUseClassLoader(boolean useIt) {
		useClassLoader = useIt;
	}
	public void setHtml(boolean html) {
		this.html = html;
	}
	public void setUmlx(boolean umlx) {
		this.umlx = umlx;
	}
}
