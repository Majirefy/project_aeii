
package com.toyknight.aeii.core.event;

import com.toyknight.aeii.core.GameListener;
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
	public boolean canExecute() {
		return true;
	}

	@Override
	public void execute(GameListener listener) {
		unit.updateBuff();
	}
	
}
