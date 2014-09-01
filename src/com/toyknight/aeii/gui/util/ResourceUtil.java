package com.toyknight.aeii.gui.util;

import java.awt.Color;
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

	public static BufferedImage getImage(String path) throws IOException {
		return ImageIO.read(new File(path));
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
