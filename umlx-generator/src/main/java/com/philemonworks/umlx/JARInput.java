package com.philemonworks.umlx;

import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 *  JARInput is
 * 
 *  @author E.M.Micklei
 */
public class JARInput {
	private Enumeration entries;
	private JarEntry nextEntry;

	public JARInput(String fileName) {
		super();
		JarFile jar = null;
		try {
			jar = new JarFile(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		entries = jar.entries();
		this.findFirst();
	}
	public String next() {
		JarEntry result = nextEntry;
		nextEntry = null;
		this.findFirst();
		return result.getName();
	}
	public boolean hasNext() {
		return nextEntry != null;
	}
	private void findFirst() {
		while (entries.hasMoreElements() && nextEntry == null) {
			JarEntry entry = (JarEntry) entries.nextElement();
			if (!entry.isDirectory() && (entry.getName().endsWith(".class")))
				nextEntry = entry;
		}

	}
}
