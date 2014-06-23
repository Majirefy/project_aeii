
package com.toyknight.aeii.gui.util;

import java.awt.Frame;
import javax.swing.JOptionPane;

/**
 *
 * @author toyknight
 */
public class DialogUtil {
	
	private DialogUtil() {
	}
	
	public static void showInfo(Frame owner, String msg) {
		JOptionPane.showMessageDialog(
					null,
					msg, 
					"Prompt",
					JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void showError(Frame owner, String msg) {
		JOptionPane.showMessageDialog(
					null,
					msg, 
					"Error",
					JOptionPane.ERROR_MESSAGE);
	}
	
}
