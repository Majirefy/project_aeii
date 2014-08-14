package com.toyknight.aeii.core.map;

import com.toyknight.aeii.core.unit.Unit;
import java.util.ArrayList;

/**
 *
 * @author toyknight
 */
public class Map {

	private final String author;
	
	private final short[][] map_data;
	private final ArrayList<Unit> unit_list;
	private final ArrayList<Tomb> tomb_list;

	public Map(short[][] map_data, ArrayList<Unit> unit_list, String author) {
		this.map_data = map_data;
		this.unit_list = unit_list;
		this.tomb_list = new ArrayList();
		this.author = author;
	}
	
	public String getAuthor() {
		return author;
	}

	public boolean isWithinMap(int x, int y) {
		return 0 <= x && x < getWidth() && 0 <= y && y < getHeight();
	}

	public int getWidth() {
		return map_data.length;
	}

	public int getHeight() {
		return map_data[0].length;
	}

	public void setTile(short index, int x, int y) {
		map_data[x][y] = index;
	}

	public short getTileIndex(int x, int y) {
		return map_data[x][y];
	}
	
	public void addTomb(int x, int y) {
		tomb_list.add(new Tomb(x, y));
	}
	
	public void removeTomb(int x, int y) {
		for(Tomb tomb: tomb_list) {
			if(tomb.x == x && tomb.y == y) {
				tomb_list.remove(tomb);
				break;
			}
		}
	}
	
	public ArrayList<Tomb> getTombList() {
		return tomb_list;
	}

	public void addUnit(Unit unit) {
		unit_list.add(unit);
	}

	public void removeUnit(int x, int y) {
		for (int i = 0; i < unit_list.size(); i++) {
			Unit unit = unit_list.get(i);
			if (unit.getX() == x && unit.getY() == y) {
				unit_list.remove(i);
				break;
			}
		}
	}
	
	public ArrayList<Unit> getUnitList() {
		return unit_list;
	}

}
