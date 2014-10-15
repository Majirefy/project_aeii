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
		int new_hp = unit.getCurrentHp() + change;
		unit.setCurrentHp(new_hp);
		dispatcher.onUnitHpChanged(unit, change);
		if (unit.getCurrentHp() <= 0) {
			getGame().destoryUnit(unit);
		}
	}

}
