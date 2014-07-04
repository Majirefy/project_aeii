
package com.toyknight.aeii.gui;

import java.awt.Dimension;
import java.awt.Graphics;

/**
 *
 * @author toyknight
 */
public abstract class AEIIComponent {
	
	private final Dimension SIZE;
	private int x = 0;
	private int y = 0;
	
	public AEIIComponent(Dimension size) {
		this.SIZE = size;
	}
	
	public final Dimension getSize() {
		return SIZE;
	}
	
	public final int getWidth() {
		return (int)SIZE.getWidth();
	}
	
	public final int getHeight() {
		return (int)SIZE.getHeight();
	}
	
	public final void setX(int x) {
		this.x = x;
	}
	
	public final int getX() {
		return x;
	}
	
	public final void setY(int y) {
		this.y = y;
	}
	
	public final int getY() {
		return y;
	}
	
	abstract public void paint(Graphics g);
	
	abstract public void update();
	
}
