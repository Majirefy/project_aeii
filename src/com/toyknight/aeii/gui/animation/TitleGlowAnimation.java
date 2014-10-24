package com.toyknight.aeii.gui.animation;

import com.toyknight.aeii.core.animation.Animation;
import com.toyknight.aeii.gui.ResourceManager;
import com.toyknight.aeii.gui.Screen;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author toyknight
 */
public class TitleGlowAnimation extends Animation {

	private final Screen screen;

	private int glow_x;

	public TitleGlowAnimation(Screen screen) {
		this.screen = screen;
	}

	@Override
	protected void doUpdate() {
		if (glow_x < screen.getWidth()) {
			glow_x += 16;
		} else {
			glow_x = -67;
		}
	}

	public void paint(Graphics g) {
		int mask_width = ResourceManager.getTitleMask().getWidth();
		int mask_height = ResourceManager.getTitleMask().getHeight();
		g.drawImage(ResourceManager.getTitleImage(), 0, 0, screen);
		g.drawImage(ResourceManager.getTitleGlow(), glow_x, 0, screen);
		g.drawImage(ResourceManager.getTitleMask(), 0, 0, screen);
		g.setColor(Color.BLACK);
		g.fillRect(mask_width, 0, screen.getWidth() - mask_width, mask_height);
	}

}
