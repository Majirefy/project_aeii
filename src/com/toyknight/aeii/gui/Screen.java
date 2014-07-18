
package com.toyknight.aeii.gui;

import java.awt.Dimension;
import javax.swing.JPanel;

/**
 *
 * @author toyknight
 */
public abstract class Screen extends JPanel {
	
	private final AEIIApplet context;

	public Screen(Dimension size, AEIIApplet context) {
		this.setPreferredSize(size);
		this.context = context;
	}
	
	protected final AEIIApplet getContext() {
		return context;
	}
	
	abstract public void update();

}
