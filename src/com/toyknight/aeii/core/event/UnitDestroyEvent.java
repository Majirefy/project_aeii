
package com.toyknight.aeii.core.event;

import com.toyknight.aeii.core.Game;
import com.toyknight.aeii.core.GameListener;
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
	public void execute(GameListener listener) {
		getGame().getMap().removeUnit(unit.getX(), unit.getY());
		getGame().updatePopulation(getGame().getCurrentTeam());
		listener.onUnitDestroyed(unit);
		getGame().getMap().addTomb(unit.getX(), unit.getY());
	}
	
}
