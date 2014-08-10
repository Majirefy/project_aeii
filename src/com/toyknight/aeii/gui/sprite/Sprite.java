
package com.toyknight.aeii.gui.sprite;

import java.awt.Graphics;

/**
 *
 * @author toyknight
 */
public class Sprite {
	
	private final int width;
	private final int height;
	
	public Sprite(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void update() {
	}
	
	public void paint(Graphics g, int x, int y) {
		
	}
	
}
