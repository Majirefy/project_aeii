
package com.toyknight.aeii.gui.animation;

import com.toyknight.aeii.core.unit.Unit;
import com.toyknight.aeii.gui.ResourceManager;
import com.toyknight.aeii.gui.screen.MapCanvas;
import com.toyknight.aeii.gui.sprite.UnitPainter;
import java.awt.Graphics;

/**
 *
 * @author toyknight
 */
public class UnitDestroyedAnimation extends UnitAnimation {
	
	private final int ts;
	private int frame = 0;

	public UnitDestroyedAnimation(Unit unit, int ts) {
		super(unit, unit.getX(), unit.getY());
		this.ts = ts;
	}
	
	@Override
	protected void doUpdate() {
		if(frame < 5) {
			frame ++;
		} else {
			complete();
		}
	}
	
	@Override
	public void paint(Graphics g, MapCanvas canvas) {
		int sx_unit = canvas.getXOnCanvas(getUnit().getX());
		int sy_unit = canvas.getYOnCanvas(getUnit().getY());
		UnitPainter.paint(g, getUnit(), sx_unit, sy_unit, ts);
		g.drawImage(ResourceManager.getWhiteSparkImage(frame), sx_unit, sy_unit, null);
	}
	
}
