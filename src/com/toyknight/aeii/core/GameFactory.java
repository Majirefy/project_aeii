
package com.toyknight.aeii.core;

import com.toyknight.aeii.core.map.Map;
import com.toyknight.aeii.core.player.LocalPlayer;
import com.toyknight.aeii.core.player.Player;

/**
 *
 * @author toyknight
 */
public class GameFactory {
	
	private final Map map;
	private final BasicGame basic_game;
	
	public GameFactory(Map map) {
		this.map = map;
		this.basic_game = new BasicGame(map);
		this.basic_game.setPlayer(0, new LocalPlayer(basic_game));
	}
	
	public void setPlayers(Player[] players) {
		
	}
	
	public BasicGame createBasicGame() {
		basic_game.init();
		return basic_game;
	}
	
}
