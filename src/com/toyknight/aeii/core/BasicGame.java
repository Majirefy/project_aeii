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
	private int current_team;
	private final Player[] player_list;
	private GameListener game_listener;

	public BasicGame(Map map) {
		this.map = map;
		player_list = new Player[4];
	}

	public void init() {
		current_team = -1;
		for (int team = 0; team < player_list.length; team++) {
			if (player_list[team] != null && current_team == -1) {
				current_team = team;
				break;
			} else {

			}
		}
	}

	public boolean isLocalPlayer() {
		return getCurrentPlayer() instanceof LocalPlayer;
	}

	public Player getCurrentPlayer() {
		return player_list[current_team];
	}

	public int getCurrentTeam() {
		return current_team;
	}

	public void setPlayer(int index, Player player) {
		player_list[index] = player;
	}

	public Player getPlayer(int index) {
		return player_list[index];
	}

	public void setGameListener(GameListener listener) {
		this.game_listener = listener;
	}

	@Override
	public void doAttack(int unit_x, int unit_y, int target_x, int target_y) {
		Unit attacker = getMap().getUnit(unit_x, unit_y);
		Unit defender = getMap().getUnit(target_x, target_y);
		if (attacker != null && defender != null) {
			game_listener.onUnitAttack(attacker, defender, 0);
			game_listener.onUnitStandby(attacker);
		}
	}

	@Override
	public void standbyUnit(int unit_x, int unit_y) {
		Unit unit = getMap().getUnit(unit_x, unit_y);
		if (unit != null) {
			unit.setStandby(true);
		}
	}

	@Override
	public void moveUnit(int unit_x, int unit_y, int dest_x, int dest_y) {
		Unit unit = getMap().getUnit(unit_x, unit_y);
		if (unit != null) {
			setUnitPosition(unit, dest_x, dest_y);
			game_listener.onUnitMove(unit, unit_x, unit_y, dest_x, dest_y);
		}
	}

	protected void setUnitPosition(Unit unit, int x, int y) {
		getMap().removeUnit(unit.getX(), unit.getY());
		unit.setX(x);
		unit.setY(y);
		getMap().addUnit(unit);
	}

	public Map getMap() {
		return map;
	}

	public void startTurn() {

	}

	@Override
	public void endTurn() {

	}

}
