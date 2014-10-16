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
public class UnitLevelUpAnimation extends UnitAnimation {

	private final int ts;
	private int current_frame = 0;
	private final int[] offset = {1, 1, 1, 0, -1, -2, -3, -4, -4, -4, -4, -4, -4};

	public UnitLevelUpAnimation(Unit unit, int ts) {
		super(unit, unit.getX(), unit.getY());
		this.ts = ts;
	}

	@Override
	protected void doUpdate() {
		if (current_frame < 12) {
			current_frame++;
		} else {
			complete();
		}
	}

	@Override
	public void paint(Graphics g, MapCanvas canvas) {
		int unit_x = canvas.getXOnCanvas(getUnit().getX());
		int unit_y = canvas.getYOnCanvas(getUnit().getY());
		UnitPainter.paint(g, getUnit(), unit_x, unit_y, ts);
		int icon_x = (ts - ts / 24 * 62) / 2 + unit_x;
		int icon_y = unit_y + ts / 24 * offset[current_frame];
		g.drawImage(ResourceManager.getLevelUpIcon(), icon_x, icon_y, canvas);
	}

}
