
package com.toyknight.aeii.gui.animation;

import com.toyknight.aeii.core.unit.Unit;

/**
 *
 * @author toyknight
 */
public class UnitAnimation extends Animation {
	
	private final Unit unit;

	public UnitAnimation(Unit unit, int x, int y) {
		super(x, y);
		this.unit = unit;
	}
	
	public Unit getUnit() {
		return unit;
	}
	
}
