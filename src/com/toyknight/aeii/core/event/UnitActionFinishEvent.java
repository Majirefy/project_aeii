package com.toyknight.aeii.core.event;

import com.toyknight.aeii.core.GameListener;
import com.toyknight.aeii.core.animation.AnimationDispatcher;
import com.toyknight.aeii.core.unit.Unit;
import com.toyknight.aeii.core.unit.UnitToolkit;

/**
 *
 * @author toyknight
 */
public class UnitActionFinishEvent implements GameEvent {

	private final Unit unit;

	public UnitActionFinishEvent(Unit unit) {
		this.unit = unit;
	}

	@Override
	public void execute(GameListener listener, AnimationDispatcher dispatcher) {
		listener.onUnitActionFinished(unit);
	}

}
