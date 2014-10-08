package com.toyknight.aeii.gui.animation;

import com.toyknight.aeii.Launcher;
import com.toyknight.aeii.core.animation.Animation;
import com.toyknight.aeii.gui.Screen;
import com.toyknight.aeii.gui.effect.ImageWaveEffect;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author toyknight
 */
public class LogoAnimation extends Animation {

	private final BufferedImage ms_logo;
	private Color bgColor = Color.WHITE;

	private int logoDrapValue = 40;

	public LogoAnimation() {
		BufferedImage img;
		try {
			img = ImageIO.read(Launcher.class.getResource("ms_logo.png"));
		} catch (IOException ex) {
			img = new BufferedImage(88, 88, BufferedImage.TYPE_INT_ARGB);
		}
		ms_logo = img;
	}
	
	public void paint(Graphics g, Screen screen) {
		g.setColor(bgColor);
		g.fillRect(0, 0, screen.getWidth(), screen.getHeight());
		int x = (screen.getWidth() - ms_logo.getWidth()) / 2;
		int y = (screen.getHeight() - ms_logo.getHeight()) / 2;
		ImageWaveEffect.drawWavedImage(g, logoDrapValue, 40, ms_logo, x, y, 4);
	}

	@Override
	public void update() {
		if (logoDrapValue > 0) {
			logoDrapValue--;
		} else {
			if (bgColor.equals(Color.BLACK)) {
				complete();
			} else {
				bgColor = new Color(bgColor.getRed() - 17, bgColor.getGreen() - 17, bgColor.getBlue() - 17);
			}
		}
	}

}
