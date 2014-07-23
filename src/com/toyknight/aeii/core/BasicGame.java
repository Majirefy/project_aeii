
package com.toyknight.aeii.core;

import com.toyknight.aeii.core.map.Map;

/**
 *
 * @author toyknight
 */
public class BasicGame {
	
	private final AnimationListener anime_listener;
	private final Map map;
	
	public BasicGame(AnimationListener anime_listener, Map map) {
		this.anime_listener = anime_listener;
		this.map = map;
	}
	
	public void endTurn() {
		
	}
	
}
