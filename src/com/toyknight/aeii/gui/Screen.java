package com.toyknight.aeii.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import javax.swing.JDesktopPane;

/**
 *
 * @author toyknight
 */
public class Screen extends JDesktopPane {

	protected final int ts;
	private final AEIIApplet context;

	public Screen(Dimension size, AEIIApplet context) {
		this.setPreferredSize(size);
		this.context = context;
		this.ts = context.getTileSize();
		this.setOpaque(false);
	}

	public void initComponents() {

	}

	protected final AEIIApplet getContext() {
		return context;
	}

	public void onKeyPress(KeyEvent e) {

	}

	public void onKeyRelease(KeyEvent e) {

	}
	
	protected void paintContent(Graphics g) {
		
	}

	@Override
	public void paint(Graphics g) {
		synchronized (getContext().getUpdateLock()) {
			paintContent(g);
			super.paint(g);
		}
	}

	public void update() {

	}

}
