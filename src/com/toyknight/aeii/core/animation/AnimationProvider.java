
package com.toyknight.aeii.core.animation;

import com.toyknight.aeii.core.unit.Unit;

/**
 *
 * @author toyknight
 */
public interface AnimationProvider {
	
	public Animation getSmokeAnimation(int x, int y);
	
	public Animation getUnitAttackAnimation(Unit attacker, Unit defender, int damage);
	
	public Animation getUnitDestroyedAnimation(Unit unit);
	
	public Animation getUnitMoveAnimation(Unit unit, int start_x, int start_y, int dest_x, int dest_y);
	
}
