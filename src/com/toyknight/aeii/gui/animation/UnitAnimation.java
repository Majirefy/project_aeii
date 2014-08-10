
package com.toyknight.aeii.gui.animation;

import com.toyknight.aeii.gui.sprite.UnitSprite;

/**
 *
 * @author toyknight
 */
public class UnitAnimation extends Animation {
	
	private final UnitSprite sprite;

	public UnitAnimation(UnitSprite sprite, int x, int y) {
		super(x, y);
		this.sprite = sprite;
	}
	
	public UnitSprite getSprite() {
		return sprite;
	}

	@Override
	public void update() {
	}
	
}
