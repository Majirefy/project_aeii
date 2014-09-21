package com.toyknight.aeii.gui.util;

import com.toyknight.aeii.gui.ResourceManager;
import java.awt.Graphics;

/**
 *
 * @author toyknight
 */
public class CharPainter {

	private static int ts;

	private static final int[] SIZE_TABLE = {9, 99, 999, 9999, 99999, 999999, 9999999, 99999999, 999999999, Integer.MAX_VALUE};

	public static void init(int ts) {
		CharPainter.ts = ts;
	}
	
	public static int getCharHeight() {
		return ts / 24 * 7;
	}
	
	public static int getCharWidth() {
		return ts / 24 * 6;
	}

	public static int getLCharHeight() {
		return ts / 24 * 11;
	}

	public static int getLCharWidth() {
		return ts / 24 * 8;
	}
	
	public static void paintNegativeNumber(Graphics g, int number, int x, int y) {
		int w = getCharWidth();
		g.drawImage(ResourceManager.getMinus(), x, y, null);
		paintNumber(g, number, x + w, y);
	}
	
	public static void paintNumber(Graphics g, int number, int x, int y) {
		int w = getCharWidth();
		int[] array = getIntArray(number);
		for (int i = 0; i < array.length; i++) {
			int n = array[i];
			g.drawImage(ResourceManager.getNumber(n), x + w * i, y, null);
		}
	}

	public static void paintPositiveLNumber(Graphics g, int number, int x, int y) {
		int lw = getLCharWidth();
		g.drawImage(ResourceManager.getLPlus(), x, y, null);
		paintLNumber(g, number, x + lw, y);
	}

	public static void paintNegativeLNumber(Graphics g, int number, int x, int y) {
		int lw = getLCharWidth();
		g.drawImage(ResourceManager.getLMinus(), x, y, null);
		paintLNumber(g, number, x + lw, y);
	}

	public static void paintLNumber(Graphics g, int number, int x, int y) {
		int lw = getLCharWidth();
		int[] array = getIntArray(number);
		for (int i = 0; i < array.length; i++) {
			int n = array[i];
			g.drawImage(ResourceManager.getLNumber(n), x + lw * i, y, null);
		}
	}
	
	public static int getNumberWidth(int number, boolean signed) {
		int w = getCharWidth();
		if (signed) {
			return w * getDigitsOfInt(number) + w;
		} else {
			return w * getDigitsOfInt(number);
		}
	}

	public static int getLNumberWidth(int number, boolean signed) {
		int lw = getLCharWidth();
		if (signed) {
			return lw * getDigitsOfInt(number) + lw;
		} else {
			return lw * getDigitsOfInt(number);
		}
	}

	private static int getDigitsOfInt(int n) {
		for (int i = 0;; i++) {
			if (n <= SIZE_TABLE[i]) {
				return i + 1;
			}
		}
	}

	private static int[] getIntArray(int n) {
		int length = getDigitsOfInt(n);
		int[] array = new int[length];
		int index = length - 1;
		if (n == 0) {
			array[0] = 0;
		} else {
			while (n > 0) {
				array[index] = n % 10;
				n = n / 10;
				index--;
			}
		}
		return array;
	}

}
