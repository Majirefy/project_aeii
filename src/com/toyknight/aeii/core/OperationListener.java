
package com.toyknight.aeii.core;

/**
 *
 * @author toyknight
 */
public interface OperationListener {
	
	public void standbyUnit(int unit_x, int unit_y);
	
	public void moveUnit(int unit_x, int unit_y, int dest_x, int dest_y);
	
	public void reverseMove();
	
	public void endTurn();
	
}
