
package com.toyknight.aeii.core.event;

import com.toyknight.aeii.core.GameListener;
import com.toyknight.aeii.core.unit.Buff;
import com.toyknight.aeii.core.unit.Unit;

/**
 *
 * @author toyknight
 */
public class BuffAttachEvent implements GameEvent {
	
	private final Unit unit;
	private final Buff buff;
	
	public BuffAttachEvent(Unit unit, Buff buff) {
		this.unit = unit;
		this.buff = buff;
	}

	@Override
	public void execute(GameListener listener) {
		unit.attachBuff(buff);
	}
	
}
