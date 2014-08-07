package com.toyknight.aeii.core;

import com.toyknight.aeii.core.map.Map;
import com.toyknight.aeii.core.player.LocalPlayer;
import com.toyknight.aeii.core.player.Player;
import com.toyknight.aeii.core.unit.Unit;

/**
 *
 * @author toyknight
 */
public class BasicGame implements OperationListener {

	private final Map map;
	private final Player[] player_list;
	private int current_team;
	private GameListener game_listener;

	public BasicGame(Map map) {
		this.map = map;
		player_list = new Player[4];
	}

	public void init() {
		for (int team = 0; team < player_list.length; team++) {
			if(player_list[team] != null) {
				current_team = team;
				break;
			}
		}
	}
	
	public boolean isLocalPlayer() {
		return getCurrentPlayer() instanceof LocalPlayer;
	}

	public final Player getCurrentPlayer() {
		return player_list[current_team];
	}
	
	public final int getCurrentTeam() {
		return current_team;
	}

	public final void setPlayer(int index, Player player) {
		player_list[index] = player;
	}

	public final Player getPlayer(int index) {
		return player_list[index];
	}
	
	public final void setGameListener(GameListener listener) {
		this.game_listener = listener;
	}
	
	public int getMapWidth() {
		return map.getMapWidth();
	}
	
	public int getMapHeight() {
		return map.getMapHeight();
	}
	
	public int getTileIndex(int x, int y) {
		return map.getTileIndex(x, y);
	}
	
	public int getUnitCount() {
		return map.getUnitList().size();
	}
	
	public Unit getUnit(int index) {
		return map.getUnitList().get(index);
	}
	
	public Unit getUnit(int x, int y) {
		for(Unit unit: map.getUnitList()) {
			if(unit.getX() == x && unit.getY() == y) {
				return unit;
			}
		}
		return null;
	}

	public void endTurn() {
		
	}

}
