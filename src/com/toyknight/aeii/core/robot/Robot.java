
package com.toyknight.aeii.core.robot;

import com.toyknight.aeii.core.RobotGameManager;

/**
 *
 * @author toyknight
 */
public abstract class Robot implements Runnable {
	
	private final RobotGameManager manager;
	
	public Robot(RobotGameManager manager) {
		this.manager = manager;
	}
	
	protected final RobotGameManager getManager() {
		return manager;
	}
	
	abstract protected void begin();

	@Override
	public final void run() {
		begin();
		manager.getGame().endTurn();
	}
	
}
