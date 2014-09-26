
package com.toyknight.aeii.core;

import com.toyknight.aeii.core.unit.Unit;

/**
 *
 * @author toyknight
 */
public interface GameListener {
	
	public void onUnitAttack(Unit attacker, Unit defender, int damage);
	
	public void onUnitAttackFinished(Unit attacker, Unit defender);
	
	public void onUnitDestroyed(Unit unit);
	
	public void onUnitMove(Unit unit, int start_x, int start_y, int dest_x, int dest_y);
	
}
