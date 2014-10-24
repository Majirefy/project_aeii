package com.toyknight.aeii.gui.animation;

import com.toyknight.aeii.gui.ResourceManager;
import com.toyknight.aeii.gui.screen.MapCanvas;
import java.awt.Graphics;

/**
 *
 * @author toyknight
 */
public class SmokeAnimation extends MapAnimation {

	private final int ts;

	private final int x;
	private final int y;
	private int frame = 0;

	public SmokeAnimation(int x, int y, int ts) {
		super(x, y);
		this.x = x;
		this.y = y;
		this.ts = ts;
	}

	@Override
	protected void doUpdate() {
		if (frame < 3) {
			frame++;
		} else {
			complete();
		}
	}

	@Override
	public void paint(Graphics g, MapCanvas canvas) {
		int sx_smoke = canvas.getXOnCanvas(x);
		int sy_smoke = canvas.getYOnCanvas(y);
		g.drawImage(ResourceManager.getSmokeImage(frame), sx_smoke, sy_smoke - frame * ts / 6, canvas);
	}

}
