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
		this.setBackground(Color.BLACK);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		paintLogo(g);
		if (isLogoEmerged()) {

		}
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
