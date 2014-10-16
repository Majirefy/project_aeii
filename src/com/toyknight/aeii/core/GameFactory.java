package com.toyknight.aeii.core;

import com.toyknight.aeii.core.map.Map;
import com.toyknight.aeii.core.player.Player;

/**
 *
 * @author toyknight
 */
public class GameFactory {

	private final Map map;

	public GameFactory(Map map) {
		this.map = map;
	}

	public Game createBasicGame(Player[] players, int max_population) {
		Game game = new Game(map, players, max_population);
		game.init();
		return game;
	}

}
