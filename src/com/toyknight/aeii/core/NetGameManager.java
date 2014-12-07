
package com.toyknight.aeii.core;

import com.toyknight.aeii.core.animation.AnimationProvider;

/**
 *
 * @author toyknight
 */
public class NetGameManager extends LocalGameManager {
    
    private final boolean is_host;

	public NetGameManager(Game game, AnimationProvider provider, boolean is_host) {
		super(game, provider);
        this.is_host = is_host;
	}
	
}
