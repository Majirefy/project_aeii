
package com.toyknight.aeii.core;

/**
 *
 * @author toyknight
 */
public interface OperationListener {
	
	public void selectUnit(int x, int y);
	
	public void moveUnit(int dest_x, int dest_y);
	
	public void endTurn();
	
}
