package com.toyknight.aeii.core;

import com.toyknight.aeii.core.map.Tile;
import com.toyknight.aeii.core.map.TileRepository;
import com.toyknight.aeii.core.unit.Ability;
import com.toyknight.aeii.core.unit.Unit;
import com.toyknight.aeii.core.unit.UnitToolkit;
import java.util.ArrayList;

/**
 *
 * @author toyknight
 */
public class GameManager {

	public static final int STATE_SELECT = 0x1;
	public static final int STATE_MOVE = 0x2;
	public static final int STATE_RMOVE = 0x3;
	public static final int STATE_ACTION = 0x4;
	public static final int STATE_ATTACK = 0x5;

	private int state;
	private final BasicGame game;
	private final UnitToolkit unit_toolkit;

	private Unit selected_unit;
	private Point last_position;
	private ArrayList<Point> move_path;
	private ArrayList<Point> movable_positions;
	private ArrayList<Point> attackable_positions;

	public GameManager(BasicGame game) {
		this.game = game;
		this.selected_unit = null;
		this.unit_toolkit = new UnitToolkit(game);
		this.state = STATE_SELECT;
	}

	public BasicGame getGame() {
		return game;
	}

	private void setState(int state) {
		this.state = state;
	}

	public int getState() {
		return state;
	}

	public UnitToolkit getUnitToolkit() {
		return unit_toolkit;
	}

	public ArrayList<Point> getMovablePositions() {
		return movable_positions;
	}

	public ArrayList<Point> getMovePath(int dest_x, int dest_y) {
		if (selected_unit != null && needNewMovePath(dest_x, dest_y)) {
			int start_x = selected_unit.getX();
			int start_y = selected_unit.getY();
			move_path = getUnitToolkit().createMovePath(start_x, start_y, dest_x, dest_y);
		}
		return move_path;
	}

	public ArrayList<Point> getAttackablePositions() {
		return attackable_positions;
	}

	private boolean needNewMovePath(int dest_x, int dest_y) {
		Point dest = new Point(dest_x, dest_y);
		if (move_path == null) {
			return true;
		} else {
			if (move_path.isEmpty()) {
				return true;
			} else {
				return !move_path.get(move_path.size() - 1).equals(dest);
			}
		}
	}

	public void beginMovePhase() {
		if (getUnitToolkit().isUnitAccessible(selected_unit)) {
			movable_positions = getUnitToolkit().createMovablePositions(selected_unit);
			this.state = STATE_MOVE;
		}
	}

	public void cancelMovePhase() {
		this.state = STATE_SELECT;
	}

	public void beginAttackPhase() {
		if (getUnitToolkit().isUnitAccessible(selected_unit)) {
			this.attackable_positions = unit_toolkit.createAttackablePositions(selected_unit);
			this.state = STATE_ATTACK;
		}
	}

	public void cancelAttackPhase() {
		if(canReverseMove()) {
			this.state = STATE_ACTION;
		} else {
			this.state = STATE_SELECT;
		}
		
	}

	public void selectUnit(int x, int y) {
		if (state == STATE_SELECT) {
			selected_unit = getGame().getMap().getUnit(x, y);
			if (selected_unit != null) {
				last_position = new Point(x, y);
			}
		}
	}

	public Unit getSelectedUnit() {
		return selected_unit;
	}

	public boolean canAttack(int x, int y) {
		if (selected_unit != null && attackable_positions != null
				&& attackable_positions.contains(new Point(x, y))) {
			Unit unit = getGame().getMap().getUnit(x, y);
			if (unit != null) {
				return unit_toolkit.isEnemy(selected_unit, unit);
			} else {
				if (selected_unit.getAbilities().contains(Ability.DESTROYER)) {
					int tile_index = getGame().getMap().getTileIndex(x, y);
					Tile tile = TileRepository.getTile(tile_index);
					return tile.isDestroyable();
				} else {
					return false;
				}
			}
		} else {
			return false;
		}
	}

	public void doAttack(int target_x, int target_y) {
		Unit defender = getGame().getMap().getUnit(target_x, target_y);
		if (getUnitToolkit().isEnemy(selected_unit, defender)) {
			int unit_x = selected_unit.getX();
			int unit_y = selected_unit.getY();
			getGame().doAttack(unit_x, unit_y, target_x, target_y);
			setState(STATE_SELECT);
		}
	}

	public void standbySelectedUnit() {
		if (selected_unit != null) {
			getGame().standbyUnit(selected_unit.getX(), selected_unit.getY());
			setState(STATE_SELECT);
		}
	}

	public void moveSelectedUnit(int dest_x, int dest_y) {
		if (selected_unit != null && state == STATE_MOVE) {
			int unit_x = selected_unit.getX();
			int unit_y = selected_unit.getY();
			if (movable_positions.contains(new Point(dest_x, dest_y))) {
				getGame().moveUnit(unit_x, unit_y, dest_x, dest_y);
				setState(STATE_ACTION);
			}
		}
	}

	public void reverseMove() {
		if (getUnitToolkit().isUnitAccessible(selected_unit) && state == STATE_ACTION) {
			int last_x = last_position.x;
			int last_y = last_position.y;
			getGame().moveUnit(selected_unit, last_x, last_y);
			beginMovePhase();
		}
	}

	public boolean canReverseMove() {
		return selected_unit.getX() != last_position.x || selected_unit.getY() != last_position.y;
	}

}
