
package com.toyknight.aeii.core.event;

import com.toyknight.aeii.core.GameListener;
import com.toyknight.aeii.core.unit.Unit;

/**
 *
 * @author toyknight
 */
public class UnitHpChangeEvent implements GameEvent {
	
	private final Unit unit;
	private final int change;
	
	public UnitHpChangeEvent(Unit unit, int change) {
		this.unit = unit;
		this.change = change;
	}

	@Override
	public void execute(GameListener listener) {
		int new_hp = unit.getCurrentHp() + change;
		unit.setCurrentHp(new_hp);
		listener.onUnitHpChanged(unit, change);
	}
	
}
