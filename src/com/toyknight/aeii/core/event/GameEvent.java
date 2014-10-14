
package com.toyknight.aeii.core.event;

import com.toyknight.aeii.core.GameListener;
import com.toyknight.aeii.core.animation.AnimationDispatcher;

/**
 *
 * @author toyknight
 */
public interface GameEvent {
	
	public void execute(GameListener listener, AnimationDispatcher dispatcher);
	
}
