
package com.toyknight.aeii.gui;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import javax.swing.JDesktopPane;

/**
 *
 * @author toyknight
 */
public class Screen extends JDesktopPane {
	
	private final AEIIApplet context;

	public Screen(Dimension size, AEIIApplet context) {
		this.setPreferredSize(size);
		this.context = context;
		this.setOpaque(false);
	}
	
	public void initComponents(int ts) {
		
	}
	
	protected final AEIIApplet getContext() {
		return context;
	}
	
	public void onKeyPress(KeyEvent e) {
		
	}
	
	public void onKeyRelease(KeyEvent e) {
		
	}
	
	public void update() {
		
	}

}
