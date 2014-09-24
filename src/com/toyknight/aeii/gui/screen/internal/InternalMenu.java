
package com.toyknight.aeii.gui.screen.internal;

import java.awt.Dimension;
import javax.swing.JInternalFrame;

/**
 *
 * @author toyknight
 */
public class InternalMenu extends JInternalFrame {
	
	public InternalMenu() {
		super("Internal Frame");
		this.getContentPane().setPreferredSize(new Dimension(400, 300));
		this.pack();
	}
	
}
