package com.toyknight.aeii.gui.sprite;

import com.toyknight.aeii.core.unit.Unit;
import com.toyknight.aeii.gui.ResourceManager;
import com.toyknight.aeii.gui.util.CharPainter;
import java.awt.Graphics;

/**
 *
 * @author toyknight
 */
public class UnitPainter {

	private static int current_frame = 0;

	private UnitPainter() {
	}

	public static void updateFrame() {
		current_frame = current_frame == 0 ? 1 : 0;
	}

	public static void paint(Graphics g, Unit unit, int x, int y, int ts) {
		int team = unit.getTeam();
		int index = unit.getIndex();
		if (unit.isStandby()) {
			g.drawImage(ResourceManager.getStandbyUnitImage(team, index),
					x, y, ts, ts, null);
		} else {
			g.drawImage(ResourceManager.getUnitImage(team, index, current_frame),
					x, y, ts, ts, null);
		}
		if (unit.getCurrentHp() < unit.getMaxHp()) {
			int hp_dx = 0;
			int hp_dy = ts - CharPainter.getCharHeight();
			CharPainter.paintNumber(g, unit.getCurrentHp(), x + hp_dx, y + hp_dy);
		}
	}

}
