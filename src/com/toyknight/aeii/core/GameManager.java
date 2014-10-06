package com.toyknight.aeii.core;

import com.toyknight.aeii.core.animation.Animation;
import com.toyknight.aeii.core.animation.AnimationListener;
import com.toyknight.aeii.core.animation.AnimationProvider;
import com.toyknight.aeii.core.map.Tile;
import com.toyknight.aeii.core.map.TileRepository;
import com.toyknight.aeii.core.unit.Ability;
import com.toyknight.aeii.core.unit.Unit;
import com.toyknight.aeii.core.unit.UnitToolkit;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author toyknight
 */
public class GameManager implements GameListener {

	public static final int STATE_SELECT = 0x1;
	public static final int STATE_MOVE = 0x2;
	public static final int STATE_RMOVE = 0x3;
	public static final int STATE_ACTION = 0x4;
	public static final int STATE_ATTACK = 0x5;

	private int state;
	private boolean is_new_unit_phase;
	private final BasicGame game;
	private final UnitToolkit unit_toolkit;
	private final AnimationProvider animation_provider;

	private final Queue<Animation> animation_dispatcher;
	private Animation current_animation;

	private Unit selected_unit;
	private Point last_position;
	private ArrayList<Point> movable_positions;
	private ArrayList<Point> attackable_positions;

	public GameManager(BasicGame game, AnimationProvider provider) {
		this.game = game;
		this.animation_provider = provider;
		this.state = STATE_SELECT;
		this.is_new_unit_phase = false;
		this.selected_unit = null;
		this.unit_toolkit = new UnitToolkit(game);
		this.animation_dispatcher = new LinkedList();
		current_animation = null;
		this.game.setGameListener(this);
	}

	public void updateAnimation() {
		if (current_animation != null) {
			current_animation.update();
		}
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

	public void submitAnimation(Animation animation) {
		animation.addAnimationListener(new AnimationListener() {
			@Override
			public void animationCompleted(Animation animation) {
				current_animation = animation_dispatcher.poll();
			}
		});
		if (current_animation == null) {
			current_animation = animation;
		} else {
			animation_dispatcher.add(animation);
		}
	}

	public Animation getCurrentAnimation() {
		return current_animation;
	}

	public UnitToolkit getUnitToolkit() {
		return unit_toolkit;
	}

	@Override
	public void onUnitAttack(Unit attacker, Unit defender, int damage) {
		Animation attack_animation = animation_provider.getUnitAttackAnimation(attacker, defender, damage);
		submitAnimation(attack_animation);
	}

	@Override
	public void onUnitAttackFinished(Unit attacker, Unit defender) {
		if (getGame().isLocalPlayer()) {
			if (attacker.getCurrentHp() > 0 && attacker.getCurrentMovementPoint() > 0
					&& attacker.hasAbility(Ability.CHARGER)) {
				beginRMovePhase();
			} else {
				attacker.setStandby(true);
				setState(STATE_SELECT);
			}
		}
	}

	@Override
	public void onUnitDestroyed(Unit unit) {
		Animation animation = animation_provider.getUnitDestroyedAnimation(unit);
		submitAnimation(animation);
		Animation smoke_animation = animation_provider.getSmokeAnimation(unit.getX(), unit.getY());
		submitAnimation(smoke_animation);
	}

	@Override
	public void onUnitMove(Unit unit, int start_x, int start_y, int dest_x, int dest_y) {
		Animation animation = animation_provider.getUnitMoveAnimation(unit, start_x, start_y, dest_x, dest_y);
		submitAnimation(animation);
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

	private void beginRMovePhase() {
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

	public void beginAttackPhase() {
		if (getUnitToolkit().isUnitAccessible(getSelectedUnit())) {
			this.attackable_positions = unit_toolkit.createAttackablePositions(selected_unit);
			setState(STATE_ATTACK);
		}
	}

	public void cancelAttackPhase() {
		if (canReverseMove()) {
			setState(STATE_ACTION);
		} else {
			setState(STATE_SELECT);
		}
	}

	public void buyUnit(int unit_index, int x, int y) {
		int tile_index = getGame().getMap().getTileIndex(x, y);
		if (this.isAccessibleCastle(tile_index)) {
			getGame().buyUnit(unit_index, x, y);
			selectUnit(x, y);
			beginMovePhase();
		}
	}

	public void selectUnit(int x, int y) {
		if (state == STATE_SELECT) {
			Unit unit = getGame().getMap().getUnit(x, y);
			if (unit != null) {
				selected_unit = unit;
				last_position = new Point(x, y);
				unit_toolkit.setCurrentUnit(selected_unit);
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
		Unit unit = getSelectedUnit();
		if (unit != null && UnitToolkit.isWithinRange(unit, x, y)) {
			Unit defender = getGame().getMap().getUnit(x, y);
			if (defender != null) {
				return UnitToolkit.isEnemy(unit, defender);
			} else {
				if (unit.hasAbility(Ability.DESTROYER)) {
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
		Unit unit = getSelectedUnit();
		if (canAttack(target_x, target_y)) {
			int unit_x = unit.getX();
			int unit_y = unit.getY();
			getGame().doAttack(unit_x, unit_y, target_x, target_y);
		}
	}

	public void standbySelectedUnit() {
		Unit unit = getSelectedUnit();
		if (unit != null) {
			getGame().standbyUnit(unit.getX(), unit.getY());
			setState(STATE_SELECT);
		}
	}

	public void moveSelectedUnit(int dest_x, int dest_y) {
		Unit unit = getSelectedUnit();
		if (unit != null && (state == STATE_MOVE || state == STATE_RMOVE)) {
			int unit_x = unit.getX();
			int unit_y = unit.getY();
			if (movable_positions.contains(new Point(dest_x, dest_y))) {
				int mp_remains = getUnitToolkit().getMovementPointRemains(unit, dest_x, dest_y);
				getGame().moveUnit(unit_x, unit_y, dest_x, dest_y);
				unit.setCurrentMovementPoint(mp_remains);
				switch (state) {
					case STATE_MOVE:
						if (is_new_unit_phase) {
							is_new_unit_phase = false;
						}
						setState(STATE_ACTION);
						break;
					case STATE_RMOVE:
						unit.setStandby(true);
						setState(STATE_SELECT);
						break;
					default:
					//do nothing
				}
			}
		}
	}

	public void reverseMove() {
		Unit unit = getSelectedUnit();
		if (getUnitToolkit().isUnitAccessible(unit) && state == STATE_ACTION) {
			int last_x = last_position.x;
			int last_y = last_position.y;
			getGame().moveUnit(unit, last_x, last_y);
			unit.setCurrentMovementPoint(unit.getMovementPoint());
			beginMovePhase();
		}
	}

	public boolean canReverseMove() {
		Unit unit = getSelectedUnit();
		return unit.getX() != last_position.x || unit.getY() != last_position.y;
	}

	public boolean isAccessibleCastle(int index) {
		if (index == 39 || index == 41 || index == 43 || index == 45) {
			Tile tile = TileRepository.getTile(index);
			return tile.getTeam() == getGame().getCurrentTeam();
		} else {
			return false;
		}
	}

}
