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

	public static final int ST_NORMAL = 0x1;
	public static final int ST_MOVE = 0x2;
	public static final int ST_RMOVE = 0x3;
	public static final int ST_ACTION = 0x4;

	private int state;

	private final Map map;
	private final Player[] player_list;
	private int current_team;
	private GameListener game_listener;

	private UnitToolkit unit_toolkit;
	private Unit selected_unit;
	private ArrayList<Point> movable_positions;
	private ArrayList<Point> move_path;

	public BasicGame(Map map) {
		this.map = map;
		player_list = new Player[4];
	}

	public void init() {
		state = ST_NORMAL;
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

	private boolean needNewMovePath(int dest_x, int dest_y) {
		Point dest = new Point(dest_x, dest_y);
		if (move_path.isEmpty()) {
			return true;
		} else {
			return !move_path.get(move_path.size() - 1).equals(dest);
		}
	}

	public void beginMovePhase() {
		if (selected_unit != null && selected_unit.getTeam() == current_team) {
			movable_positions = unit_toolkit.createMovablePositions(selected_unit);
			this.state = ST_MOVE;
		}
	}

	public void cancelMovePhase() {
		this.state = ST_NORMAL;
	}

	protected void setState(int state) {
		this.state = state;
	}

	public int getState() {
		return state;
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
	public void selectUnit(int x, int y) {
		selected_unit = getMap().getUnit(x, y);
	}

	public Unit getSelectedUnit() {
		return selected_unit;
	}

	@Override
	public void moveUnit(int dest_x, int dest_y) {
		if (selected_unit != null && selected_unit.isAvailable()) {
			move_path = unit_toolkit.createMovePath(selected_unit, dest_x, dest_y);
			if (!move_path.isEmpty()) {
				getMap().removeUnit(selected_unit.getX(), selected_unit.getY());
				selected_unit.setX(dest_x);
				selected_unit.setY(dest_y);
				getMap().addUnit(selected_unit);
				this.state = ST_ACTION;
				game_listener.onUnitMove(selected_unit, move_path);
			}
		}
	}

	public Map getMap() {
		return map;
	}

	@Override
	public void endTurn() {

	}

}
