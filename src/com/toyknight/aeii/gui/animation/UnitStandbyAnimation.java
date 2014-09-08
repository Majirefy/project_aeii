
package com.toyknight.aeii.gui.animation;

import com.toyknight.aeii.core.unit.Unit;
import com.toyknight.aeii.gui.screen.MapCanvas;
import com.toyknight.aeii.gui.sprite.UnitPainter;
import java.awt.Graphics;

/**
 *
 * @author toyknight
 */
public class UnitStandbyAnimation extends UnitAnimation {
	
	private final int ts;

	public UnitStandbyAnimation(Unit unit, int ts) {
		super(new Unit(unit), unit.getX(), unit.getY());
		this.ts = ts;
	}
	
	@Override
	public void doUpdate() {
		complete();
	}
	
	@Override
	public void paint(Graphics g, MapCanvas canvas) {
		int sx = canvas.getXOnCanvas(getX());
		int sy = canvas.getYOnCanvas(getY());
		UnitPainter.paint(g, getUnit(), sx, sy, ts);
	}
	
}
