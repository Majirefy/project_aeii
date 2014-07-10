
package com.toyknight.aeii.gui;

import com.toyknight.aeii.gui.util.ResourceUtil;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author toyknight
 */
public class ResManager {
	
	private static BufferedImage[] borders;
	private final static Color aeii_panel_bg = new Color(36, 42, 69);
	
	private ResManager() {}
	
	public static void init() throws IOException {
		loadBorder();
	}
	
	private static void loadBorder() throws IOException {
		BufferedImage img_borders = ImageIO.read(new File("res\\img\\border.png"));
		borders = new BufferedImage[8];
		for(int i=0;i<borders.length;i++) {
			BufferedImage border = 
					ResourceUtil.getImageClip(img_borders, 16*i, 0, 16, 16);
			borders[i] = border;
		}
	}
	
	public static BufferedImage getBorder(int index) {
		return borders[index];
	}
	
	public static Color getAEIIPanelBg() {
		return aeii_panel_bg;
	}
	
}
