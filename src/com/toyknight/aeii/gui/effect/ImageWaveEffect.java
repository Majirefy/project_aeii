package com.toyknight.aeii.gui.effect;

import com.toyknight.aeii.Launcher;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author toyknight
 */
public class ImageWaveEffect {

	private ImageWaveEffect() {
	}

	private static short[] sintab = null;
	private final static int FULL_CIRCLE = 360;

	public static void drawWavedImage(Graphics g, int current, int sum, BufferedImage img, int x, int y, int t) {
		int width = img.getWidth();
		int height = img.getHeight();
		int halfWidth = width / 2;
		int wholeHeight = height / 1;
		int frameSize = (width * current) / sum;
		int magnitude = (width * (sum - current)) / (sum * 4);
		int angle = (360 * current) / sum;
		int anglestep = (360 * t) / wholeHeight;
		for (int i = 0; i < wholeHeight; i++) {
			int offset = magnitude * sin(angle) >> 10;
			g.setClip(((x + halfWidth) - frameSize / 2) + offset, y + i * 1, frameSize, 1);
			g.drawImage(img, x + offset, y, null);
			angle += anglestep;
		}
		int screen_width = Launcher.getWindow().getContentPane().getWidth();
		int screen_height  = Launcher.getWindow().getContentPane().getHeight();
		g.setClip(0, 0, screen_width, screen_height);
	}

	public static void createSinTab() {
		sintab = new short[FULL_CIRCLE];
		int var1 = FULL_CIRCLE * 10000 / 2 / 31415;
		int var2 = 1024 * var1;
		int var3 = 0;

		for (int var5 = 0; var5 < FULL_CIRCLE; ++var5) {
			int var4 = var3 / var1;
			sintab[var5] = (short) var4;
			var2 -= var4;
			var3 += var2 / var1;
		}

		sintab[180] = 0;
		sintab[270] = -1024;
	}

	private static short sin(int angle) {
		angle %= 360;
		return sintab[angle];
	}

}
