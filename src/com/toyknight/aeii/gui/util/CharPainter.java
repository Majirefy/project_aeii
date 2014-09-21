package com.toyknight.aeii.gui.util;

import com.toyknight.aeii.gui.ResourceManager;
import java.awt.Graphics;

/**
 *
 * @author toyknight
 */
public class CharPainter {

	private static final int[] SIZE_TABLE = {9, 99, 999, 9999, 99999, 999999, 9999999, 99999999, 999999999, Integer.MAX_VALUE};

	public static void paintPositiveLNumber(Graphics g, int number, int x, int y, int ts) {
		int lw = ts / 24 * 8;
		g.drawImage(ResourceManager.getLPlus(), x, y, null);
		paintLNumber(g, number, x + lw, y, ts);
	}

	public static void paintNegativeLNumber(Graphics g, int number, int x, int y, int ts) {
		int lw = ts / 24 * 8;
		g.drawImage(ResourceManager.getLMinus(), x, y, null);
		paintLNumber(g, number, x + lw, y, ts);
	}

	public static void paintLNumber(Graphics g, int number, int x, int y, int ts) {
		int lw = ts / 24 * 8;
		int[] array = getIntArray(number);
		for (int i = 0; i < array.length; i++) {
			int n = array[i];
			g.drawImage(ResourceManager.getLNumber(n), x + lw * i, y, null);
		}
	}

	public static int getLNumberWidth(int number, boolean signed, int ts) {
		int lw = ts / 24 * 8;
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
