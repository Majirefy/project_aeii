package com.toyknight.aeii.core;

/**
 *
 * @author toyknight
 */
public interface OperationListener {

	public void buyUnit(int index, int x, int y);

	public void addUnit(int index, int team, int x, int y);

	public void doSummon(int summoner_x, int summoner_y, int target_x, int target_y);
	
	public void doHeal(int healer_x, int healer_y, int target_x, int target_y);
	
	public void doOccupy(int conqueror_x, int conqueror_y, int x, int y);
	
	public void doRepair(int repairer_x, int repairer_y, int x, int y);

	public void doAttack(int unit_x, int unit_y, int target_x, int target_y);

	public void standbyUnit(int unit_x, int unit_y);

	public void moveUnit(int unit_x, int unit_y, int dest_x, int dest_y);

	public void endTurn();

}
