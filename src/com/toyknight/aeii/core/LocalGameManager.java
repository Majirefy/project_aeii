package com.toyknight.aeii.core;

import com.toyknight.aeii.core.animation.Animation;
import com.toyknight.aeii.core.animation.AnimationDispatcher;
import com.toyknight.aeii.core.animation.AnimationListener;
import com.toyknight.aeii.core.animation.AnimationProvider;
import com.toyknight.aeii.core.unit.Unit;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author toyknight
 */
public class LocalGameManager extends GameManager implements AnimationDispatcher {

	private final AnimationProvider animation_provider;

	private final Queue<Animation> animation_dispatcher;
	private Animation current_animation;

	public LocalGameManager(Game game, AnimationProvider provider) {
		super(game);
		this.animation_provider = provider;
		this.animation_dispatcher = new LinkedList();
		current_animation = null;
	}

	@Override
	public void updateAnimation() {
		if (current_animation != null) {
			current_animation.update();
		}
	}

	@Override
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

	@Override
	public Animation getCurrentAnimation() {
		return current_animation;
	}

	@Override
	public boolean isAnimating() {
		return current_animation != null;
	}

	public void reverseMove() {
		Unit unit = getSelectedUnit();
		if (getUnitToolkit().isUnitAccessible(unit) && canReverseMove() && getState() == STATE_ACTION) {
			int last_x = last_position.x;
			int last_y = last_position.y;
			getGame().getMap().moveUnit(unit, last_x, last_y);
			unit.setCurrentMovementPoint(unit.getMovementPoint());
			beginMovePhase();
		}
	}

	public boolean canReverseMove() {
		return is_selected_unit_moved;
	}

	@Override
	public void onOccupy() {
		Animation msg_animation = animation_provider.getOccupiedMessageAnimation();
		submitAnimation(msg_animation);
	}

	@Override
	public void onRepair() {
		Animation msg_animation = animation_provider.getRepairedMessageAnimation();
		submitAnimation(msg_animation);
	}

	@Override
	public void onSummon(Unit summoner, int target_x, int target_y) {
		Animation summon_animation = animation_provider.getSummonAnimation(summoner, target_x, target_y);
		submitAnimation(summon_animation);
	}

	@Override
	public void onUnitHpChanged(Unit unit, int change) {
		Animation change_animation = animation_provider.getUnitHpChangeAnimation(unit, change);
		submitAnimation(change_animation);
	}

	@Override
	public void onUnitAttack(Unit attacker, Unit defender, int damage) {
		Animation attack_animation = animation_provider.getUnitAttackAnimation(attacker, defender, damage);
		submitAnimation(attack_animation);
	}

	@Override
	public void onUnitDestroyed(Unit unit) {
		Animation animation = animation_provider.getUnitDestroyedAnimation(unit);
		submitAnimation(animation);
		Animation smoke_animation = animation_provider.getSmokeAnimation(unit.getX(), unit.getY());
		submitAnimation(smoke_animation);
	}
	
	@Override
	public void onUnitLevelUp(Unit unit) {
		Animation animation = animation_provider.getUnitLevelUpAnimation(unit);
		submitAnimation(animation);
	}

	@Override
	public void onTileDestroyed(int tile_index, int x, int y) {
		Animation animation = animation_provider.getTileAttackedAnimation(tile_index, x, y);
		submitAnimation(animation);
		Animation smoke_animation = animation_provider.getSmokeAnimation(x, y);
		submitAnimation(smoke_animation);
	}

	@Override
	public void onUnitMove(Unit unit, int start_x, int start_y, int dest_x, int dest_y) {
		Animation animation = animation_provider.getUnitMoveAnimation(unit, start_x, start_y, dest_x, dest_y);
		submitAnimation(animation);
	}

	@Override
	public void onTurnStart(int turn, int income, int team) {
		income = getGame().isLocalPlayer() ? income : -1;
		Animation animation = animation_provider.getTurnStartAnimation(turn, income, team);
		submitAnimation(animation);
	}
	
	@Override
	public void onGameOver(int alliance) {
		
	}

	public void update() {
		updateAnimation();
		getGame().dispatchGameEvent();
	}
	
	@Override
	public boolean isProcessing() {
		return super.isProcessing() || isAnimating();
	}

}
