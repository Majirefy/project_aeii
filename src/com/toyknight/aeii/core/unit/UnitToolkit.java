package com.toyknight.aeii.core.unit;

import com.toyknight.aeii.core.BasicGame;
import com.toyknight.aeii.core.map.Tile;
import com.toyknight.aeii.core.map.TileEntitySet;
import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author toyknight
 */
public class UnitToolkit {

	private final BasicGame game;
	private ArrayList<Point> movable_positions;
	private final int[] x_dir = {1, 0, -1, 0};
	private final int[] y_dir = {0, 1, 0, -1};

	public UnitToolkit(BasicGame game) {
		this.game = game;
	}

	public ArrayList<Point> createMovablePositions(Unit unit) {
		movable_positions = new ArrayList();
		createMovablePositions(unit, unit.getX(), unit.getY(), unit.getCurrentMovementPoint());
		return movable_positions;
	}

	public ArrayList<Point> createMovePath(Unit unit, int dx, int dy, ArrayList<Point> movable_positions) {
		Point dest = new Point(dx, dy);
		if(movable_positions.contains(dest)) {
			ArrayList<Point> path = createMovePath(unit.getX(), unit.getY(), dx, dy, movable_positions, new ArrayList());
			if(path != null) {
				return path;
			} else {
				return new ArrayList();
			}
		} else {
			return new ArrayList();
		}
	}

	private ArrayList<Point> createMovePath(
			int cx, int cy, int dx, int dy,
			ArrayList<Point> movable_positions, ArrayList<Point> moved_positions) {
		ArrayList<Point> path = new ArrayList();
		ArrayList<Point> new_moved_positions = (ArrayList<Point>)moved_positions.clone();
		Point current_position = new Point(cx, cy);
		path.add(current_position);
		new_moved_positions.add(current_position);
		ArrayList<ArrayList<Point>> available_paths = new ArrayList();
		for (int i = 0; i < 4; i++) {
			int new_x = cx + x_dir[i];
			int new_y = cy + y_dir[i];
			Point new_position = new Point(new_x, new_y);
			if (game.getMap().isWithinMap(new_x, new_y) 
					&& movable_positions.contains(new_position)
					&& !moved_positions.contains(new_position)) {
				ArrayList<Point> new_path = createMovePath(new_x, new_y, dx, dy, movable_positions, new_moved_positions);
				if (new_path != null) {
					available_paths.add(new_path);
				}
			}
		}
		if (available_paths.isEmpty()) {
			if (cx == dx && cy == dy) {
				return path;
			} else {
				return null;
			}
		} else {
			int min_len = Integer.MAX_VALUE;
			int min_index = 0;
			for (int i = 0; i < available_paths.size(); i++) {
				if (available_paths.get(i).size() < min_len) {
					min_len = available_paths.get(i).size();
					min_index = i;
				}
			}
			path.addAll(available_paths.get(min_index));
			return path;
		}
	}

	private void createMovablePositions(Unit unit, int x, int y, int mp_left) {
		Point position = new Point(x, y);
		if (!movable_positions.contains(position) && game.getUnit(x, y) == null) {
			movable_positions.add(position);
		}
		if (mp_left <= 0) {
			return;
		}
		for (int i = 0; i < 4; i++) {
			int new_x = x + x_dir[i];
			int new_y = y + y_dir[i];
			if (game.getMap().isWithinMap(new_x, new_y)) {
				int tile_index = game.getMap().getTileIndex(new_x, new_y);
				Tile tile = TileEntitySet.getTile(tile_index);
				int mp_cost = getMovementPointCost(unit, tile);
				if (mp_cost <= mp_left) {
					Unit target_unit = game.getUnit(new_x, new_y);
					if (target_unit == null) {
						createMovablePositions(unit, new_x, new_y, mp_left - mp_cost);
					} else {
						if (target_unit.getTeam() == unit.getTeam()) {
							createMovablePositions(unit, new_x, new_y, mp_left - mp_cost);
						}
					}
				}
			}
		}
	}

	public int getMovementPointCost(Unit unit, Tile tile) {
		int mp_cost = tile.getStepCost();
		int tile_type = tile.getType();
		ArrayList<Integer> abilities = unit.getAbilities();
		if (abilities.contains(Ability.AIR_FORCE)) {
			mp_cost = 1;
		}
		if (abilities.contains(Ability.CRAWLER)
				&& (tile_type == Tile.TYPE_LAND || tile_type == Tile.TYPE_FOREST || tile_type == Tile.TYPE_MOUNTAIN)) {
			mp_cost = 1;
		}
		if (abilities.contains(Ability.FIGHTER_OF_THE_SEA) && tile_type == Tile.TYPE_WATER) {
			mp_cost = 1;
		}
		if (abilities.contains(Ability.FIGHTER_OF_THE_FOREST) && tile_type == Tile.TYPE_FOREST) {
			mp_cost = 1;
		}
		if (abilities.contains(Ability.FIGHTER_OF_THE_MOUNTAIN) && tile_type == Tile.TYPE_MOUNTAIN) {
			mp_cost = 1;
		}
		if (mp_cost < 1) {
			mp_cost = 1;
		}
		return mp_cost;
	}

}
