
package com.toyknight.aeii.core.creator;

import com.toyknight.aeii.core.Game;

/**
 *
 * @author toyknight
 */
public interface GameCreator {
	
	public void setGameCreatorListener(GameCreatorListener listener);
	
	public boolean canChangeProperty();
	
	public String[] getMapList();
	
	public void refreshMapList();
	
	public void changeMap(int index);
	
	public void setMaxPopulation(int population);
	
	public void setStartGold(int gold);
	
	public boolean canCreate();
	
	public Game create();
	
}
