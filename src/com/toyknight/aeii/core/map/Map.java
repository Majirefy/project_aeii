package com.toyknight.aeii.core.map;

import com.toyknight.aeii.core.Point;
import com.toyknight.aeii.core.unit.Unit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author toyknight
 */
public class Map {

	private final String author;

	private final short[][] map_data;
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
		if (isWithinMap(x, y)) {
			return map_data[x][y];
		} else {
			return -1;
		}
	}

	public void addTomb(int x, int y) {
		Tomb tomb = new Tomb(x, y);
		if (!tomb_list.contains(tomb)) {
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
		if (!unit_map.containsKey(position)) {
			unit_map.put(position, unit);
		}
	}

	public Unit getUnit(int x, int y) {
		return unit_map.get(new Point(x, y));
	}

	public void removeUnit(int x, int y) {
		unit_map.remove(new Point(x, y));
	}

	public Collection<Unit> getUnitSet() {
		return unit_map.values();
	}

	public Set<Point> getUnitPositionSet() {
		return unit_map.keySet();
	}

}
