/*
 * Created on 4-dec-2003
 *
 * XP Team
 * */
package com.philemonworks.umlx;

/**
 * @author emicklei
 *
 * Purpose:
 *
 */
import java.io.File;
import java.io.FileOutputStream;
import org.apache.log4j.Logger;
import com.philemonworks.umlx.elements.Class;
import com.philemonworks.umlx.elements.Interface;
import com.philemonworks.umlx.elements.Model;
import com.philemonworks.umlx.elements.ModelElement;
import com.philemonworks.umlx.elements.Package;
import com.philemonworks.writer.XMLWriter;

public class XMLExporter implements IExporter {
    public static final String UMLXHEADER = "<!-- UMLX version 1.3 see www.PhilemonWorks.com -->";
    public String filePath = "'";
    private int exportcount = 0;

    public void exportModelElementToIn(ModelElement theModelElement, String localName, String dir) {
        try {
            new File(dir).mkdirs();
			Logger.getLogger(XMLExporter.class).debug("Writing to " + dir + File.separator + localName);
            FileOutputStream fos = new FileOutputStream(dir + File.separator + localName);
            fos.write(XMLWriter.XMLHEADER.getBytes());
            fos.write('\n');
            fos.write(UMLXHEADER.getBytes());
            fos.write('\n');
            fos.write(theModelElement.toString().getBytes());
            fos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
			Logger.getLogger(XMLExporter.class).error("Unable to export a" + theModelElement.getClass().getName() + " to " + dir + File.separator + localName);
        }
    }

    public void export(ModelElement anElement) {
        this.exportModelElementToIn(anElement, Utils.shortNameFrom(anElement.name) + anElement.getFileExtension(), filePath);
    }

    public void export(Model umlxModel) {
        // Modifies filePath so keep a reference to the original value
        String home = filePath;
        for (int p = 0; p < umlxModel.getPackages().size(); p++) {
            Package each = (Package) umlxModel.getPackages().get(p);
            each.fileName = Utils.dotted2Path(each.name) + File.separator + Utils.shortNameFrom(each.name);
            this.setFilePath(home + File.separator + Utils.dotted2Path(each.name));
            this.export(each);
            exportcount++;
            for (int c = 0; c < each.classes.size(); c++) {
                this.export((Class) each.classes.get(c));
                exportcount++;
            }
            for (int i = 0; i < each.interfaces.size(); i++) {
                this.export((Interface) each.interfaces.get(i));
                exportcount++;
            }
        }
        filePath = home;
        // Export the model itself
        this.export((ModelElement)umlxModel);
		Logger.getLogger(this.getClass()).info("XMLExporter finished exporting: " + exportcount + " UMLX files in: " + filePath);
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}