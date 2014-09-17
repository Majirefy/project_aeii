
package com.toyknight.aeii.core;

import com.toyknight.aeii.core.unit.Unit;

/**
 *
 * @author toyknight
 */
public interface GameListener {
	
	public void onUnitAttack(Unit attacker, Unit defender, int attack_damage, int counter_damage);
	
	public void onUnitAttackFinished(Unit attacker, Unit defender);
	
	public void onUnitMove(Unit unit, int start_x, int start_y, int dest_x, int dest_y);
	
}
