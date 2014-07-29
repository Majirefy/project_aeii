package com.toyknight.aeii.gui.animation;

import com.toyknight.aeii.gui.ResourceManager;
import java.awt.Graphics;

/**
 *
 * @author toyknight
 */
public class TileSprite extends Sprite {

	private final int[] frames = new int[2];
	private boolean is_animated = false;
	private static int current_frame_index = 0;
	private final static int delay = 3;
	private static int current_delay = 0;

	public TileSprite(int tile_size, int index) {
		super(tile_size, tile_size);
		frames[0] = index;
	}

	public void setAnimationTileIndex(int index) {
		frames[1] = index;
		is_animated = true;
	}

	public static void updateFrame() {
		if(current_delay < delay) {
			current_delay ++;
		} else {
			current_delay = 0;
			current_frame_index = current_frame_index == 0 ? 1 : 0;
		}
	}

	@Override
	public void paint(Graphics g, int x, int y) {
		if (is_animated) {
			g.drawImage(
					ResourceManager.getTileImage(frames[current_frame_index]),
					x, y, getWidth(), getHeight(), null);
		} else {
			g.drawImage(
					ResourceManager.getTileImage(frames[0]),
					x, y, getWidth(), getHeight(), null);
		}
	}

}
