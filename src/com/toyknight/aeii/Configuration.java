package com.toyknight.aeii;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author toyknight
 */
public class Configuration {

	private static int speed;
	private static int scale;
	private static int base_size;

	private static int key_up;
	private static int key_down;
	private static int key_left;
	private static int key_right;
	private static int key_cancel;
	private static int key_info;
	
	private static String lang_file;

	private static final File CONFIG_FILE = new File("config.xml");

	private Configuration() {
	}

	public static void init() throws IOException {
		if (CONFIG_FILE.exists()) {
			loadConfig();
		} else {
			restoreDefaultConfig();
			loadConfig();
		}
	}

	private static void loadConfig() throws IOException {
		Properties config = new Properties();
		FileInputStream fis = new FileInputStream(CONFIG_FILE);
		config.loadFromXML(fis);
		fis.close();
		speed = Integer.parseInt(config.getProperty("SPEED", Integer.toString(30)));
		scale = Integer.parseInt(config.getProperty("CANVAS_SCALE", Integer.toString(1)));
		base_size = Integer.parseInt(config.getProperty("BASE_TILE_SIZE", Integer.toString(48)));
		key_up = Integer.parseInt(config.getProperty("KEY_UP", Integer.toString(KeyEvent.VK_UP)));
		key_down = Integer.parseInt(config.getProperty("KEY_DOWN", Integer.toString(KeyEvent.VK_DOWN)));
		key_left = Integer.parseInt(config.getProperty("KEY_LEFT", Integer.toString(KeyEvent.VK_LEFT)));
		key_right = Integer.parseInt(config.getProperty("KEY_RIGHT", Integer.toString(KeyEvent.VK_RIGHT)));
		key_cancel = Integer.parseInt(config.getProperty("KEY_CANCEL", Integer.toString(KeyEvent.VK_ESCAPE)));
		key_info = Integer.parseInt(config.getProperty("KEY_INFO", Integer.toString(KeyEvent.VK_I)));
		lang_file = config.getProperty("LANGUAGE_FILE", "en.lang");
	}

	private static void restoreDefaultConfig() throws IOException {
		Properties config = new Properties();
		config.setProperty("SPEED", Integer.toString(30));
		config.setProperty("CANVAS_SCALE", Integer.toString(1));
		config.setProperty("BASE_TILE_SIZE", Integer.toString(48));
		config.setProperty("KEY_UP", Integer.toString(KeyEvent.VK_UP));
		config.setProperty("KEY_DOWN", Integer.toString(KeyEvent.VK_DOWN));
		config.setProperty("KEY_LEFT", Integer.toString(KeyEvent.VK_LEFT));
		config.setProperty("KEY_RIGHT", Integer.toString(KeyEvent.VK_RIGHT));
		config.setProperty("KEY_CANCEL", Integer.toString(KeyEvent.VK_ESCAPE));
		config.setProperty("KEY_INFO", Integer.toString(KeyEvent.VK_I));
		config.setProperty("LANGUAGE_FILE", "en.lang");
		FileOutputStream fos = new FileOutputStream(CONFIG_FILE);
		config.storeToXML(fos, "Please do not modify this file manually.");
		fos.flush();
		fos.close();
	}

	public static int getGameSpeed() {
		return speed;
	}

	public static int getCanvasScale() {
		return scale;
	}

	public static int getBaseTileSize() {
		return base_size;
	}

	public static int getKeyUp() {
		return key_up;
	}

	public static int getKeyDown() {
		return key_down;
	}

	public static int getKeyLeft() {
		return key_left;
	}

	public static int getKeyRight() {
		return key_right;
	}

	public static int getKeyCancel() {
		return key_cancel;
	}

	public static int getKeyInfo() {
		return key_info;
	}
	
	public static String getLanguageFile() {
		return lang_file;
	}

}
