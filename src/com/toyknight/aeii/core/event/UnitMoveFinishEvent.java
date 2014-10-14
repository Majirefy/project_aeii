
package com.toyknight.aeii.core.event;

import com.toyknight.aeii.core.GameListener;
import com.toyknight.aeii.core.animation.AnimationDispatcher;
import com.toyknight.aeii.core.unit.Unit;

/**
 *
 * @author toyknight
 */
public class UnitMoveFinishEvent implements GameEvent {
	
	private final Unit unit;
	private final int start_x;
	private final int start_y;
	
	public UnitMoveFinishEvent(Unit unit, int start_x, int start_y) {
		this.unit = unit;
		this.start_x = start_x;
		this.start_y = start_y;
	}

	@Override
	public void execute(GameListener listener, AnimationDispatcher dispatcher) {
		listener.onUnitMoveFinished(unit, start_x, start_y);
	}
	
}
