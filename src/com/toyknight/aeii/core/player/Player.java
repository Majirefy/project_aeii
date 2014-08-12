package com.toyknight.aeii.core.player;

import com.toyknight.aeii.core.OperationListener;

/**
 *
 * @author toyknight
 */
public abstract class Player {
	
	private int alliance;

	private final OperationListener opt_listener;

	public Player(OperationListener opt_listener) {
		this.opt_listener = opt_listener;
	}

	public OperationListener getOperationListener() {
		return opt_listener;
	}

	public void setAlliance(int alliance) {
		this.alliance = alliance;
	}

	public int getAlliance() {
		return alliance;
	}

	abstract public void play();

}
