
package com.toyknight.aeii.gui.util;

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
	
}
