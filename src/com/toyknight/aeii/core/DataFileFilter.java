
package com.toyknight.aeii.core;

import java.io.File;
import java.io.FileFilter;

/**
 *
 * @author st000121
 */
public class DataFileFilter implements FileFilter {
		
		@Override
		public boolean accept(File f) {
			if(f.isFile() && f.getName().endsWith(".dat")) {
				return true;
			} else {
				return false;
			}
		}

	}
