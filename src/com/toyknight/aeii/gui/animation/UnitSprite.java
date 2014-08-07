
package com.toyknight.aeii.gui.animation;

import com.toyknight.aeii.gui.ResourceManager;
import java.awt.Graphics;

/**
 *
 * @author toyknight
 */
public class UnitSprite extends Sprite {
	
	private final int team;
	private final int index;
	
	private static int current_frame = 0;
	private final static int delay = 3;
	private static int current_delay = 0;

	public UnitSprite(int team, int index, int width, int height) {
		super(width, height);
		this.team = team;
		this.index = index;
	}
	
	public static void updateFrame() {
		if(current_delay < delay) {
			current_delay ++;
		} else {
			current_delay = 0;
			current_frame = current_frame == 0 ? 1 : 0;
		}
	}
	
	@Override
	public void paint(Graphics g, int x, int y) {
		g.drawImage(ResourceManager.getUnitImage(team, index, current_frame), 
				x, y, getWidth(), getHeight(), null);
	}
	
}
