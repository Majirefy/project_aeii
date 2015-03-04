
package com.toyknight.aeii.core.event;

import com.toyknight.aeii.core.Game;
import com.toyknight.aeii.core.GameListener;
import com.toyknight.aeii.core.SkirmishGame;
import com.toyknight.aeii.core.map.Tile;
import com.toyknight.aeii.core.unit.Unit;

/**
 *
 * @author toyknight
 */
public class OccupyEvent implements GameEvent {

	private final Game game;
	private final Unit conqueror;
	private final int x;
	private final int y;

	public OccupyEvent(Game game, Unit conqueror, int x, int y) {
		this.game = game;
		this.conqueror = conqueror;
		this.x = x;
		this.y = y;
	}
	
	protected Game getGame() {
		return game;
	}
	
	@Override
	public boolean canExecute() {
		return getGame().getMap().getTile(x, y).isCapturable();
	}

	@Override
	public void execute(GameListener listener) {
		Tile tile = getGame().getMap().getTile(x, y);
		getGame().changeTile(tile.getCapturedTileIndex(conqueror.getTeam()), x, y);
		getGame().standbyUnit(conqueror.getX(), conqueror.getY());
        listener.onOccupy();
		if(getGame() instanceof SkirmishGame) {
			((SkirmishGame)getGame()).onOccupy(x, y);
		}
	}
	
}
