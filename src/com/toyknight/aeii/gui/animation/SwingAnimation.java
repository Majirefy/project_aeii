package com.toyknight.aeii.gui.animation;

import com.toyknight.aeii.core.animation.Animation;
import com.toyknight.aeii.gui.screen.MapCanvas;
import java.awt.Graphics;

/**
 *
 * @author toyknight
 */
public class SwingAnimation extends Animation {

	private int interval;
	private int current_delay;

	public SwingAnimation() {
		this(-1, -1);
	}

	public SwingAnimation(int x, int y) {
		if (x >= 0 && y >= 0) {
			this.addLocation(x, y);
		}
		setInterval(0);
	}

	public final void setInterval(int interval) {
		this.interval = interval;
		current_delay = 0;
	}

	protected void doUpdate() {

	}

	@Override
	public void update() {
		if (current_delay < interval) {
			current_delay++;
		} else {
			current_delay = 0;
			doUpdate();
		}
	}

	public void paint(Graphics g, MapCanvas canvas) {
	}

}
