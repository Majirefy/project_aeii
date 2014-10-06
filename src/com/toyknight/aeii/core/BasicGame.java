package com.toyknight.aeii.core;

import com.toyknight.aeii.core.map.Map;
import com.toyknight.aeii.core.player.LocalPlayer;
import com.toyknight.aeii.core.player.Player;
import com.toyknight.aeii.core.unit.Unit;
import com.toyknight.aeii.core.unit.UnitFactory;
import com.toyknight.aeii.core.unit.UnitToolkit;
import java.util.Collection;

/**
 *
 * @author toyknight
 */
public class BasicGame implements OperationListener {

	private final Map map;
	private int current_team;
	private final Player[] player_list;
	private GameListener game_listener;

	private final int max_population;

	public BasicGame(Map map, Player[] players, int max_population) {
		this.map = map;
		player_list = new Player[4];
		for (int team = 0; team < 4; team++) {
			if (team < players.length) {
				player_list[team] = players[team];
			} else {
				break;
			}
		}
		this.max_population = max_population;
	}

	public void init() {
		current_team = -1;
		for (int team = 0; team < player_list.length; team++) {
			if (player_list[team] != null) {
				if (current_team == -1) {
					current_team = team;
				}
				updatePopulation(team);
			} else {
				//remove all elements on the map that is related to this team
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

	public Player getPlayer(int team ){
		return player_list[team];
	}
	
	public int getMaxPopulation() {
		return max_population;
	}

	public void setGameListener(GameListener listener) {
		this.game_listener = listener;
	}

	@Override
	public void doAttack(int unit_x, int unit_y, int target_x, int target_y) {
		Unit attacker = getMap().getUnit(unit_x, unit_y);
		if (attacker != null && UnitToolkit.isWithinRange(attacker, target_x, target_y)) {
			Unit defender = getMap().getUnit(target_x, target_y);
			if (defender != null) {
				doAttack(attacker, defender);
			} else {

			}
		}
	}

	protected void doAttack(Unit attacker, Unit defender) {
		int attack_damage = UnitToolkit.getDamage(attacker, defender, getMap());
		doDamage(attacker, defender, attack_damage);
		if (defender.getCurrentHp() > 0
				&& UnitToolkit.isWithinRange(defender, attacker.getX(), attacker.getY())) {
			int counter_damage = UnitToolkit.getDamage(defender, attacker, getMap());
			doDamage(defender, attacker, counter_damage);
		}
		game_listener.onUnitAttackFinished(attacker, defender);
	}

	protected void doDamage(Unit attacker, Unit defender, int damage) {
		if (defender.getCurrentHp() > damage) {
			defender.setCurrentHp(defender.getCurrentHp() - damage);
			game_listener.onUnitAttack(attacker, defender, damage);
			//deal with buff issues
		} else {
			damage = defender.getCurrentHp();
			defender.setCurrentHp(0);
			game_listener.onUnitAttack(attacker, defender, damage);
			getMap().removeUnit(defender.getX(), defender.getY());
			updatePopulation(getCurrentTeam());
			game_listener.onUnitDestroyed(defender);
			getMap().addTomb(defender.getX(), defender.getY());
		}
	}

	@Override
	public void standbyUnit(int unit_x, int unit_y) {
		Unit unit = getMap().getUnit(unit_x, unit_y);
		if (unit != null) {
			standbyUnit(unit);
		}
	}

	protected void standbyUnit(Unit unit) {
		unit.setStandby(true);
	}

	@Override
	public void buyUnit(int index, int x, int y) {
		int current_cold = getCurrentPlayer().getGold();
		int unit_price = UnitFactory.getUnitPrice(index);
		if(current_cold >= unit_price) {
			getCurrentPlayer().setGold(current_cold - unit_price);
			addUnit(index, x, y);
		}
	}

	@Override
	public void addUnit(int index, int x, int y) {
		Unit unit = UnitFactory.createUnit(index);
		unit.setX(x);
		unit.setY(y);
		getMap().addUnit(unit);
		updatePopulation(getCurrentTeam());
	}

	@Override
	public void moveUnit(int unit_x, int unit_y, int dest_x, int dest_y) {
		Unit unit = getMap().getUnit(unit_x, unit_y);
		if (unit != null) {
			moveUnit(unit, dest_x, dest_y);
		}
	}

	protected void moveUnit(Unit unit, int dest_x, int dest_y) {
		if (unit != null && getMap().getUnit(dest_x, dest_y) == null) {
			int start_x = unit.getX();
			int start_y = unit.getY();
			getMap().moveUnit(unit, dest_x, dest_y);
			game_listener.onUnitMove(unit, start_x, start_y, dest_x, dest_y);
		}
	}
	
	protected void updatePopulation(int team) {
		getPlayer(team).setPopulation(getMap().getUnitCount(team));
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
