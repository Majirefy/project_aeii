
package com.toyknight.aeii.core;

import com.toyknight.aeii.core.map.Tile;
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
	public static final int STATE_SUMMON = 0x6;
	
	private int state;
	private boolean is_new_unit_phase;
	private final Game game;
	private final UnitToolkit unit_toolkit;
	
	private Unit selected_unit;
	private Point last_position;
	private ArrayList<Point> movable_positions;
	private ArrayList<Point> attackable_positions;
	
	public GameManager(Game game) {
		this.game = game;
		this.state = STATE_SELECT;
		this.is_new_unit_phase = false;
		this.selected_unit = null;
		this.unit_toolkit = new UnitToolkit(game);
	}
	
	public Game getGame() {
		return game;
	}

	protected void setState(int state) {
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

	public ArrayList<Point> getAttackablePositions() {
		return attackable_positions;
	}

	public void beginMovePhase() {
		if (getUnitToolkit().isUnitAccessible(getSelectedUnit())) {
			movable_positions = getUnitToolkit().createMovablePositions();
			setState(STATE_MOVE);
		}
	}

	protected void beginRMovePhase() {
		if (getUnitToolkit().isUnitAccessible(getSelectedUnit())) {
			getUnitToolkit().setCurrentUnit(getSelectedUnit());
			movable_positions = getUnitToolkit().createMovablePositions();
			setState(STATE_RMOVE);
		}
	}

	public void cancelMovePhase() {
		if (!is_new_unit_phase) {
			setState(STATE_SELECT);
		}
	}

	public boolean canCancelMovePhase() {
		return !is_new_unit_phase;
	}

	public void beginAttackPhase() {
		if (getUnitToolkit().isUnitAccessible(getSelectedUnit()) && isActionState()) {
			this.attackable_positions = unit_toolkit.createAttackablePositions(selected_unit);
			setState(STATE_ATTACK);
		}
	}

	public void cancelActionPhase() {
		if (canReverseMove()) {
			setState(STATE_ACTION);
		} else {
			setState(STATE_SELECT);
		}
	}

	public void beginSummonPhase() {
		if (getUnitToolkit().isUnitAccessible(getSelectedUnit()) && isActionState()) {
			this.attackable_positions = unit_toolkit.createAttackablePositions(selected_unit);
			setState(STATE_SUMMON);
		}
	}

	public void buyUnit(int unit_index, int x, int y) {
		if (this.isAccessibleCastle(x, y)) {
			getGame().buyUnit(unit_index, x, y);
			selectUnit(x, y);
			beginMovePhase();
			is_new_unit_phase = true;
		}
	}

	public void selectUnit(int x, int y) {
		if (state == STATE_SELECT) {
			Unit unit = getGame().getMap().getUnit(x, y);
			if (unit != null) {
				selected_unit = unit;
				last_position = new Point(x, y);
				unit_toolkit.setCurrentUnit(selected_unit);
				if (is_new_unit_phase) {
					is_new_unit_phase = false;
				}
			}
		}
	}

	public Unit getUnit(int x, int y) {
		return getGame().getMap().getUnit(x, y);
	}

	public Unit getSelectedUnit() {
		return selected_unit;
	}

	public boolean canAttack(int x, int y) {
		Unit attacker = getSelectedUnit();
		if (attacker != null && UnitToolkit.isWithinRange(attacker, x, y)) {
			Unit defender = getGame().getMap().getUnit(x, y);
			if (defender != null) {
				return UnitToolkit.isEnemy(attacker, defender);
			} else {
				if (attacker.hasAbility(Ability.DESTROYER)) {
					return getGame().getMap().getTile(x, y).isDestroyable();
				} else {
					return false;
				}
			}
		} else {
			return false;
		}
	}

	public boolean canSummon(int x, int y) {
		if (getGame().getMap().isTomb(x, y)) {
			return getGame().getMap().getUnit(x, y) == null;
		} else {
			return false;
		}
	}
	
	protected void onUnitActionFinished(Unit unit) {
		if (getGame().isLocalPlayer()) {
			if (UnitToolkit.canRemove(unit)) {
				beginRMovePhase();
			} else {
				setState(STATE_SELECT);
			}
		}
	}

	public void doAttack(int target_x, int target_y) {
		if (canAttack(target_x, target_y) && state == STATE_ATTACK) {
			Unit unit = getSelectedUnit();
			getGame().doAttack(unit.getX(), unit.getY(), target_x, target_y);
			onUnitActionFinished(unit);
		}
	}

	public void doSummon(int target_x, int target_y) {
		if (canSummon(target_x, target_y) && state == STATE_SUMMON) {
			Unit summoner = getSelectedUnit();
			getGame().doSummon(summoner.getX(), summoner.getY(), target_x, target_y);
			onUnitActionFinished(summoner);
		}
	}

	public void doOccupy(int x, int y) {
		Unit conqueror = getSelectedUnit();
		if (getGame().canOccupy(conqueror, x, y) && isActionState()) {
			getGame().doOccupy(conqueror.getX(), conqueror.getY(), x, y);
			onUnitActionFinished(conqueror);
		}
	}

	public void doRepair(int x, int y) {
		Unit repairer = getSelectedUnit();
		if (getGame().canRepair(repairer, x, y) && isActionState()) {
			getGame().doRepair(repairer.getX(), repairer.getY(), x, y);
			onUnitActionFinished(repairer);
		}
	}

	public void standbySelectedUnit() {
		Unit unit = getSelectedUnit();
		if (unit != null && isActionState()) {
			getGame().standbyUnit(unit.getX(), unit.getY());
			setState(STATE_SELECT);
		}
	}

	public void moveSelectedUnit(int dest_x, int dest_y) {
		Unit unit = getSelectedUnit();
		if (unit != null && (state == STATE_MOVE || state == STATE_RMOVE)) {
			int unit_x = unit.getX();
			int unit_y = unit.getY();
			if (canSelectedUnitMove(dest_x, dest_y)) {
				int mp_remains = getUnitToolkit().getMovementPointRemains(unit, dest_x, dest_y);
				getGame().moveUnit(unit_x, unit_y, dest_x, dest_y);
				unit.setCurrentMovementPoint(mp_remains);
				switch (state) {
					case STATE_MOVE:
						setState(STATE_ACTION);
						break;
					case STATE_RMOVE:
						getGame().standbyUnit(unit.getX(), unit.getY());
						setState(STATE_SELECT);
						break;
					default:
					//do nothing
				}
			}
		}
	}

	public boolean canSelectedUnitMove(int dest_x, int dest_y) {
		Point dest = getGame().getMap().getPosition(dest_x, dest_y);
		return movable_positions.contains(dest);
	}

	public void reverseMove() {
		Unit unit = getSelectedUnit();
		if (getUnitToolkit().isUnitAccessible(unit) && canReverseMove() && state == STATE_ACTION) {
			int last_x = last_position.x;
			int last_y = last_position.y;
			getGame().getMap().moveUnit(unit, last_x, last_y);
			unit.setCurrentMovementPoint(unit.getMovementPoint());
			beginMovePhase();
		}
	}

	public boolean canReverseMove() {
		Unit unit = getSelectedUnit();
		return !unit.isAt(last_position.x, last_position.y);
	}

	public boolean isActionState() {
		return state == STATE_SELECT || state == STATE_ACTION;
	}

	public boolean isAccessibleCastle(int x, int y) {
		Tile tile = getGame().getMap().getTile(x, y);
		if (tile.isCastle()) {
			return tile.getTeam() == getGame().getCurrentTeam();
		} else {
			return false;
		}
	}
	
}
