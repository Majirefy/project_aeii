package com.toyknight.aeii.core;

import com.toyknight.aeii.core.animation.Animation;
import com.toyknight.aeii.core.animation.AnimationListener;
import com.toyknight.aeii.core.animation.AnimationProvider;
import com.toyknight.aeii.core.map.Tile;
import com.toyknight.aeii.core.map.TileRepository;
import com.toyknight.aeii.core.unit.Ability;
import com.toyknight.aeii.core.unit.Unit;
import com.toyknight.aeii.core.unit.UnitToolkit;
import com.toyknight.aeii.gui.animation.UnitAttackAnimation;
import com.toyknight.aeii.gui.animation.UnitMoveAnimation;
import java.util.ArrayList;
import java.util.PriorityQueue;

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
	private final BasicGame game;
	private final UnitToolkit unit_toolkit;
	private final AnimationProvider animation_provider;
	
	private final PriorityQueue<Animation> animation_dispatcher;
	private Animation current_animation;

	private Unit selected_unit;
	private Point last_position;
	private ArrayList<Point> movable_positions;
	private ArrayList<Point> attackable_positions;

	public GameManager(BasicGame game, AnimationProvider provider) {
		this.game = game;
		this.animation_provider = provider;
		this.state = STATE_SELECT;
		this.selected_unit = null;
		this.unit_toolkit = new UnitToolkit(game);
		this.animation_dispatcher = new PriorityQueue();
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
		Animation animation = animation_provider.getUnitAttackAnimation(attacker, defender, damage);
		submitAnimation(animation);
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
		if (getUnitToolkit().isUnitAccessible(selected_unit)) {
			movable_positions = getUnitToolkit().createMovablePositions();
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
			Unit unit = getGame().getMap().getUnit(x, y);
			if (unit != null) {
				selected_unit = unit;
				last_position = new Point(x, y);
				unit_toolkit.setCurrentUnit(selected_unit);
			}
		}
	}

	public Unit getSelectedUnit() {
		return selected_unit;
	}

	public boolean canAttack(int x, int y) {
		if (selected_unit != null && UnitToolkit.isWithinRange(selected_unit, x, y)) {
			Unit defender = getGame().getMap().getUnit(x, y);
			if (defender != null) {
				return UnitToolkit.isEnemy(selected_unit, defender);
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
		if (canAttack(target_x, target_y)) {
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
