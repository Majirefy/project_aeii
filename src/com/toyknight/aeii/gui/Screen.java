
package com.toyknight.aeii.gui;

import java.awt.Dimension;
import javax.swing.JPanel;

/**
 *
 * @author toyknight
 */
public abstract class Screen extends JPanel {
	
	private final AEIIMainFrame context;

	public Screen(Dimension size, AEIIMainFrame context) {
		this.setPreferredSize(size);
		this.context = context;
	}
	
	protected final AEIIMainFrame getContext() {
		return context;
	}
	
	abstract public void update();

}
