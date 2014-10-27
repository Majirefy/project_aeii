package com.toyknight.aeii.gui.util;

import com.toyknight.aeii.gui.ResourceManager;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author toyknight
 */
public class ResourceUtil {

	private ResourceUtil() {
	}

	public static BufferedImage getImageClip(BufferedImage source, int x, int y, int width, int height) {
		BufferedImage clip = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		clip.getGraphics().drawImage(source, -x, -y, null);
		return clip;
	}

	public static void paintBorder(Graphics g, int x, int y, int width, int height) {
		g.drawImage(ResourceManager.getBorderImage(0), x, y, 16, 16, null);
		g.drawImage(ResourceManager.getBorderImage(1), x + 16, y, width - 32, 16, null);
		g.drawImage(ResourceManager.getBorderImage(2), x + width - 16, y, 16, 16, null);
		g.drawImage(ResourceManager.getBorderImage(3), x, y + 16, 16, height - 32, null);
		g.drawImage(ResourceManager.getBorderImage(4), x + width - 16, y + 16, 16, height - 32, null);
		g.drawImage(ResourceManager.getBorderImage(5), x, y + height - 16, 16, 16, null);
		g.drawImage(ResourceManager.getBorderImage(6), x + 16, y + height - 16, width - 32, 16, null);
		g.drawImage(ResourceManager.getBorderImage(7), x + width - 16, y + height - 16, 16, 16, null);
	}

	public static BufferedImage getGrayScaledImage(BufferedImage source) {
		BufferedImage target = new BufferedImage(
				source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < source.getWidth(); x++) {
			for (int y = 0; y < source.getHeight(); y++) {
				int rgb = source.getRGB(x, y);
				if ((rgb >> 24) != 0) {
					Color sc = new Color(source.getRGB(x, y));
					int r = sc.getRed();
					int g = sc.getGreen();
					int b = sc.getBlue();
					int gs = (r + g + b) / 3;
					int gs_rgb = new Color(gs, gs, gs).getRGB();
					target.setRGB(x, y, gs_rgb);
				}
			}
		}
		return target;
	}

}
