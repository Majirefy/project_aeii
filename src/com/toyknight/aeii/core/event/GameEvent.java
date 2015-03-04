
package com.toyknight.aeii.core.event;

import com.toyknight.aeii.core.GameListener;

/**
 *
 * @author toyknight
 */
public interface GameEvent {
	
	public boolean canExecute();
	
	public void execute(GameListener listener);
	
}
