
package com.toyknight.aeii.core.map;

import com.toyknight.aeii.core.unit.Unit;
import java.util.ArrayList;

/**
 *
 * @author toyknight
 */
public class BasicMap {
	
	private final ArrayList<Unit> unit_list = new ArrayList();
	
	public final Unit getUnit(int x, int y) {
		for(Unit unit: unit_list) {
			if(unit.getXPosition() == x && unit.getYPosition() == y) {
				return unit;
			}
		}
		return null;
	}
	
}
