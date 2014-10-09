package com.toyknight.aeii.core;

import com.toyknight.aeii.core.map.Map;
import com.toyknight.aeii.core.map.Tile;
import com.toyknight.aeii.core.player.LocalPlayer;
import com.toyknight.aeii.core.player.Player;
import com.toyknight.aeii.core.unit.Ability;
import com.toyknight.aeii.core.unit.Buff;
import com.toyknight.aeii.core.unit.Unit;
import com.toyknight.aeii.core.unit.UnitFactory;
import com.toyknight.aeii.core.unit.UnitToolkit;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author toyknight
 */
public class Game implements OperationListener {

	private final Map map;
	private int current_team;
	private final Player[] player_list;
	private GameListener game_listener;

	private int turn;

	private final int max_population;

	public Game(Map map, Player[] players, int max_population) {
		this.map = map;
		player_list = new Player[4];
		for (int team = 0; team < 4; team++) {
			if (team < players.length) {
				player_list[team] = players[team];
			} else {
				break;
			}
		}
		this.turn = 0;
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

	public Player getPlayer(int team) {
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
				if (attacker.getAbilities().contains(Ability.DESTROYER)
						&& getMap().getTile(target_x, target_y).isDestroyable()) {
					doDestroy(attacker, target_x, target_y);
				}
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
		game_listener.onUnitActionFinished(attacker);
	}

	protected void doDestroy(Unit destroyer, int x, int y) {
		int tile_index = getMap().getTileIndex(x, y);
		game_listener.onTileDestroyed(tile_index, x, y);
		getMap().setTile(getMap().getTile(x, y).getDestroyedTileIndex(), x, y);
		game_listener.onUnitActionFinished(destroyer);
	}

	protected void doDamage(Unit attacker, Unit defender, int damage) {
		if (defender.getCurrentHp() > damage) {
			defender.setCurrentHp(defender.getCurrentHp() - damage);
			game_listener.onUnitAttack(attacker, defender, damage);
			//deal with buff issues
			if (attacker.getAbilities().contains(Ability.POISONER)) {
				defender.attachBuff(new Buff(Buff.POISONED, 2));
			}
		} else {
			damage = defender.getCurrentHp();
			defender.setCurrentHp(0);
			game_listener.onUnitAttack(attacker, defender, damage);
			destoryUnit(defender);
		}
	}

	protected void destoryUnit(Unit unit) {
		getMap().removeUnit(unit.getX(), unit.getY());
		updatePopulation(getCurrentTeam());
		game_listener.onUnitDestroyed(unit);
		getMap().addTomb(unit.getX(), unit.getY());
	}

	@Override
	public void doSummon(int summoner_x, int summoner_y, int target_x, int target_y) {
		Unit summoner = getMap().getUnit(summoner_x, summoner_y);
		if (summoner != null
				&& UnitToolkit.isWithinRange(summoner, target_x, target_y)
				&& getMap().isTomb(target_x, target_y)) {
			doSummon(summoner, target_x, target_y);
		}
	}

	protected void doSummon(Unit summoner, int target_x, int target_y) {
		getMap().removeTomb(target_x, target_y);
		addUnit(10, getCurrentTeam(), target_x, target_y);
		getMap().getUnit(target_x, target_y).setStandby(true);
		game_listener.onSummon(summoner, target_x, target_y);
		game_listener.onUnitActionFinished(summoner);
	}

	@Override
	public void doOccupy(int conqueror_x, int conqueror_y, int x, int y) {
		Unit conqueror = getMap().getUnit(conqueror_x, conqueror_y);
		if (canOccupy(conqueror, x, y)) {
			doOccupy(x, y);
			game_listener.onUnitActionFinished(conqueror);
		}
	}

	@Override
	public void doOccupy(int x, int y) {
		Tile tile = getMap().getTile(x, y);
		if (tile.isCapturable()) {
			changeTile(tile.getCapturedTileIndex(getCurrentTeam()), x, y);
			game_listener.onOccupy();
		}
	}

	@Override
	public void doRepair(int repairer_x, int repairer_y, int x, int y) {
		Unit repairer = getMap().getUnit(x, y);
		if (canRepair(repairer, x, y)) {
			doRepair(x, y);
			game_listener.onUnitActionFinished(repairer);
		}
	}

	@Override
	public void doRepair(int x, int y) {
		Tile tile = getMap().getTile(x, y);
		if (tile.isRepairable()) {
			changeTile(tile.getRepairedTileIndex(), x, y);
			game_listener.onRepair();

		}
	}

	protected void changeTile(short index, int x, int y) {
		getMap().setTile(index, x, y);
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
		if (current_cold >= unit_price) {
			getCurrentPlayer().setGold(current_cold - unit_price);
			addUnit(index, getCurrentTeam(), x, y);
		}
	}

	@Override
	public void addUnit(int index, int team, int x, int y) {
		Unit unit = UnitFactory.createUnit(index, team);
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

	private void poisonUnit(Unit unit) {
		int current_hp = unit.getCurrentHp();
		int poison_damage = current_hp > 10 ? 10 : current_hp;
		unit.setCurrentHp(current_hp - poison_damage);
		game_listener.onUnitHpChanged(unit, -poison_damage);
		if (unit.getCurrentHp() <= 0) {
			destoryUnit(unit);
		}
	}

	private void checkTerrainHeal(Unit unit) {
		int heal = 0;
		Tile tile = getMap().getTile(unit.getX(), unit.getY());
		if (unit.getCurrentHp() < unit.getMaxHp()) {
			if (tile.getTeam() == -1) {
				heal = tile.getHpRecovery();
			} else {
				if(tile.getTeam() == getCurrentTeam()) {
					heal = tile.getHpRecovery();
				}
			}
			if(unit.getMaxHp() - unit.getCurrentHp() <= heal) {
				heal = unit.getMaxHp() - unit.getCurrentHp();
			}
		}
		if(heal > 0) {
			int new_hp = unit.getCurrentHp() + heal;
			unit.setCurrentHp(new_hp);
			game_listener.onUnitHpChanged(unit, heal);
		}
	}

	public boolean canOccupy(Unit conqueror, int x, int y) {
		if (conqueror == null) {
			return false;
		}
		if (conqueror.getTeam() != getCurrentTeam()) {
			return false;
		}
		Tile tile = getMap().getTile(x, y);
		if (tile.getTeam() != getCurrentTeam()) {
			return (tile.isCastle() && conqueror.getAbilities().contains(Ability.COMMANDER))
					|| (tile.isVillage() && conqueror.getAbilities().contains(Ability.CONQUEROR));
		} else {
			return false;
		}
	}

	public boolean canRepair(Unit repairer, int x, int y) {
		if (repairer == null) {
			return false;
		}
		if (repairer.getTeam() != getCurrentTeam()) {
			return false;
		}
		Tile tile = getMap().getTile(x, y);
		return repairer.getAbilities().contains(Ability.REPAIRER) && tile.isRepairable();
	}

	protected void moveUnit(Unit unit, int dest_x, int dest_y) {
		if (unit != null) {
			int start_x = unit.getX();
			int start_y = unit.getY();
			if (getMap().canMove(dest_x, dest_y)) {
				getMap().moveUnit(unit, dest_x, dest_y);
				game_listener.onUnitMove(unit, start_x, start_y, dest_x, dest_y);
			}
		}
	}

	protected void updatePopulation(int team) {
		getPlayer(team).setPopulation(getMap().getUnitCount(team));
	}

	protected int getIncome(int team) {
		int income = 0;
		for (int x = 0; x < getMap().getWidth(); x++) {
			for (int y = 0; y < getMap().getHeight(); y++) {
				Tile tile = getMap().getTile(x, y);
				if (tile.getTeam() == team) {
					if (tile.isVillage()) {
						income += 100;
					}
					if (tile.isCastle()) {
						income += 50;
					}
				}
			}
		}
		return income;
	}

	public int getTurn() {
		return turn;
	}

	public Map getMap() {
		return map;
	}

	public void startTurn() {
		turn++;
		int income = getIncome(getCurrentTeam());
		getCurrentPlayer().addGold(income);
		if (isLocalPlayer()) {
			game_listener.onTurnStart(turn, income, getCurrentTeam());
		} else {
			game_listener.onTurnStart(turn, -1, getCurrentTeam());
		}

		Set<Point> position_set = new HashSet(getMap().getUnitPositionSet());
		for (Point position : position_set) {
			Unit unit = getMap().getUnit(position.x, position.y);
			if (unit.getTeam() == getCurrentTeam()) {
				checkTerrainHeal(unit);
				//deal with buff issues
				if (unit.hasBuff(Buff.POISONED)) {
					poisonUnit(unit);
				}
				//deal with ability issues
				if (unit.getCurrentHp() > 0) {
					unit.updateBuff();
				}
			}
		}
	}

	private void restoreUnit(Unit unit) {
		unit.setCurrentMovementPoint(unit.getMovementPoint());
		unit.setStandby(false);
	}

	@Override
	public void endTurn() {
		Collection<Unit> units = getMap().getUnitSet();
		for (Unit unit : units) {
			if (unit.getTeam() == getCurrentTeam()) {
				restoreUnit(unit);
			}
		}
		do {
			if (current_team < 3) {
				current_team++;
			} else {
				current_team = 0;
			}
		} while (getCurrentPlayer() == null);
		//deal with various issues
		startTurn();
	}

}
