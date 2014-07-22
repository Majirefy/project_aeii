
package com.toyknight.aeii.core.player;

import com.toyknight.aeii.core.OperationListener;

/**
 *
 * @author toyknight
 */
public abstract class Player {
	
	private final OperationListener opt_listener;
	
	public Player(OperationListener opt_listener) {
		this.opt_listener = opt_listener;
	}
	
	abstract public void play();
	
}
