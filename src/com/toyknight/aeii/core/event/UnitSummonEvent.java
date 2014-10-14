

package com.toyknight.aeii.core.event;

import com.toyknight.aeii.core.Game;
import com.toyknight.aeii.core.GameListener;
import com.toyknight.aeii.core.animation.AnimationDispatcher;
import com.toyknight.aeii.core.unit.Unit;

/**
 *
 * @author toyknight
 */
public class UnitSummonEvent implements GameEvent {
	
	private final Game game;
	private final Unit summoner;
	private final int target_x;
	private final int target_y;
	
	public UnitSummonEvent(Game game, Unit summoner, int target_x, int target_y) {
		this.game = game;
		this.summoner = summoner;
		this.target_x = target_x;
		this.target_y = target_y;
	}
	
	protected Game getGame() {
		return game;
	}

	@Override
	public void execute(GameListener listener, AnimationDispatcher dispatcher) {
		getGame().getMap().removeTomb(target_x, target_y);
		getGame().addUnit(10, getGame().getCurrentTeam(), target_x, target_y);
		getGame().getMap().getUnit(target_x, target_y).setStandby(true);
		dispatcher.onSummon(summoner, target_x, target_y);
	}
	
}
