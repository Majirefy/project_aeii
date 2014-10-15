package com.toyknight.aeii.core.event;

import com.toyknight.aeii.core.Game;
import com.toyknight.aeii.core.GameListener;
import com.toyknight.aeii.core.animation.AnimationDispatcher;
import com.toyknight.aeii.core.unit.Unit;

/**
 *
 * @author toyknight
 */
public class UnitMoveEvent implements GameEvent {

	private final Game game;
	private final Unit unit;
	private final int dest_x;
	private final int dest_y;

	public UnitMoveEvent(Game game, Unit unit, int dest_x, int dest_y) {
		this.game = game;
		this.unit = unit;
		this.dest_x = dest_x;
		this.dest_y = dest_y;
	}

	protected Game getGame() {
		return game;
	}
	
	@Override
	public boolean canExecute() {
		return getGame().getMap().canMove(dest_x, dest_y);
	}

	@Override
	public void execute(GameListener listener, AnimationDispatcher dispatcher) {
		int start_x = unit.getX();
		int start_y = unit.getY();
		getGame().getMap().moveUnit(unit, dest_x, dest_y);
		dispatcher.onUnitMove(unit, start_x, start_y, dest_x, dest_y);
	}

}
