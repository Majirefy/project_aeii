
package com.toyknight.aeii.core.map;

/**
 *
 * @author toyknight
 */
public interface MapProvider {
	
	public int getMapCount();
	
	public Map getMap(int index);
	
	public String getMapName(int index);
	
	public void refreshMapList();
	
}
