package com.philemonworks.umlx.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Example: http://svn.sourceforge.net/viewvc/maven-taglib/m2-taglib-plugin/tags/maven-taglib-plugin-2.3/src/main/java/net/sf/maventaglib/
 * 
 * @goal generate
 */
public class GenerateMojo extends AbstractMojo 
{
    public void execute() throws MojoExecutionException 
    {
        getLog().info("[umlx-maven-plugin] Run the UMLX Generator and create Class Diagrams in HTML");
        com.philemonworks.umlx.GenerationSpec.main(new String[0]);
        getLog().info("[umlx-maven-plugin] ... Generator done!");
    }
}