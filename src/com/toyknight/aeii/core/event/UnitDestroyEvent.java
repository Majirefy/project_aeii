package com.toyknight.aeii.core.event;

import com.toyknight.aeii.core.Game;
import com.toyknight.aeii.core.GameListener;
import com.toyknight.aeii.core.animation.AnimationDispatcher;
import com.toyknight.aeii.core.unit.Unit;

/**
 *
 * @author toyknight
 */
public class UnitDestroyEvent implements GameEvent {

	private final Game game;
	private final Unit unit;

	public UnitDestroyEvent(Game game, Unit unit) {
		this.game = game;
		this.unit = unit;
	}

	protected Game getGame() {
		return game;
	}

	@Override
	public boolean canExecute() {
		return true;
	}

	@Override
	public void execute(GameListener listener, AnimationDispatcher dispatcher) {
		getGame().getMap().removeUnit(unit.getX(), unit.getY());
		getGame().updatePopulation(unit.getTeam());
		dispatcher.onUnitDestroyed(unit);
		if (unit.getIndex() != 11) {
			getGame().getMap().addTomb(unit.getX(), unit.getY());
		}
		if (unit.isCommander()) {
			getGame().changeCommanderPriceDelta(unit.getTeam(), 500);
		}
	}

}
