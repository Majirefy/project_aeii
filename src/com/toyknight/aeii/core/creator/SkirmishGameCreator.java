package com.toyknight.aeii.core.creator;

import com.toyknight.aeii.core.Game;
import com.toyknight.aeii.core.GameFactory;
import com.toyknight.aeii.core.SuffixFileFilter;
import com.toyknight.aeii.core.map.Map;
import com.toyknight.aeii.core.map.MapFactory;
import com.toyknight.aeii.core.player.LocalPlayer;
import com.toyknight.aeii.core.player.Player;
import java.io.File;
import java.io.IOException;

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

	private Map selected_map;
	private File[] map_list;
	private String[] map_name_list;
	private int selected_map_index;

	private void updatePlayers() {
		if (selected_map != null) {
			for (int team = 0; team < 4; team++) {
				boolean access = selected_map.getTeamAccess(team);
				if(access == true && players[team] == null) {
					players[team] = new LocalPlayer();
					players[team].setAlliance(team);
				}
				if(access == false) {
					players[team] = null;
				}
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
	public String[] getMapList() {
		if (map_list == null) {
			refreshMapList();
		} else {
			return map_name_list;
		}
		return map_name_list;
	}

	@Override
	public void refreshMapList() {
		File map_dir = new File("map/");
		map_list = map_dir.listFiles(map_file_filter);
		map_name_list = new String[map_list.length];
		for (int i = 0; i < map_list.length; i++) {
			map_name_list[i] = map_list[i].getName();
		}
		selected_map_index = -1;
	}

	@Override
	public void changeMap(int index) {
		File map_file = map_list[index];
		try {
			this.selected_map = MapFactory.createMap(map_file);
			this.selected_map_index = index;
			updatePlayers();
			this.listener.onMapChanged(index);
		} catch (IOException ex) {
			this.listener.onMapChanged(selected_map_index);
		}
	}

	@Override
	public void setMaxPopulation(int population) {
	}

	@Override
	public void setStartGold(int gold) {
	}

	@Override
	public boolean canCreate() {
		return selected_map_index >= 0;
	}

	@Override
	public Game create() {
		for (Player player : players) {
			if (player != null) {
				player.setGold(start_gold);
			}
		}
		GameFactory factory = new GameFactory(selected_map);
		return factory.createSkirmishGame(players, max_population);
	}

}
