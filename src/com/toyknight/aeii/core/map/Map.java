package com.toyknight.aeii.core.map;

import com.toyknight.aeii.core.unit.Unit;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author toyknight
 */
public class Map {

	private final String author;
	
	private final short[][] map_data;
	private ArrayList<Unit> unit_list;
	private final HashMap<Point, Unit> unit_map;
	private final ArrayList<Tomb> tomb_list;

	public Map(short[][] map_data, String author) {
		this.map_data = map_data;
		this.author = author;
		this.unit_map = new HashMap();
		this.tomb_list = new ArrayList();
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
		Tomb tomb = new Tomb(x, y);
		if(!tomb_list.contains(tomb)) {
			tomb_list.add(tomb);
		}
	}
	
	public void removeTomb(Tomb tomb) {
		tomb_list.remove(tomb);
	}
	
	public ArrayList<Tomb> getTombList() {
		return tomb_list;
	}

	public void addUnit(Unit unit) {
		Point position = new Point(unit.getX(), unit.getY());
		unit_map.put(position, unit);
		unit_list = new ArrayList(unit_map.values());
	}
	
	public Unit getUnit(int x, int y) {
		return unit_map.get(new Point(x, y));
	}

	public void removeUnit(int x, int y) {
		unit_map.remove(new Point(x, y));
		unit_list = new ArrayList(unit_map.values());
	}
	
	public ArrayList<Unit> getUnitList() {
		return unit_list;
	}

}
