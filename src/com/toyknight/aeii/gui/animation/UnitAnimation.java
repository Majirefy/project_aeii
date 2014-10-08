package com.toyknight.aeii.gui.animation;

import com.toyknight.aeii.core.unit.Unit;
import com.toyknight.aeii.core.unit.UnitFactory;

/**
 *
 * @author toyknight
 */
public class UnitAnimation extends CanvasAnimation {

	private final Unit unit;

	public UnitAnimation(Unit unit, int x, int y) {
		super(x, y);
		if (unit != null) {
			this.unit = UnitFactory.cloneUnit(unit);
		} else {
			this.unit = null;
		}
	}

	public Unit getUnit() {
		return unit;
	}

}
