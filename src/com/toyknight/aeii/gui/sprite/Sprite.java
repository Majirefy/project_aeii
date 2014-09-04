
package com.toyknight.aeii.gui.sprite;

import java.awt.Graphics;

/**
 *
 * @author toyknight
 */
public class Sprite {
	
	private final int width;
	private final int height;
	private int interval;
	private int current_delay;
	
	public Sprite(int width, int height) {
		this.width = width;
		this.height = height;
		setInterval(0);
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public final void setInterval(int interval) {
		this.interval = interval;
		current_delay = 0;
	}
	
	protected void doUpdate() {
		
	}
	
	public final void update() {
		if (current_delay < interval) {
			current_delay++;
		} else {
			current_delay = 0;
			doUpdate();
		}
	}
	
	public void paint(Graphics g, int x, int y) {
		
	}
	
}
