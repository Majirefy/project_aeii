
package com.toyknight.aeii.gui;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 *
 * @author toyknight
 */
public class GameCanvas extends Canvas {
	
	private final BufferedImage off_screen_buffer;
	
	public GameCanvas(Dimension base_size, int scale) {
		Dimension real_size = new Dimension(
				(int)base_size.getWidth()*scale, 
				(int)base_size.getHeight()*scale);
		this.setPreferredSize(real_size);
		off_screen_buffer = new BufferedImage(
				base_size.width, 
				base_size.height, 
				BufferedImage.TYPE_INT_ARGB);
	}
	
	public Graphics getOffScreenGraphics() {
		Graphics g = off_screen_buffer.createGraphics();
		((Graphics2D)g).setRenderingHint(
				RenderingHints.KEY_ANTIALIASING, 
				RenderingHints.VALUE_ANTIALIAS_ON);
		return g;
	}
	
	@Override
	public void paint(Graphics g) {
		g.drawImage(off_screen_buffer, 0, 0, getWidth(), getHeight(), this);
	}
	
}
