package com.toyknight.aeii.core.map;

import com.toyknight.aeii.core.Point;
import com.toyknight.aeii.core.unit.Unit;
import com.toyknight.aeii.core.unit.UnitToolkit;
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

	private Unit new_unit;
	private final short[][] map_data;
	private final HashMap<Point, Unit> unit_map;
	private final ArrayList<Tomb> tomb_list;
	private final Point[][] position_map;

	public Map(short[][] map_data, String author) {
		this.author = author;
		new_unit = null;
		this.map_data = map_data;
		this.unit_map = new HashMap();
		this.tomb_list = new ArrayList();
		position_map = new Point[getWidth()][getHeight()];
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				position_map[x][y] = new Point(x, y);
			}
		}
	}

	public String getAuthor() {
		return author;
	}

	public boolean isWithinMap(int x, int y) {
		return 0 <= x && x < getWidth() && 0 <= y && y < getHeight();
	}

	public final int getWidth() {
		return map_data.length;
	}

	public final int getHeight() {
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

	public void moveUnit(Unit unit, int dest_x, int dest_y) {
		int start_x = unit.getX();
		int start_y = unit.getY();
		Point start_position = getPosition(start_x, start_y);
		if (UnitToolkit.isTheSameUnit(unit, unit_map.get(start_position))) {
			unit_map.remove(start_position);
		}
		if (getUnit(dest_x, dest_y) == null) {
			unit.setX(dest_x);
			unit.setY(dest_y);
			Point dest_position = getPosition(dest_x, dest_y);
			unit_map.put(dest_position, unit);
			if (UnitToolkit.isTheSameUnit(unit, new_unit)) {
				new_unit = null;
			}
		}
	}

	public void addUnit(Unit unit) {
		Point position = getPosition(unit.getX(), unit.getY());
		if (!unit_map.containsKey(position)) {
			unit_map.put(position, unit);
		} else {
			if (new_unit == null) {
				new_unit = unit;
			}
		}
	}

	public Unit getUnit(int x, int y) {
		if (new_unit != null && new_unit.isAt(x, y)) {
			return new_unit;
		} else {
			return unit_map.get(getPosition(x, y));
		}
	}

	public void removeUnit(int x, int y) {
		unit_map.remove(getPosition(x, y));
	}

	public Collection<Unit> getUnitSet() {
		return unit_map.values();
	}

	public Set<Point> getUnitPositionSet() {
		return unit_map.keySet();
	}

	public int getUnitCount(int team) {
		Collection<Unit> units = getUnitSet();
		int count = 0;
		for (Unit unit : units) {
			if (unit.getTeam() == team) {
				count++;
			}
		}
		if (new_unit != null && new_unit.getTeam() == team) {
			count++;
		}
		return count;
	}

	private Point getPosition(int x, int y) {
		return position_map[x][y];
	}

}
