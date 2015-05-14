package com.philemonworks.umlx.maven;

import java.util.Locale;
import org.apache.maven.doxia.siterenderer.Renderer;
import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReportException;

/**
 * 
 * <pre>
 * Docs: 
 * 		https://svn.apache.org/repos/asf/maven/plugins/trunk/maven-changes-plugin/
 * 		http://docs.codehaus.org/display/MAVENUSER/Write+your+own+report+plugin
 * 		http://www.nabble.com/-M2--Is-there-a-guide-how-to-write-Report-Plugins--tf2458030.html#a6850949
 * </pre>
 * 
 * @goal report
 */
public class ReportMojo extends AbstractMavenReport {	
    /**
     * Directory where reports will go.
     *
     * @parameter expression="${project.reporting.outputDirectory}"
     * @required
     * @readonly
     */
    private String outputDirectory;
    
	public boolean canGenerateReport() {
		// TODO Auto-generated method stub
		return true;
	}

	protected void executeReport(Locale arg0) throws MavenReportException {
		
		getLog().info("[umlx-maven-plugin] Make a Report with references to the Class Diagrams");
		String here = System.getProperty("user.dir");
		Reporter.main(new String[] {
				here + "/target/site/umlx",
				here + "/target/generated-site/xdoc" });
		getLog().info("[umlx-maven-plugin] ... Reporter done!");				
	}

	
	protected String getOutputDirectory() {
		return outputDirectory;
	}

	
	protected MavenProject getProject() {
		// TODO Auto-generated method stub
		return null;
	}

	
	protected Renderer getSiteRenderer() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getDescription(Locale arg0) {
		return "Class diagrams in HTML - one for each package";
	}

	public String getName(Locale arg0) {
		return "UMLX report";
	}

	public String getOutputName() {
		return "umlx-report";
	}

}