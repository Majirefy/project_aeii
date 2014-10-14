
package com.toyknight.aeii.core.event;

import com.toyknight.aeii.core.Game;
import com.toyknight.aeii.core.GameListener;
import com.toyknight.aeii.core.animation.AnimationDispatcher;
import com.toyknight.aeii.core.unit.Unit;

/**
 *
 * @author toyknight
 */
public class TileDestroyEvent implements GameEvent {
	
	private final Game game;
	private final int x;
	private final int y;
	
	public TileDestroyEvent(Game game, int x, int y) {
		this.game = game;
		this.x = x;
		this.y = y;
	}
	
	protected Game getGame() {
		return game;
	}

	@Override
	public void execute(GameListener listener, AnimationDispatcher dispatcher) {
		int tile_index = getGame().getMap().getTileIndex(x, y);
		dispatcher.onTileDestroyed(tile_index, x, y);
		short destroyed_tile_index = 
				getGame().getMap().getTile(x, y).getDestroyedTileIndex();
		getGame().getMap().setTile(destroyed_tile_index, x, y);
	}
	
}
