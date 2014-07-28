
package com.toyknight.aeii.gui;

import com.toyknight.aeii.core.map.TileFactory;
import com.toyknight.aeii.gui.util.ResourceUtil;
import com.toyknight.aeii.gui.util.SuffixFileFilter;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author toyknight
 */
public class ResourceManager {
	
	private static BufferedImage img_logo;
	private static BufferedImage[] borders;
	private static BufferedImage[] tiles;
	private static BufferedImage[] top_tiles;
	private static Color aeii_panel_bg;
	
	private ResourceManager() {}
	
	public static void init(int ts) throws IOException {
		img_logo = ImageIO.read(new File("res\\logo.png"));
		loadBorder();
		loadTiles(ts);
		loadTopTiles(ts);
		aeii_panel_bg = new Color(36, 42, 69);
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
	
	private static void loadTiles(int ts) throws IOException {
		int tile_count = TileFactory.getTileCount();
		tiles = new BufferedImage[tile_count];
		for (int i = 0; i < tile_count; i++) {
			File tile = new File("res\\img\\tiles\\tile_" + i + ".png");

			tiles[i] = new BufferedImage(ts, ts, BufferedImage.TYPE_INT_ARGB);
			tiles[i].getGraphics().drawImage(ImageIO.read(tile), 0, 0, ts, ts, null);
		}
	}
	
	private static void loadTopTiles(int ts) throws IOException {
		File img_top_tile_dir = new File("res\\img\\tiles\\top_tiles");
		int top_tile_count = img_top_tile_dir.listFiles(new SuffixFileFilter("png")).length;
		top_tiles = new BufferedImage[top_tile_count];
		for (int i = 0; i < top_tile_count; i++) {
			File tile = new File("res\\img\\tiles\\top_tiles\\top_tile_" + i + ".png");
			top_tiles[i] = new BufferedImage(ts, ts, BufferedImage.TYPE_INT_ARGB);
			top_tiles[i].getGraphics().drawImage(ImageIO.read(tile), 0, 0, ts, ts, null);
		}
	}
	
	public static BufferedImage getLogoImage() {
		return img_logo;
	}
	
	public static BufferedImage getBorderImage(int index) {
		return borders[index];
	}
	
	public static BufferedImage getTileImage(int index) {
		return tiles[index];
	}
	
	public static BufferedImage getTopTileImage(int index) {
		return top_tiles[index];
	}
	
	public static Color getAEIIPanelBg() {
		return aeii_panel_bg;
	}
	
}
