package com.toyknight.aeii.gui.animation;

import com.toyknight.aeii.core.unit.Unit;
import com.toyknight.aeii.gui.screen.MapCanvas;
import com.toyknight.aeii.gui.sprite.UnitPainter;
import com.toyknight.aeii.gui.util.CharPainter;
import java.awt.Graphics;

/**
 *
 * @author toyknight
 */
public class UnitHpChangeAnimation extends UnitAnimation {

	private final int ts;
	private final int change;
	private final int[] y_offset = {2, 0, -1, -1, -2, -2, -2, -2, -1, -1, 0, 1, 2, 4, 6, 4, 3, 4, 6, 6, 6, 6};

	private int current_frame = 0;

	public UnitHpChangeAnimation(Unit unit, int change, int ts) {
		super(unit, unit.getX(), unit.getY());
		this.change = change;
		this.ts = ts;
	}

	@Override
	public void doUpdate() {
		if (current_frame < 21) {
			current_frame++;
		} else {
			complete();
		}
	}

	@Override
	public void paint(Graphics g, MapCanvas canvas) {
		int sx = canvas.getXOnCanvas(getUnit().getX());
		int sy = canvas.getYOnCanvas(getUnit().getY());
		canvas.locateViewport(getUnit().getX(), getUnit().getY());
		int dx = (ts - CharPainter.getLNumberWidth(Math.abs(change), true)) / 2;
		int dy = (ts - CharPainter.getLCharHeight()) / 2;
		UnitPainter.paint(g, getUnit(), sx, sy, ts);
		if (change >= 0) {
			CharPainter.paintPositiveLNumber(g, change, sx + dx, sy + dy + y_offset[current_frame]);
		} else {
			CharPainter.paintNegativeLNumber(g, Math.abs(change), sx + dx, sy + dy + y_offset[current_frame] * ts / 24);
		}
	}

}
