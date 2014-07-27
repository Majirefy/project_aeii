
package com.toyknight.aeii.gui.animation;

import java.awt.Graphics;

/**
 *
 * @author toyknight
 */
public class Sprite {
	
	private final int width;
	private final int height;
	
	private int x;
	private int y;
	
	public Sprite(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getX() {
		return x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void update() {
	}
	
	public void paint(Graphics g) {
		
	}
	
}
