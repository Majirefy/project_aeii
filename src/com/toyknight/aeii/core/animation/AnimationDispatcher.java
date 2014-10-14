
package com.toyknight.aeii.core.animation;

import com.toyknight.aeii.core.unit.Unit;

/**
 *
 * @author toyknight
 */
public interface AnimationDispatcher {
	
	public void updateAnimation();
	
	public void submitAnimation(Animation animation);
	
	public Animation getCurrentAnimation();
	
	public boolean isAnimating();
	
	public void onOccupy();
	
	public void onRepair();
	
	public void onUnitHpChanged(Unit unit, int change);
	
	public void onSummon(Unit summoner, int target_x, int target_y);
	
	public void onUnitAttack(Unit attacker, Unit defender, int damage);
	
	public void onTileDestroyed(int tile_index, int x, int y);
	
	public void onUnitDestroyed(Unit unit);
	
	public void onUnitMove(Unit unit, int start_x, int start_y, int dest_x, int dest_y);
	
	public void onTurnStart(int turn, int income, int team);
	
}
