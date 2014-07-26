
package com.toyknight.aeii.gui;

import java.awt.Dimension;
import javax.swing.JDesktopPane;

/**
 *
 * @author toyknight
 */
public abstract class Screen extends JDesktopPane {
	
	private final AEIIApplet context;

	public Screen(Dimension size, AEIIApplet context) {
		this.setPreferredSize(size);
		this.context = context;
		this.setOpaque(false);
	}
	
	protected final AEIIApplet getContext() {
		return context;
	}
	
	abstract public void update();

}
