package com.toyknight.aeii.core.unit;

import com.toyknight.aeii.core.BasicGame;
import com.toyknight.aeii.core.map.Tile;
import com.toyknight.aeii.core.map.TileEntitySet;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author toyknight
 */
public class UnitToolkit {

	private final BasicGame game;
	private HashSet<Point> movable_positions;
	private ArrayList<Point> move_path;
	private final int[] x_dir = {1, -1, 0, 0};
	private final int[] y_dir = {0, 0, 1, -1};

	private PathNode shortest_path_node;

	public UnitToolkit(BasicGame game) {
		this.game = game;
	}

	public ArrayList<Point> createMovablePositions(Unit unit) {
		movable_positions = new HashSet();
		Point start_position = new Point(unit.getX(), unit.getY());
		createMovablePositions(unit, start_position, unit.getCurrentMovementPoint());
		return new ArrayList(movable_positions);
	}

	public ArrayList<Point> createMovePath(Unit unit, int dx, int dy, ArrayList<Point> movable_positions) {
		Point dest_position = new Point(dx, dy);
		if (!movable_positions.contains(dest_position)) {
			return new ArrayList();
		}
		shortest_path_node = null;
		HashSet<Point> movable_set = new HashSet(movable_positions);
		Point start_position = new Point(unit.getX(), unit.getY());
		createShortestPathNode(start_position, dest_position, null, movable_set);
		if (shortest_path_node != null) {
			move_path = new ArrayList();
			createMovePath(shortest_path_node);
			return move_path;
		} else {
			return new ArrayList();
		}
	}

	private void createShortestPathNode(Point current, Point dest, PathNode parent, HashSet<Point> movable) {
		PathNode current_node;
		if (parent != null) {
			current_node = new PathNode(current, parent, parent.getDepth() + 1);
		} else {
			current_node = new PathNode(current, parent, 0);
		}
		if (current.equals(dest)) {
			if (shortest_path_node == null) {
				shortest_path_node = current_node;
			} else {
				if (current_node.getDepth() < shortest_path_node.getDepth()) {
					shortest_path_node = current_node;
				}
			}
		} else {
			for (int i = 0; i < 4; i++) {
				int next_x = current.x + x_dir[i];
				int next_y = current.y + y_dir[i];
				if (game.getMap().isWithinMap(next_x, next_y)) {
					Point next = new Point(next_x, next_y);
					if (parent != null) {
						if (movable.contains(next) && !isMovedPosition(parent, next)) {
							createShortestPathNode(next, dest, current_node, movable);
						}
					} else {
						if (movable.contains(next)) {
							createShortestPathNode(next, dest, current_node, movable);
						}
					}

				}
			}
		}
	}

	private void createMovePath(PathNode node) {
		move_path.add(0, node.getPosition());
		PathNode parent = node.getParent();
		if (parent != null) {
			createMovePath(parent);
		}
	}

	private boolean isMovedPosition(PathNode node, Point position) {
		if (node != null) {
			if (node.getPosition().equals(position)) {
				return true;
			} else {
				return isMovedPosition(node.getParent(), position);
			}
		} else {
			return false;
		}
	}

	private void createMovablePositions(Unit unit, Point current, int mp) {
		if(game.getMap().getUnit(current.x, current.y) == null) {
			movable_positions.add(current);
		}
		if (mp <= 0) {
			return;
		}
		for (int i = 0; i < 4; i++) {
			int next_x = current.x + x_dir[i];
			int next_y = current.y + y_dir[i];
			Point next = new Point(next_x, next_y);
			if (game.getMap().isWithinMap(next_x, next_y)) {
				int mp_cost = getMovementPointCost(unit, next_x, next_y);
				if (mp_cost <= mp) {
					Unit target_unit = game.getMap().getUnit(next_x, next_y);
					if (target_unit == null) {
						createMovablePositions(unit, next, mp - mp_cost);
					} else {
						if (target_unit.getTeam() == unit.getTeam()) {
							createMovablePositions(unit, next, mp - mp_cost);
						}
					}
				}
			}
		}
	}

	public int getMovementPointCost(Unit unit, int x, int y) {
		int tile_index = game.getMap().getTileIndex(x, y);
		Tile tile = TileEntitySet.getTile(tile_index);
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

	private class PathNode {

		private final PathNode parent;
		private final ArrayList<PathNode> childs;

		private final int depth;
		private final Point position;

		public PathNode(Point position, PathNode parent, int depth) {
			this.parent = parent;
			this.position = position;
			childs = new ArrayList();
			this.depth = depth;
		}

		public int getDepth() {
			return depth;
		}

		public Point getPosition() {
			return position;
		}

		public PathNode getParent() {
			return parent;
		}

		public void appendChild(PathNode node) {
			childs.add(node);
		}

		public ArrayList<PathNode> getChilds() {
			return childs;
		}

	}

}
