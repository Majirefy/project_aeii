
package com.toyknight.aeii.core;

import com.toyknight.aeii.core.map.Map;
import com.toyknight.aeii.core.player.Player;
import com.toyknight.aeii.core.unit.Unit;

/**
 *
 * @author toyknight
 */
public class SkirmishGame extends Game {
	
	private boolean is_game_over = false;

	public SkirmishGame(Map map, Player[] players, int max_population) {
		super(map, players, max_population);
	}
	
	protected void setGameOver(boolean b) {
		this.is_game_over = b;
	}
	
	public boolean isGameOver() {
		return is_game_over;
	}
	
	public void onUnitMoved(Unit unit, int dest_x, int dest_y) {
		
	}
	
	public void onUnitDestroyed(Unit unit) {
		
	}
	
	public void onOccupy(int x, int y) {
		
	}
	
}
