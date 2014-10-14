package com.toyknight.aeii.core;

import com.toyknight.aeii.core.animation.AnimationDispatcher;
import com.toyknight.aeii.core.event.BuffAttachEvent;
import com.toyknight.aeii.core.event.GameEvent;
import com.toyknight.aeii.core.event.OccupyEvent;
import com.toyknight.aeii.core.event.RepairEvent;
import com.toyknight.aeii.core.event.TileDestroyEvent;
import com.toyknight.aeii.core.event.UnitActionFinishEvent;
import com.toyknight.aeii.core.event.UnitAttackEvent;
import com.toyknight.aeii.core.event.UnitDestroyEvent;
import com.toyknight.aeii.core.event.UnitHpChangeEvent;
import com.toyknight.aeii.core.event.UnitMoveEvent;
import com.toyknight.aeii.core.event.UnitSummonEvent;
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
import java.util.LinkedList;
import java.util.Queue;
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
	private Displayable displayable;
	private AnimationDispatcher animation_dispatcher;
	private final Queue<GameEvent> event_queue;

	private int turn;

	private final Unit[] commanders;
	private final int max_population;
	private final int[] commander_price_delta;

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
		this.commander_price_delta = new int[4];
		this.commanders = new Unit[4];
		Set<Point> position_set = new HashSet(getMap().getUnitPositionSet());
		for (Point position : position_set) {
			Unit unit = getMap().getUnit(position.x, position.y);
			if (unit.isCommander()) {
				commanders[unit.getTeam()] = unit;
			}
		}
		for (int team = 0; team < 4; team++) {
			if (commanders[team] == null) {
				commanders[team] = UnitFactory.createUnit(9, team);
			}
		}
		this.event_queue = new LinkedList();
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

	protected void submitGameEvent(GameEvent e) {
		event_queue.add(e);
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

	public void setDisplayable(Displayable displayable) {
		this.displayable = displayable;
	}

	public void setAnimationDispatcher(AnimationDispatcher dispatcher) {
		this.animation_dispatcher = dispatcher;
	}

	@Override
	public void doAttack(int unit_x, int unit_y, int target_x, int target_y) {
		Unit attacker = getMap().getUnit(unit_x, unit_y);
		if (attacker != null && UnitToolkit.isWithinRange(attacker, target_x, target_y)) {
			Unit defender = getMap().getUnit(target_x, target_y);
			if (defender != null) {
				doAttack(attacker, defender);
			} else {
				if (attacker.hasAbility(Ability.DESTROYER)
						&& getMap().getTile(target_x, target_y).isDestroyable()) {
					doDestroy(attacker, target_x, target_y);
				}
			}
		}
	}

	protected void doAttack(Unit attacker, Unit defender) {
		int attack_damage = UnitToolkit.getDamage(attacker, defender, getMap());
		doDamage(attacker, defender, attack_damage);
		if (defender.getCurrentHp() > attack_damage) {
			if (UnitToolkit.canCounter(defender, attacker)) {
				Unit defender_tmp = UnitFactory.cloneUnit(defender);
				defender_tmp.setCurrentHp(defender.getCurrentHp() - attack_damage);
				int counter_damage = UnitToolkit.getDamage(defender_tmp, attacker, getMap());
				doDamage(defender, attacker, counter_damage);
				if (attacker.getCurrentHp() > counter_damage) {
					checkAttackBuff(defender, attacker);
					submitGameEvent(new UnitActionFinishEvent(attacker));
				} else {
					destoryUnit(attacker);
				}
			} else {
				submitGameEvent(new UnitActionFinishEvent(attacker));
			}
			checkAttackBuff(attacker, defender);
		} else {
			destoryUnit(defender);
			submitGameEvent(new UnitActionFinishEvent(attacker));
		}
	}
	
	protected void checkAttackBuff(Unit attacker, Unit defender) {
		if (attacker.hasAbility(Ability.POISONER)) {
			submitGameEvent(new BuffAttachEvent(defender, new Buff(Buff.POISONED, 2)));
		}
	}

	protected void doDestroy(Unit destroyer, int x, int y) {
		submitGameEvent(new TileDestroyEvent(this, x, y));
		submitGameEvent(new UnitActionFinishEvent(destroyer));
	}

	protected void doDamage(Unit attacker, Unit defender, int damage) {
		if (defender.getCurrentHp() > damage) {
			submitGameEvent(new UnitAttackEvent(attacker, defender, damage));
		} else {
			submitGameEvent(new UnitAttackEvent(attacker, defender, defender.getCurrentHp()));
		}
	}

	protected void destoryUnit(Unit unit) {
		submitGameEvent(new UnitDestroyEvent(this, unit));
		if (unit.isCommander()) {
			commander_price_delta[unit.getTeam()] += 500;
		}
	}

	@Override
	public void doSummon(int summoner_x, int summoner_y, int target_x, int target_y) {
		Unit summoner = getMap().getUnit(summoner_x, summoner_y);
		if (summoner != null
				&& UnitToolkit.isWithinRange(summoner, target_x, target_y)
				&& getMap().isTomb(target_x, target_y)) {
			submitGameEvent(new UnitSummonEvent(this, summoner, target_x, target_y));
			submitGameEvent(new UnitActionFinishEvent(summoner));
		}
	}

	@Override
	public void doOccupy(int conqueror_x, int conqueror_y, int x, int y) {
		Unit conqueror = getMap().getUnit(conqueror_x, conqueror_y);
		if (canOccupy(conqueror, x, y)) {
			submitGameEvent(new OccupyEvent(this, conqueror, x, y));
			conqueror.setStandby(true);
		}
	}

	@Override
	public void doRepair(int repairer_x, int repairer_y, int x, int y) {
		Unit repairer = getMap().getUnit(x, y);
		if (canRepair(repairer, x, y)) {
			submitGameEvent(new RepairEvent(this, repairer, x, y));
		}
	}

	public void changeTile(short index, int x, int y) {
		getMap().setTile(index, x, y);
	}

	@Override
	public void standbyUnit(int unit_x, int unit_y) {
		Unit unit = getMap().getUnit(unit_x, unit_y);
		if (unit != null && getMap().canStandby(unit)) {
			standbyUnit(unit);
		}
	}

	protected void standbyUnit(Unit unit) {
		unit.setStandby(true);
	}

	public void restoreCommander(int team, int x, int y) {
		if (!isCommanderAlive(team)) {
			getMap().addUnit(commanders[team]);
			commanders[team].setCurrentHp(commanders[team].getMaxHp());
			restoreUnit(commanders[team]);
			updatePopulation(team);
		}
	}

	@Override
	public void buyUnit(int index, int x, int y) {
		int current_cold = getCurrentPlayer().getGold();
		int unit_price = UnitFactory.getSample(index).getPrice();
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
		if (unit != null && getMap().canMove(dest_x, dest_y)) {
			submitGameEvent(new UnitMoveEvent(this, unit, dest_x, dest_y));
		}
	}

	protected void poisonUnit(Unit unit) {
		int current_hp = unit.getCurrentHp();
		int poison_damage = current_hp > 10 ? 10 : current_hp;
		changeUnitHp(unit, -poison_damage);
		if (unit.getCurrentHp() <= poison_damage) {
			destoryUnit(unit);
		}
	}

	protected void checkTerrainHeal(Unit unit) {
		int heal = 0;
		Tile tile = getMap().getTile(unit.getX(), unit.getY());
		if (tile.getTeam() == -1) {
			heal += tile.getHpRecovery();
		} else {
			if (tile.getTeam() == getCurrentTeam()) {
				heal += tile.getHpRecovery();
			}
		}
		if (unit.hasAbility(Ability.SON_OF_THE_MOUNTAIN) && tile.getType() == Tile.TYPE_MOUNTAIN) {
			heal += 10;
		}
		if (unit.hasAbility(Ability.SON_OF_THE_FOREST) && tile.getType() == Tile.TYPE_FOREST) {
			heal += 10;
		}
		if (unit.hasAbility(Ability.SON_OF_THE_SEA) && tile.getType() == Tile.TYPE_WATER) {
			heal += 10;
		}
		healUnit(unit, heal);
	}

	protected void healAllys(Unit healer) {
		for (int dx = -1; dx <= 1; dx++) {
			for (int dy = -1; dy <= 1; dy++) {
				if (dx != 0 && dy != 0) {
					int x = healer.getX() + dx;
					int y = healer.getY() + dy;
					if (getMap().isWithinMap(x, y)) {
						Unit unit = getMap().getUnit(x, y);
						if (unit != null && unit.getTeam() == healer.getTeam()) {
							healUnit(unit, 10);
						}
					}
				}
			}
		}
	}

	protected void healUnit(Unit unit, int heal) {
		//validate heal
		if (unit.getMaxHp() - unit.getCurrentHp() <= heal) {
			heal = unit.getMaxHp() - unit.getCurrentHp();
		}
		if (heal > 0) {
			changeUnitHp(unit, heal);
		}
	}

	protected void changeUnitHp(Unit unit, int change) {
		submitGameEvent(new UnitHpChangeEvent(unit, change));
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
			return (tile.isCastle() && conqueror.hasAbility(Ability.COMMANDER))
					|| (tile.isVillage() && conqueror.hasAbility(Ability.CONQUEROR));
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
		return repairer.hasAbility(Ability.REPAIRER) && tile.isRepairable();
	}

	public int getCommanderPrice(int team) {
		if (commander_price_delta[team] > 0) {
			return UnitFactory.getSample(9).getPrice() + commander_price_delta[team];
		} else {
			return -1;
		}
	}

	public Unit getCommander(int team) {
		return commanders[team];
	}

	public boolean isCommanderAlive(int team) {
		Set<Point> position_set = new HashSet(getMap().getUnitPositionSet());
		for (Point position : position_set) {
			Unit unit = getMap().getUnit(position.x, position.y);
			if (unit.getTeam() == team && unit.isCommander()) {
				return true;
			}
		}
		return false;
	}

	public void updatePopulation(int team) {
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

	public Point getTurnStartPosition(int team) {
		Set<Point> position_set = new HashSet(getMap().getUnitPositionSet());
		Unit first_unit = null;
		for (Point position : position_set) {
			Unit unit = getMap().getUnit(position.x, position.y);
			if (first_unit == null) {
				first_unit = unit;
			}
			if (unit.getTeam() == team && unit.isCommander()) {
				return getMap().getPosition(unit.getX(), unit.getY());
			}
		}
		if (first_unit != null) {
			return getMap().getPosition(first_unit.getX(), first_unit.getY());
		}
		Point first_tile_position = null;
		for (int x = 0; x < getMap().getWidth(); x++) {
			for (int y = 0; y < getMap().getHeight(); y++) {
				if (getMap().getTile(x, y).getTeam() == team) {
					if (first_tile_position == null) {
						first_tile_position = getMap().getPosition(x, y);
					}
					if (getMap().getTile(x, y).isCastle()) {
						return getMap().getPosition(x, y);
					}
				}
			}
		}
		if (first_tile_position != null) {
			return first_tile_position;
		}
		return getMap().getPosition(0, 0);
	}

	public int getTurn() {
		return turn;
	}

	public final Map getMap() {
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
				if (unit.hasAbility(Ability.HEALING_AURA)) {
					healAllys(unit);
				}
				if (unit.getCurrentHp() > 0) {
					unit.updateBuff();
				}
			}
		}
		Point team_start_position = getTurnStartPosition(getCurrentTeam());
		displayable.locateViewport(team_start_position.x, team_start_position.y);
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

	public void dispatchGameEvent() {
		if (!animation_dispatcher.isAnimating()) {
			GameEvent event = event_queue.poll();
			if (event != null) {
				event.execute(game_listener);
			}
		}
	}

}
