package com.toyknight.aeii.gui.screen;

import com.toyknight.aeii.gui.AEIIApplet;
import com.toyknight.aeii.gui.ResManager;
import com.toyknight.aeii.gui.Screen;
import com.toyknight.aeii.gui.effect.ImageWaveEffect;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author toyknight
 */
public class MainMenuScreen extends Screen {

	private int logoDrapValue = 0;

	public MainMenuScreen(Dimension size, AEIIApplet context) {
		super(size, context);
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		paintLogo(g);
		if (isLogoEmerged()) {

		}
		super.paint(g);
	}

	private void paintLogo(Graphics g) {
		BufferedImage img_logo = ResManager.getLogoImage();
		ImageWaveEffect.drawWavedImage(
				g, logoDrapValue, 40,
				img_logo, 0, 0, 1);
	}

	private boolean isLogoEmerged() {
		return logoDrapValue >= 40;
	}

	@Override
	public void update() {
		if (isLogoEmerged()) {
			
		} else {
			logoDrapValue++;
		}
	}

}
