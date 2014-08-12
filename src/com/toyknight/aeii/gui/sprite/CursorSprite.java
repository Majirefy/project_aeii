
package com.toyknight.aeii.gui.sprite;

import com.toyknight.aeii.gui.ResourceManager;
import java.awt.Graphics;

/**
 *
 * @author toyknight
 */
public class CursorSprite extends Sprite {
	
	private int current_frame = 0;

	public CursorSprite(int tile_size) {
		super(tile_size, tile_size);
	}
	
	@Override
	public void update() {
		current_frame = current_frame == 0 ? 1 : 0;
	}
	
	@Override
	public void paint(Graphics g, int x, int y) {
		g.drawImage(
				ResourceManager.getCursorImage(current_frame), 
				x, y, getWidth(), getHeight(), null);
	}
	
}
