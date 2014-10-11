package com.toyknight.aeii.gui.animation;

import com.toyknight.aeii.core.Point;
import com.toyknight.aeii.core.unit.Unit;
import com.toyknight.aeii.gui.screen.MapCanvas;
import com.toyknight.aeii.gui.sprite.UnitPainter;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 *
 * @author toyknight
 */
public class UnitMoveAnimation extends UnitAnimation {

	private final int ts;

	private final ArrayList<Point> path;
	private int current_location;
	private int x_offset;
	private int y_offset;

	public UnitMoveAnimation(Unit unit, ArrayList<Point> path, int ts) {
		super(unit, unit.getX(), unit.getY());
		this.ts = ts;
		this.path = path;
		current_location = 0;
		x_offset = 0;
		y_offset = 0;
	}

	@Override
	public void doUpdate() {
		if (current_location < path.size() - 1) {
			Point current = path.get(current_location);
			Point next = path.get(current_location + 1);
			if (current.x > next.x) {
				x_offset -= ts / 4;
			}
			if (current.x < next.x) {
				x_offset += ts / 4;
			}
			if (current.y > next.y) {
				y_offset -= ts / 4;
			}
			if (current.y < next.y) {
				y_offset += ts / 4;
			}
			if (Math.abs(x_offset) >= ts || Math.abs(y_offset) >= ts) {
				x_offset = 0;
				y_offset = 0;
				current_location++;
			}
		} else {
			this.complete();
		}
	}

	@Override
	public void paint(Graphics g, MapCanvas canvas) {
		if (path.size() > 0) {
			Point current = path.get(current_location);
			int sx = canvas.getXOnCanvas(current.x);
			int sy = canvas.getYOnCanvas(current.y);
			validateViewport(canvas, sx, sy);
			UnitPainter.paint(g, getUnit(), sx + x_offset, sy + y_offset, ts);
			
		}
	}

	private void validateViewport(MapCanvas canvas, int sx, int sy) {
		if (canvas.isWithinCanvas(sx, sy)) {
			if (sx + x_offset > canvas.getWidth() - ts * 2) {
				canvas.dragViewport(ts / 4, 0);
			}
			if (sx + x_offset < ts) {
				canvas.dragViewport(-ts / 4, 0);
			}
			if (sy + y_offset > canvas.getHeight() - ts * 2) {
				canvas.dragViewport(0, ts / 4);
			}
			if (sy + y_offset < ts) {
				canvas.dragViewport(0, -ts / 4);
			}
		} else {
			Point current = path.get(current_location);
			canvas.locateViewport(current.x, current.y);
		}
	}

}
