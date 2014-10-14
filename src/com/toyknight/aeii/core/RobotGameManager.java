
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
		syncAnimation();
		super.moveSelectedUnit(dest_x, dest_y);
	}
	
	@Override
	public void doAttack(int target_x, int target_y) {
		syncAnimation();
		super.doAttack(target_x, target_y);
	}
	
	@Override
	public void doSummon(int target_x, int target_y) {
		syncAnimation();
		super.doSummon(target_x, target_y);
	}
	
	@Override
	public void doOccupy(int x, int y) {
		syncAnimation();
		super.doOccupy(x, y);
	}
	
	@Override
	public void doRepair(int x, int y) {
		syncAnimation();
		super.doRepair(x, y);
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
