
package com.toyknight.aeii.core.player;

import com.toyknight.aeii.core.OperationListener;

/**
 *
 * @author toyknight
 */
public class LocalPlayer extends Player {

	public LocalPlayer(OperationListener opt_listener) {
		super(opt_listener);
	}

	@Override
	public void play() {
		//do nothing. wait for user's operation on gui.
	}
	
}
