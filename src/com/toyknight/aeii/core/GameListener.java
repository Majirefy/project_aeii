
package com.toyknight.aeii.core;

import com.toyknight.aeii.core.unit.Unit;
import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author toyknight
 */
public interface GameListener {
	
	public void onUnitAttack(Unit target, int damage);
	
	public void onUnitMove(Unit unit, ArrayList<Point> path);
	
}
