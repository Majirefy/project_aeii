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

	public GameFactory(Map map) {
		this.map = map;
	}

	public BasicGame createBasicGame(Player[] players, int start_gold, int max_population) {
		for (Player player : players) {
			player.setGold(start_gold);
		}
		BasicGame game = new BasicGame(map, players, max_population);
		game.init();
		return game;
	}

}
