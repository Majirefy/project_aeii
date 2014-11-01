
package com.toyknight.aeii.core.creator;

import com.toyknight.aeii.core.Game;
import com.toyknight.aeii.core.map.Map;

/**
 *
 * @author toyknight
 */
public interface GameCreator {
	
	public boolean canChangeProperty();
	
	public void setGameCreatorListener(GameCreatorListener listener);
	
	public Map getMap();
	
	public void setMap(Map map);
	
	public void setMaxPopulation(int population);
	
	public void setStartGold(int gold);
	
	public boolean canCreate();
	
	public Game create();
	
}
