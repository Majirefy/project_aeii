package com.toyknight.aeii.core.event;

import com.toyknight.aeii.core.Game;
import com.toyknight.aeii.core.GameListener;
import com.toyknight.aeii.core.animation.AnimationDispatcher;
import com.toyknight.aeii.core.unit.Unit;

/**
 *
 * @author toyknight
 */
public class UnitHpChangeEvent implements GameEvent {

	private final Game game;
	private final Unit unit;
	private final int change;

	public UnitHpChangeEvent(Game game, Unit unit, int change) {
		this.game = game;
		this.unit = unit;
		this.change = change;
	}

	protected Game getGame() {
		return game;
	}

	@Override
	public void execute(GameListener listener, AnimationDispatcher dispatcher) {
		int actual_change = validateHpChange(unit, change);
		int changed_hp = unit.getCurrentHp() + actual_change;
		unit.setCurrentHp(changed_hp);
		dispatcher.onUnitHpChanged(unit, actual_change);
		if (unit.getCurrentHp() <= 0) {
			new UnitDestroyEvent(getGame(), unit).execute(null, dispatcher);
		}
	}
	
	private int validateHpChange(Unit unit, int change) {
		if(unit.getCurrentHp() + change <= 0) {
			return -unit.getCurrentHp();
		}
		if(unit.getCurrentHp() + change >= unit.getMaxHp()) {
			return unit.getMaxHp() - unit.getCurrentHp();
		}
		return change;
	}

}
