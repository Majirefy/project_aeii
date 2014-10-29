package com.toyknight.aeii.core;

import java.io.File;
import java.io.FileFilter;

/**
 *
 * @author st000121
 */
public class SuffixFileFilter implements FileFilter {

	private final String suffix;

	public SuffixFileFilter(String suffix) {
		this.suffix = suffix;
	}

	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return false;
		} else {
			return f.isFile() && f.getName().endsWith("." + suffix);
		}
	}

}
