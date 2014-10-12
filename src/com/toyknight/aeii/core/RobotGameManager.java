
package com.toyknight.aeii.core;

import com.toyknight.aeii.core.animation.AnimationDispatcher;

/**
 *
 * @author toyknight
 */
public class RobotGameManager extends GameManager {
	
	private final AnimationDispatcher animation_dispatcher;

	public RobotGameManager(Game game, AnimationDispatcher dispatcher) {
		super(game);
		this.animation_dispatcher = dispatcher;
	}
	
	@Override
	public void moveSelectedUnit(int dest_x, int dest_y) {
		super.moveSelectedUnit(dest_x, dest_y);
		syncAnimation();
	}
	
	@Override
	public void doAttack(int target_x, int target_y) {
		super.doAttack(target_x, target_y);
		syncAnimation();
	}
	
	@Override
	public void doSummon(int target_x, int target_y) {
		super.doSummon(target_x, target_y);
		syncAnimation();
	}
	
	@Override
	public void doOccupy(int x, int y) {
		super.doOccupy(x, y);
		syncAnimation();
	}
	
	@Override
	public void doRepair(int x, int y) {
		super.doRepair(x, y);
		syncAnimation();
	}
	
	private void syncAnimation() {
		while(animation_dispatcher.isAnimating()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException ex) {
			}
		}
	}
	
}
