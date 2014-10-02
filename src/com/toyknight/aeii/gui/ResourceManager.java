package com.toyknight.aeii.gui;

import com.toyknight.aeii.core.map.TileRepository;
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
	private static BufferedImage[][] action_buttons;
	private static BufferedImage[] borders;
	private static BufferedImage[] tiles;
	private static BufferedImage[] top_tiles;
	private static BufferedImage tomb;
	private static BufferedImage[][][] units;
	private static BufferedImage[][] standby_units;
	private static BufferedImage[] cursor;
	private static BufferedImage move_target;
	private static BufferedImage[] attack_cursor;
	private static BufferedImage[] attack_spark;
	private static BufferedImage move_alpha;
	private static BufferedImage attack_alpha;
	private static BufferedImage[] white_spark;
	private static BufferedImage[] smoke;
	private static BufferedImage[] numbers;
	private static BufferedImage[] lnumbers;
	private static BufferedImage minus;
	private static BufferedImage lplus;
	private static BufferedImage lminus;
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
		loadSparks(ts);
		loadAlpha(ts);
		loadSmoke(ts);
		loadChars(ts);
		loadActionButtons(ts);
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
		int tile_count = TileRepository.getTileCount();
		tiles = new BufferedImage[tile_count];
		for (int i = 0; i < tile_count; i++) {
			File tile = new File("res\\img\\tiles\\tile_" + i + ".png");

			tiles[i] = new BufferedImage(ts, ts, BufferedImage.TYPE_INT_ARGB);
			tiles[i].getGraphics().drawImage(ImageIO.read(tile), 0, 0, ts, ts, null);
		}
		File tomb_file = new File("res\\img\\tombstone.png");
		tomb = new BufferedImage(ts, ts, BufferedImage.TYPE_INT_ARGB);
		tomb.getGraphics().drawImage(ImageIO.read(tomb_file), 0, 0, ts, ts, null);
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
		standby_units = new BufferedImage[4][unit_count];
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
				standby_units[team][index] = ResourceUtil.getGrayScaledImage(unit_f0);
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
		int acw = ts / 26 * 40;
		int ach = ts / 26 * 41;
		attack_cursor = new BufferedImage[3];
		for (int i = 0; i < 3; i++) {
			File cursor_file = new File("res\\img\\attack_cursor_" + i + ".png");
			attack_cursor[i] = new BufferedImage(acw, ach, BufferedImage.TYPE_INT_ARGB);
			attack_cursor[i].getGraphics().drawImage(ImageIO.read(cursor_file), 0, 0, acw, ach, null);
		}
	}

	private static void loadSparks(int ts) throws IOException {
		int asts = ts / 24 * 20;
		attack_spark = new BufferedImage[6];
		File attack_spark_file = new File("res\\img\\attack_spark.png");
		BufferedImage attack_sparks = new BufferedImage(asts * 6, asts, BufferedImage.TYPE_INT_ARGB);
		attack_sparks.getGraphics().drawImage(ImageIO.read(attack_spark_file), 0, 0, asts * 6, asts, null);
		for (int i = 0; i < 6; i++) {
			attack_spark[i] = ResourceUtil.getImageClip(attack_sparks, asts * i, 0, asts, asts);
		}
		white_spark = new BufferedImage[6];
		File spark_file = new File("res\\img\\white_spark.png");
		BufferedImage white_sparks = new BufferedImage(ts * 6, ts, BufferedImage.TYPE_INT_ARGB);
		white_sparks.getGraphics().drawImage(ImageIO.read(spark_file), 0, 0, ts * 6, ts, null);
		for (int i = 0; i < 6; i++) {
			white_spark[i] = ResourceUtil.getImageClip(white_sparks, ts * i, 0, ts, ts);
		}
	}

	private static void loadAlpha(int ts) throws IOException {
		File alpha_file = new File("res\\img\\alpha.png");
		BufferedImage img_alpha = new BufferedImage(ts * 2, ts, BufferedImage.TYPE_INT_ARGB);
		img_alpha.getGraphics().drawImage(ImageIO.read(alpha_file), 0, 0, ts * 2, ts, null);
		move_alpha = ResourceUtil.getImageClip(img_alpha, ts, 0, ts, ts);
		attack_alpha = ResourceUtil.getImageClip(img_alpha, 0, 0, ts, ts);
	}

	private static void loadSmoke(int ts) throws IOException {
		int h = ts / 24 * 20;
		smoke = new BufferedImage[4];
		File smoke_file = new File("res\\img\\smoke.png");
		BufferedImage smokes = new BufferedImage(ts * 4, h, BufferedImage.TYPE_INT_ARGB);
		smokes.getGraphics().drawImage(ImageIO.read(smoke_file), 0, 0, ts * 4, h, null);
		for (int i = 0; i < 4; i++) {
			smoke[i] = ResourceUtil.getImageClip(smokes, ts * i, 0, ts, h);
		}
	}

	private static void loadChars(int ts) throws IOException {
		int w = ts / 24 * 6;
		int h = ts / 24 * 7;
		int lw = ts / 24 * 8;
		int lh = ts / 24 * 11;
		File chars_file = new File("res\\img\\chars.png");
		BufferedImage img_chars = new BufferedImage(w * 12, h, BufferedImage.TYPE_INT_ARGB);
		img_chars.getGraphics().drawImage(ImageIO.read(chars_file), 0, 0, w * 12, h, null);
		File lchars_file = new File("res\\img\\lchars.png");
		BufferedImage img_lchars = new BufferedImage(lw * 13, lh, BufferedImage.TYPE_INT_ARGB);
		img_lchars.getGraphics().drawImage(ImageIO.read(lchars_file), 0, 0, lw * 13, lh, null);
		numbers = new BufferedImage[10];
		lnumbers = new BufferedImage[10];
		for (int i = 0; i < 10; i++) {
			numbers[i] = ResourceUtil.getImageClip(img_chars, i * w, 0, w, h);
			lnumbers[i] = ResourceUtil.getImageClip(img_lchars, i * lw, 0, lw, lh);
		}
		minus = ResourceUtil.getImageClip(img_chars, 10 * w, 0, w, h);
		lminus = ResourceUtil.getImageClip(img_lchars, 11 * lw, 0, lw, lh);
		lplus = ResourceUtil.getImageClip(img_lchars, 12 * lw, 0, lw, lh);
	}

	private static void loadActionButtons(int ts) throws IOException {
		int iw = ts / 24 * 16;
		int ih = ts / 24 * 16;
		int cw = ts / 24 * 20;
		int ch = ts / 24 * 21;
		File icons_file = new File("res\\img\\action_icons.png");
		BufferedImage action_icons = new BufferedImage(iw * 7, ih, BufferedImage.TYPE_INT_ARGB);
		action_icons.getGraphics().drawImage(ImageIO.read(icons_file), 0, 0, iw * 7, ih, null);
		BufferedImage[] icon_list = new BufferedImage[7];
		for (int i = 0; i < 7; i++) {
			icon_list[i] = ResourceUtil.getImageClip(action_icons, iw * i, 0, iw, ih);
		}
		File circle_file = new File("res\\img\\small_circle.png");
		BufferedImage small_circles = new BufferedImage(cw * 2, ch, BufferedImage.TYPE_INT_ARGB);
		small_circles.getGraphics().drawImage(ImageIO.read(circle_file), 0, 0, cw * 2, ch, null);
		BufferedImage[] circle_list = new BufferedImage[2];
		for (int i = 0; i < 2; i++) {
			circle_list[i] = ResourceUtil.getImageClip(small_circles, cw * i, 0, cw, ch);
		}
		action_buttons = new BufferedImage[7][2];
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 2; j++) {
				action_buttons[i][j] = new BufferedImage(cw, ch, BufferedImage.TYPE_INT_ARGB);
				action_buttons[i][j].getGraphics().drawImage(circle_list[j], 0, 0, null);
				action_buttons[i][j].getGraphics().drawImage(icon_list[i], (cw - iw) / 2, (ch - ih) / 2, null);
			}
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

	public static BufferedImage getTombImage() {
		return tomb;
	}

	public static BufferedImage getUnitImage(int team, int index, int frame) {
		return units[team][index][frame];
	}

	public static BufferedImage getStandbyUnitImage(int team, int index) {
		return standby_units[team][index];
	}

	public static BufferedImage getCursorImage(int index) {
		return cursor[index];
	}

	public static BufferedImage getMoveTargetCursorImage() {
		return move_target;
	}

	public static BufferedImage getAttackCursorImage(int index) {
		return attack_cursor[index];
	}

	public static BufferedImage getAttackSparkImage(int index) {
		return attack_spark[index];
	}

	public static BufferedImage getWhiteSparkImage(int index) {
		return white_spark[index];
	}

	public static BufferedImage getSmokeImage(int index) {
		return smoke[index];
	}

	public static BufferedImage getAttackAlpha() {
		return attack_alpha;
	}

	public static BufferedImage getMoveAlpha() {
		return move_alpha;
	}

	public static BufferedImage getNumber(int n) {
		return numbers[n];
	}

	public static BufferedImage getLNumber(int n) {
		return lnumbers[n];
	}

	public static BufferedImage getMinus() {
		return minus;
	}

	public static BufferedImage getLPlus() {
		return lplus;
	}

	public static BufferedImage getLMinus() {
		return lminus;
	}
	
	public static BufferedImage getActionButtonImage(int index, int frame) {
		return action_buttons[index][frame];
	}

	public static Color getAEIIPanelBg() {
		return aeii_panel_bg;
	}

	public static Color getMovePathColor() {
		return move_path_color;
	}

}
