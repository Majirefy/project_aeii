
package com.toyknight.aeii.core.map;

import com.toyknight.aeii.core.unit.Unit;
import java.util.ArrayList;

/**
 *
 * @author toyknight
 */
public class Map {
	
	private final short[][] map_data;
	private final ArrayList<Unit> unit_list;
	
	public Map(short[][] map_data, ArrayList<Unit> unit_list) {
		this.map_data = map_data;
		this.unit_list = unit_list;
	}
	
	public final Unit getUnit(int x, int y) {
		for(Unit unit: unit_list) {
			if(unit.getX() == x && unit.getY() == y) {
				return unit;
			}
		}
		return null;
	}
	
}
