package com.toyknight.aeii.core.creator;

import com.toyknight.aeii.core.Game;
import com.toyknight.aeii.core.GameFactory;
import com.toyknight.aeii.core.SuffixFileFilter;
import com.toyknight.aeii.core.map.Map;
import com.toyknight.aeii.core.player.LocalPlayer;
import com.toyknight.aeii.core.player.Player;

/**
 *
 * @author toyknight
 */
public class SkirmishGameCreator implements GameCreator {

	private final SuffixFileFilter map_file_filter = new SuffixFileFilter("aem");

	private GameCreatorListener listener;

	private final Player[] players = new Player[4];
	private int start_gold = 1000;
	private int max_population = 10;

	private Map map;

	private void updatePlayers() {
		if (map != null) {
			for (int team = 0; team < 4; team++) {
				boolean access = map.getTeamAccess(team);
				if (access == true && players[team] == null) {
					players[team] = new LocalPlayer();
					players[team].setAlliance(team);
				}
				if (access == false) {
					players[team] = null;
				}
			}
		} else {
			for (int team = 0; team < 4; team++) {
				players[team] = null;
			}
		}
	}

	@Override
	public void setGameCreatorListener(GameCreatorListener listener) {
		this.listener = listener;
	}

	@Override
	public boolean canChangeProperty() {
		return true;
	}
	
	@Override
	public void setMap(Map map) {
		this.map = map;
		updatePlayers();
		listener.onMapChanged(map);
	}

	@Override
	public void setMaxPopulation(int population) {
	}

	@Override
	public void setStartGold(int gold) {
	}

	@Override
	public boolean canCreate() {
		return map != null;
	}

	@Override
	public Game create() {
		for (Player player : players) {
			if (player != null) {
				player.setGold(start_gold);
			}
		}
		GameFactory factory = new GameFactory(map);
		return factory.createSkirmishGame(players, max_population);
	}

}
