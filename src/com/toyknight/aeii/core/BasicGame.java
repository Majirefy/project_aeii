package com.toyknight.aeii.core;

import com.toyknight.aeii.core.map.Map;
import com.toyknight.aeii.core.player.LocalPlayer;
import com.toyknight.aeii.core.player.Player;
import com.toyknight.aeii.core.unit.Unit;
import com.toyknight.aeii.core.unit.UnitToolkit;
import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author toyknight
 */
public class BasicGame implements OperationListener {

	public static final int STATE_MOVE = 0x2;
	public static final int STATE_RMOVE = 0x3;
	public static final int STATE_ACTION = 0x4;
	public static final int STATE_ATTACK = 0x5;

	private int state;

	private final Map map;
	private final Player[] player_list;
	private int current_team;
	private GameListener game_listener;

	private UnitToolkit unit_toolkit;
	private Unit selected_unit;
	private ArrayList<Point> movable_positions;
	private ArrayList<Point> move_path;
	private Point last_position;
	private ArrayList<Point> attackable_positions;

	public BasicGame(Map map) {
		this.map = map;
		player_list = new Player[4];
	}

	public void init() {
		state = STATE_ACTION;
		selected_unit = null;
		unit_toolkit = new UnitToolkit(this);
		movable_positions = new ArrayList();
		move_path = new ArrayList();
		current_team = -1;
		for (int team = 0; team < player_list.length; team++) {
			if (player_list[team] != null && current_team == -1) {
				current_team = team;
				break;
			} else {

			}
		}
	}

	public ArrayList<Point> getMovablePositions() {
		return movable_positions;
	}

	public ArrayList<Point> getMovePath(int dest_x, int dest_y) {
		if (selected_unit != null && needNewMovePath(dest_x, dest_y)) {
			move_path = unit_toolkit.createMovePath(selected_unit, dest_x, dest_y);
		}
		return move_path;
	}

	public ArrayList<Point> getAttackablePositions() {
		return attackable_positions;
	}

	private boolean needNewMovePath(int dest_x, int dest_y) {
		Point dest = new Point(dest_x, dest_y);
		if (move_path.isEmpty()) {
			return true;
		} else {
			return !move_path.get(move_path.size() - 1).equals(dest);
		}
	}

	public void beginMovePhase() {
		if (isUnitAccessible(selected_unit) && state == STATE_ACTION) {
			movable_positions = unit_toolkit.createMovablePositions(selected_unit);
			this.state = STATE_MOVE;
		}
	}

	public void cancelMovePhase() {
		this.state = STATE_ACTION;
	}

	public void beginAttackPhase() {
		if (isUnitAccessible(selected_unit) && state == STATE_ACTION) {
			this.attackable_positions = unit_toolkit.createAttackablePositions(selected_unit);
			this.state = STATE_ATTACK;
		}
	}

	public void cancelAttackPhase() {
		this.state = STATE_ACTION;
	}

	protected void setState(int state) {
		this.state = state;
	}

	public int getState() {
		return state;
	}

	public boolean canAttack(int x, int y) {
		if (attackable_positions != null && attackable_positions.contains(new Point(x, y))) {
			Unit unit = getMap().getUnit(x, y);
			if (unit != null) {
				return isEnemy(unit);
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean isEnemy(Unit unit) {
		if (unit != null) {
			return unit.getTeam() != current_team;
		} else {
			return false;
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

	public void selectUnit(int x, int y) {
		selected_unit = getMap().getUnit(x, y);
		if (selected_unit != null) {
			last_position = new Point(x, y);
		}
	}

	public Unit getSelectedUnit() {
		return selected_unit;
	}

	@Override
	public void standbyUnit(int unit_x, int unit_y) {
		if (state == STATE_ACTION) {
			Unit unit = getMap().getUnit(unit_x, unit_y);
			if (isUnitAccessible(unit)) {
				unit.setStandby(true);
			}
		}
	}

	@Override
	public void moveUnit(int unit_x, int unit_y, int dest_x, int dest_y) {
		Unit unit = getMap().getUnit(unit_x, unit_y);
		if (isUnitAccessible(unit)) {
			move_path = unit_toolkit.createMovePath(unit, dest_x, dest_y);
			if (!move_path.isEmpty()) {
				setUnitPosition(unit, dest_x, dest_y);
				this.state = STATE_ACTION;
				game_listener.onUnitMove(unit, move_path);
			}
		}
	}

	public boolean canReverseMove() {
		if (last_position != null) {
			int last_x = last_position.x;
			int last_y = last_position.y;
			int current_x = selected_unit.getX();
			int current_y = selected_unit.getY();
			return current_x != last_x || current_y != last_y;
		} else {
			return false;
		}
	}

	@Override
	public void reverseMove() {
		if (isUnitAccessible(selected_unit) && state == STATE_ACTION) {
			if (canReverseMove()) {
				int last_x = last_position.x;
				int last_y = last_position.y;
				setUnitPosition(selected_unit, last_x, last_y);
				this.state = STATE_MOVE;
				beginMovePhase();
			}
		}
	}

	protected void setUnitPosition(Unit unit, int x, int y) {
		getMap().removeUnit(unit.getX(), unit.getY());
		unit.setX(x);
		unit.setY(y);
		getMap().addUnit(unit);
	}

	public boolean isUnitAccessible(Unit unit) {
		return unit != null && unit.getTeam() == current_team && !unit.isStandby();
	}

	public Map getMap() {
		return map;
	}

	@Override
	public void endTurn() {

	}

}
