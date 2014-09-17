package com.toyknight.aeii.gui.animation;

import com.toyknight.aeii.core.unit.Unit;

/**
 *
 * @author toyknight
 */
public class UnitAnimation extends SwingAnimation {

	private final Unit unit;

	public UnitAnimation(Unit unit, int x, int y) {
		super(x, y);
		if (unit != null) {
			this.unit = new Unit(unit);
		} else {
			this.unit = null;
		}
	}

	public Unit getUnit() {
		return unit;
	}

}
