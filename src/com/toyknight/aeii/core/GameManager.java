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
	private int last_state;
	private boolean is_new_unit_phase;
	private final Game game;
	private final UnitToolkit unit_toolkit;
	private ManagerStateListener state_listener;

	private Unit selected_unit;
	protected Point last_position;
	protected boolean is_selected_unit_moved;
	private ArrayList<Point> movable_positions;
	private ArrayList<Point> attackable_positions;

	public GameManager(Game game) {
		this.game = game;
		this.state = STATE_SELECT;
		this.last_state = state;
		this.is_new_unit_phase = false;
		this.selected_unit = null;
		this.unit_toolkit = new UnitToolkit(game);
	}

	public Game getGame() {
		return game;
	}

	public void setStateListener(ManagerStateListener listener) {
		this.state_listener = listener;
	}

	protected void setState(int state) {
		if (state != this.state) {
			this.last_state = this.state;
			this.state = state;
			if (state_listener != null) {
				state_listener.managerStateChanged(this);
			}
		}
	}

	public int getState() {
		return state;
	}

	public int getLastState() {
		return last_state;
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
		if (canCancelMovePhase()) {
			setState(STATE_SELECT);
		}
	}

	public boolean canCancelMovePhase() {
		return !is_new_unit_phase;
	}

	public void beginAttackPhase() {
		if (getUnitToolkit().isUnitAccessible(getSelectedUnit()) && isActionState()) {
			this.attackable_positions = getUnitToolkit().createAttackablePositions(selected_unit);
			setState(STATE_ATTACK);
		}
	}

	public void cancelActionPhase() {
		if (state == STATE_ATTACK || state == STATE_SUMMON) {
			setState(last_state);
		} else {
			setState(last_state);
		}
	}

	public void beginSummonPhase() {
		if (getUnitToolkit().isUnitAccessible(getSelectedUnit()) && isActionState()) {
			this.attackable_positions = getUnitToolkit().createAttackablePositions(selected_unit);
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
				is_selected_unit_moved = false;
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

	public void restoreCommander(int x, int y) {
		int team = getGame().getCurrentTeam();
		if (!getGame().isCommanderAlive(team)) {
			getGame().restoreCommander(team, x, y);
			selectUnit(x, y);
			beginMovePhase();
			is_new_unit_phase = true;
		}
	}

	public void standbySelectedUnit() {
		Unit unit = getSelectedUnit();
		if (unit != null && (isActionState() || (isNewUnitPhase() && state == STATE_MOVE))) {
			if (getGame().getMap().canStandby(unit)) {
				getGame().standbyUnit(unit.getX(), unit.getY());
				setState(STATE_SELECT);
			}
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
				is_selected_unit_moved = true;
				switch (state) {
					case STATE_MOVE:
						if (unit.hasAbility(Ability.SIEGE_MACHINE)) {
							getGame().standbyUnit(unit.getX(), unit.getY());
							setState(STATE_SELECT);
						} else {
							setState(STATE_ACTION);
						}
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
		return movable_positions.contains(dest) && getGame().getMap().canMove(dest_x, dest_y);
	}

	public boolean isActionState() {
		return state == STATE_SELECT || state == STATE_ACTION;
	}

	public boolean isNewUnitPhase() {
		return is_new_unit_phase;
	}

	public boolean isAccessibleCastle(int x, int y) {
		if (getGame().getMap().isWithinMap(x, y)) {
			Tile tile = getGame().getMap().getTile(x, y);
			return tile.isCastle() && tile.getTeam() == getGame().getCurrentTeam();
		} else {
			return false;
		}
	}

}
