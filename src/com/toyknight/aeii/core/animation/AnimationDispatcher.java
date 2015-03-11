
package com.toyknight.aeii.core.animation;

import com.toyknight.aeii.core.Point;
import com.toyknight.aeii.core.unit.Unit;
import java.util.Map;

/**
 *
 * @author toyknight
 */
public interface AnimationDispatcher {
	
	public void updateAnimation();
	
	public void submitAnimation(Animation animation);
	
	public Animation getCurrentAnimation();
	
	public boolean isAnimating();
	
	public void submitOccupyAnimation();
	
	public void submitRepairAnimation();
    
    public void submitSummonAnimation(Unit summoner, int target_x, int target_y);
	
	public void submitUnitHpChangeAnimation(Unit unit, int change);
    
    public void submitMapHpChangeAnimation(Map<Point, Integer> hp_change_map);
	
    public void submitTileDestroyAnimation(int tile_index, int x, int y);
	
	public void submitUnitAttackAnimation(Unit attacker, Unit defender, int damage);
	
	public void submitUnitDestroyAnimation(Unit unit);
	
	public void submitUnitLevelUpAnimation(Unit unit);
	
	public void submitUnitMoveAnimation(Unit unit, int start_x, int start_y, int dest_x, int dest_y);
	
	public void submitTurnStartAnimation(int turn, int income, int team);
	
	public void submitGameOverAnimation(int alliance);
	
}
