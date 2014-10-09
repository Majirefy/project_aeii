package com.toyknight.aeii.gui.animation;

import com.toyknight.aeii.core.map.Tile;
import com.toyknight.aeii.gui.ResourceManager;
import com.toyknight.aeii.gui.screen.MapCanvas;
import com.toyknight.aeii.gui.sprite.TilePainter;
import java.awt.Graphics;

/**
 *
 * @author toyknight
 */
public class TileAttackedAnimation extends SwingAnimation {

	private final int ts;
	private final int x;
	private final int y;
	private final int tile_index;

	private int current_frame = 0;

	public TileAttackedAnimation(int tile_index, int x, int y, int ts) {
		this.ts = ts;
		this.x = x;
		this.y = y;
		this.tile_index = tile_index;
	}

	@Override
	protected void doUpdate() {
		if (current_frame < 9) {
			current_frame++;
		} else {
			complete();
		}
	}

	@Override
	public void paint(Graphics g, MapCanvas canvas) {
		int sx = canvas.getXOnCanvas(x);
		int sy = canvas.getYOnCanvas(y);
		int offset = (ts - ts / 24 * 20) / 2;
		TilePainter.paint(g, tile_index, sx, sy, ts);
		g.drawImage(ResourceManager.getAttackSparkImage(current_frame % 5), sx + offset, sy + offset, null);
	}

}
