package com.toyknight.aeii.core.event;

import com.toyknight.aeii.core.Game;
import com.toyknight.aeii.core.GameListener;
import com.toyknight.aeii.core.animation.AnimationDispatcher;
import com.toyknight.aeii.core.map.Tile;
import com.toyknight.aeii.core.unit.Unit;

/**
 *
 * @author toyknight
 */
public class RepairEvent implements GameEvent {

	private final Game game;
	private final Unit repairer;
	private final int x;
	private final int y;

	public RepairEvent(Game game, Unit repairer, int x, int y) {
		this.game = game;
		this.repairer = repairer;
		this.x = x;
		this.y = y;
	}
	
	protected Game getGame() {
		return game;
	}
	
	@Override
	public boolean canExecute() {
		return getGame().getMap().getTile(x, y).isRepairable();
	}

	@Override
	public void execute(GameListener listener, AnimationDispatcher dispatcher) {
		Tile tile = getGame().getMap().getTile(x, y);
		getGame().changeTile(tile.getRepairedTileIndex(), x, y);
		getGame().standbyUnit(repairer.getX(), repairer.getY());
		dispatcher.onRepair();
	}

}
