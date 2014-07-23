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
	private final ArrayList<Tomb> tomb_list;

	public Map(short[][] map_data, ArrayList<Unit> unit_list) {
		this.map_data = map_data;
		this.unit_list = unit_list;
		this.tomb_list = new ArrayList();
	}

	public boolean isWithinMap(int x, int y) {
		return 0 <= x && x < getMapWidth() && 0 <= y && y < getMapHeight();
	}

	public int getMapWidth() {
		return map_data.length;
	}

	public int getMapHeight() {
		return map_data[0].length;
	}

	public final void setTile(short index, int x, int y) {
		map_data[x][y] = index;
	}

	public final short getTileIndex(int x, int y) {
		return map_data[x][y];
	}
	
	public final void addTomb(int x, int y) {
		tomb_list.add(new Tomb(x, y));
	}
	
	public final void removeTomb(int x, int y) {
		for(Tomb tomb: tomb_list) {
			if(tomb.x == x && tomb.y == y) {
				tomb_list.remove(tomb);
				break;
			}
		}
	}
	
	public final ArrayList<Tomb> getTombList() {
		return tomb_list;
	}

	public final void addUnit(Unit unit) {
		unit_list.add(unit);
	}

	public final void removeUnit(int x, int y) {
		for (int i = 0; i < unit_list.size(); i++) {
			Unit unit = unit_list.get(i);
			if (unit.getX() == x && unit.getY() == y) {
				unit_list.remove(i);
				break;
			}
		}
	}

	public final Unit getUnit(int x, int y) {
		for (Unit unit : unit_list) {
			if (unit.getX() == x && unit.getY() == y) {
				return unit;
			}
		}
		return null;
	}

}
