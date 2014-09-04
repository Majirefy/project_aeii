package com.toyknight.aeii.gui.sprite;

import com.toyknight.aeii.gui.ResourceManager;
import java.awt.Graphics;

/**
 *
 * @author toyknight
 */
public class AttackCursorSprite extends Sprite {

	private int current_frame = 0;

	public AttackCursorSprite(int ts) {
		super(ts / 24 * 40, ts / 24 * 41);
	}

	@Override
	public void doUpdate() {
		current_frame = current_frame < 2 ? current_frame + 1 : 0;
	}

	@Override
	public void paint(Graphics g, int x, int y) {
		int ts = getWidth() / 40 * 24;
		int dx = (ts - getWidth()) / 2;
		int dy = (ts - getHeight()) / 2;
		g.drawImage(
				ResourceManager.getAttackCursorImage(current_frame),
				x + dx, y + dy, getWidth(), getHeight(), null);
	}

}
