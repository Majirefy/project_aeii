
package com.toyknight.aeii.core.robot;

import com.toyknight.aeii.core.manager.GameManager;

/**
 *
 * @author toyknight
 */
public abstract class Robot implements Runnable {
	
	private final GameManager manager;
	
	public Robot(GameManager manager) {
		this.manager = manager;
	}
	
	public final GameManager getManager() {
		return manager;
	}
	
	abstract protected void begin();

	@Override
	public final void run() {
		begin();
		manager.getGame().endTurn();
	}
	
}
