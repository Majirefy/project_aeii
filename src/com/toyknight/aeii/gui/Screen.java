
package com.toyknight.aeii.gui;

import java.awt.Dimension;
import java.awt.Graphics;

/**
 *
 * @author toyknight
 */
public class Screen extends AEIIGui {
	
	private final AEIIMainFrame context;

	public Screen(Dimension size, AEIIMainFrame context) {
		super(size);
		this.context = context;
	}
	
	public final AEIIMainFrame getContext() {
		return context;
	}

	@Override
	public void paint(Graphics g) {
	}
	
	@Override
	public void update() {
		
	}
	
	public final void addGui(AEIIGui gui) {
		
	}

}
