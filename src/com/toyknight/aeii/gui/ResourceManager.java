package com.toyknight.aeii.gui;

import com.toyknight.aeii.core.map.TileEntitySet;
import com.toyknight.aeii.core.unit.UnitFactory;
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
	private static BufferedImage[][][] units;
	private static BufferedImage[] cursor;
	private static BufferedImage move_target;
	private static BufferedImage move_alpha;
	private static BufferedImage attack_alpha;
	private static Color aeii_panel_bg;
	private static Color move_path_color;

	private ResourceManager() {
	}

	public static void init(int ts) throws IOException {
		img_logo = ImageIO.read(new File("res\\logo.png"));
		loadBorder();
		loadTiles(ts);
		loadTopTiles(ts);
		loadCursors(ts);
		loadUnits(ts);
		loadAlpha(ts);
		aeii_panel_bg = new Color(36, 42, 69);
		move_path_color = new Color(225, 0, 82);
	}

	private static void loadBorder() throws IOException {
		BufferedImage img_borders = ImageIO.read(new File("res\\img\\border.png"));
		borders = new BufferedImage[8];
		for (int i = 0; i < borders.length; i++) {
			BufferedImage border
					= ResourceUtil.getImageClip(img_borders, 16 * i, 0, 16, 16);
			borders[i] = border;
		}
	}

	private static void loadTiles(int ts) throws IOException {
		int tile_count = TileEntitySet.getTileCount();
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

	private static void loadUnits(int ts) throws IOException {
		int unit_count = UnitFactory.getUnitCount();
		units = new BufferedImage[4][unit_count][2];
		for (int team = 0; team < 4; team++) {
			File unit_team = new File("res\\img\\units\\unit_icons_" + team + ".png");
			BufferedImage img_units
					= new BufferedImage(unit_count * ts, ts * 2, BufferedImage.TYPE_INT_ARGB);
			img_units.getGraphics().drawImage(
					ImageIO.read(unit_team),
					0, 0,
					unit_count * ts, ts * 2,
					null);
			for (int index = 0; index < unit_count; index++) {
				BufferedImage unit_f0 = new BufferedImage(ts, ts, BufferedImage.TYPE_INT_ARGB);
				BufferedImage unit_f1 = new BufferedImage(ts, ts, BufferedImage.TYPE_INT_ARGB);
				unit_f0.getGraphics().drawImage(
						img_units,
						0 - index * ts, 0,
						img_units.getWidth(), img_units.getHeight(),
						null);
				unit_f1.getGraphics().drawImage(
						img_units,
						0 - index * ts, -ts,
						img_units.getWidth(), img_units.getHeight(),
						null);
				units[team][index][0] = unit_f0;
				units[team][index][1] = unit_f1;
			}
		}
	}

	private static void loadCursors(int ts) throws IOException {
		ts = ts / 24 * 26;
		cursor = new BufferedImage[2];
		for (int i = 0; i < 2; i++) {
			File cursor_file = new File("res\\img\\cursor_" + i + ".png");
			cursor[i] = new BufferedImage(ts, ts, BufferedImage.TYPE_INT_ARGB);
			cursor[i].getGraphics().drawImage(ImageIO.read(cursor_file), 0, 0, ts, ts, null);
		}
		File move_target_file = new File("res\\img\\move_target_cursor.png");
		move_target = new BufferedImage(ts, ts, BufferedImage.TYPE_INT_ARGB);
		move_target.getGraphics().drawImage(ImageIO.read(move_target_file), 0, 0, ts, ts, null);
	}

	private static void loadAlpha(int ts) throws IOException {
		File alpha_file = new File("res\\img\\alpha.png");
		BufferedImage img_alpha = new BufferedImage(ts * 2, ts, BufferedImage.TYPE_INT_ARGB);
		img_alpha.getGraphics().drawImage(ImageIO.read(alpha_file), 0, 0, ts * 2, ts, null);
		move_alpha = ResourceUtil.getImageClip(img_alpha, ts, 0, ts, ts);
		attack_alpha = ResourceUtil.getImageClip(img_alpha, 0, 0, ts, ts);
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

	public static BufferedImage getUnitImage(int team, int index, int frame) {
		return units[team][index][frame];
	}

	public static BufferedImage getCursorImage(int index) {
		return cursor[index];
	}
	
	public static BufferedImage getMoveTargetCursorImage() {
		return move_target;
	}

	public static BufferedImage getAttackAlpha() {
		return attack_alpha;
	}

	public static BufferedImage getMoveAlpha() {
		return move_alpha;
	}

	public static Color getAEIIPanelBg() {
		return aeii_panel_bg;
	}
	
	public static Color getMovePathColor() {
		return move_path_color;
	}

}
