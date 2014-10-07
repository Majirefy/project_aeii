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
public class SummonAnimation extends UnitAnimation {

	private final int ts;
	private int frame = 0;
	private final int target_x;
	private final int target_y;

	public SummonAnimation(Unit summoner, int target_x, int target_y, int ts) {
		super(summoner, target_x, target_y);
		this.ts = ts;
		this.target_x = target_x;
		this.target_y = target_y;
		this.addLocation(summoner.getX(), summoner.getY());
	}

	@Override
	protected void doUpdate() {
		if (frame < 9) {
			frame++;
		} else {
			complete();
		}
	}

	@Override
	public void paint(Graphics g, MapCanvas canvas) {
		int offset = ts / 24 * 2;
		int actual_frame = frame % 5;
		int sx_target = canvas.getXOnCanvas(target_x);
		int sy_target = canvas.getYOnCanvas(target_y);
		int sx_summoner = canvas.getXOnCanvas(getUnit().getX());
		int sy_summoner = canvas.getYOnCanvas(getUnit().getY());
		UnitPainter.paint(g, getUnit(), sx_summoner, sy_summoner, ts);
		g.drawImage(ResourceManager.getWhiteSparkImage(actual_frame), sx_target - offset, sy_target - offset, null);
		g.drawImage(ResourceManager.getWhiteSparkImage(actual_frame), sx_target - offset, sy_target + offset, null);
		g.drawImage(ResourceManager.getWhiteSparkImage(actual_frame), sx_target + offset, sy_target - offset, null);
		g.drawImage(ResourceManager.getWhiteSparkImage(actual_frame), sx_target + offset, sy_target + offset, null);
		g.drawImage(ResourceManager.getWhiteSparkImage(actual_frame), sx_target, sy_target, null);
	}

}
