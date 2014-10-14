
package com.toyknight.aeii.core;

import com.toyknight.aeii.core.unit.Unit;

/**
 *
 * @author toyknight
 */
public interface GameListener {
	
	public void onUnitActionFinished(Unit unit);
	
	public void onUnitMoveFinished(Unit unit, int start_x, int start_y);
	
}
