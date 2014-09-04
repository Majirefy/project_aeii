package com.toyknight.aeii.gui.animation;

import com.toyknight.aeii.core.unit.Unit;
import com.toyknight.aeii.gui.ResourceManager;
import com.toyknight.aeii.gui.screen.MapCanvas;
import com.toyknight.aeii.gui.sprite.UnitPainter;
import java.awt.Graphics;
import java.util.Random;

/**
 *
 * @author toyknight
 */
public class UnitAttackedAnimation extends UnitAnimation {

	private final int ts;
	private final Random random;

	private int current_frame;
	private int unit_dx;
	private int unit_dy;

	public UnitAttackedAnimation(Unit unit, int damage, int ts) {
		super(unit, unit.getX(), unit.getY());
		this.ts = ts;
		current_frame = 0;
		random = new Random(System.currentTimeMillis());
	}

	@Override
	protected void doUpdate() {
		if (current_frame < 5) {
			current_frame++;
		} else {
			complete();
		}
	}
	
	@Override
	public void update() {
		unit_dx = random.nextInt(ts / 12) - ts / 24;
		unit_dy = random.nextInt(ts / 12) - ts / 24;
		super.update();
	}

	@Override
	public void paint(Graphics g, MapCanvas canvas) {
		int offset = (ts - ts / 24 * 20) / 2;
		int sx = canvas.getXOnCanvas(getX());
		int sy = canvas.getYOnCanvas(getY());
		UnitPainter.paint(g, getUnit(), sx + unit_dx, sy + unit_dy, ts);
		g.drawImage(ResourceManager.getAttackSparkImage(current_frame), sx + offset, sy + offset, null);
	}

}
