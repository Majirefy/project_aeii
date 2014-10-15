
package com.toyknight.aeii.core.event;

import com.toyknight.aeii.core.GameListener;
import com.toyknight.aeii.core.animation.AnimationDispatcher;
import com.toyknight.aeii.core.unit.Unit;

/**
 *
 * @author toyknight
 */
public class BuffUpdateEvent implements GameEvent {
	
	private final Unit unit;
	
	public BuffUpdateEvent(Unit unit) {
		this.unit = unit;
	}

	@Override
	public void execute(GameListener listener, AnimationDispatcher dispatcher) {
		unit.updateBuff();
	}
	
}
