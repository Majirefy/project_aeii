
package com.toyknight.aeii.core;

import com.toyknight.aeii.core.map.Map;
import com.toyknight.aeii.core.player.Player;

/**
 *
 * @author toyknight
 */
public class BasicGame {
	
	private final AnimationListener anime_listener;
	private final Map map;
	private final Player[] player_list;
	private int current_player_index;
	
	public BasicGame(AnimationListener anime_listener, Map map) {
		this.anime_listener = anime_listener;
		this.map = map;
		player_list = new Player[4];
	}
	
	public final Player getCurrentPlayer() {
		return player_list[current_player_index];
	}
	
	public final Player getPlayer(int index) {
		return player_list[index];
	}
	
	public final Map getMap() {
		return map;
	}
	
	public void endTurn() {
		
	}
	
}
