package com.toyknight.aeii.core.unit;

import com.toyknight.aeii.core.BasicGame;
import com.toyknight.aeii.core.map.Tile;
import com.toyknight.aeii.core.map.TileEntitySet;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 *
 * @author toyknight
 */
public class UnitToolkit {

	private final BasicGame game;
	private Set<Point> movable_positions;
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
		Step start_step = new Step(start_position, unit.getCurrentMovementPoint());
		Queue<Step> start_list = new LinkedList();
		start_list.add(start_step);
		createMovablePisitions(start_list, unit);
		return new ArrayList(movable_positions);
	}

	private void createMovablePisitions(Queue<Step> parent, Unit unit) {
		Queue<Step> children = new LinkedList();
		while (!parent.isEmpty()) {
			Step current_step = parent.poll();
			if(game.getMap().getUnit(current_step.getPosition().x, current_step.getPosition().y) == null) {
				movable_positions.add(current_step.getPosition());
			}
			for (int i = 0; i < 4; i++) {
				int next_x = current_step.getPosition().x + x_dir[i];
				int next_y = current_step.getPosition().y + y_dir[i];
				Point next = new Point(next_x, next_y);
				if (game.getMap().isWithinMap(next_x, next_y) && !movable_positions.contains(next)) {
					int mp = current_step.getMp();
					int mp_cost = getMovementPointCost(unit, next_x, next_y);
					if (mp_cost <= mp) {
						Unit target_unit = game.getMap().getUnit(next_x, next_y);
						if (target_unit == null) {
							Step next_step = new Step(next, mp - mp_cost);
							children.add(next_step);
						} else {
							if (target_unit.getTeam() == unit.getTeam()) {
								Step next_step = new Step(next, mp - mp_cost);
								children.add(next_step);
							}
						}
					}
				}
			}
		}
		if (!children.isEmpty()) {
			createMovablePisitions(children, unit);
		}
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

	private class Step {

		private final int mp;
		private final Point position;

		public Step(Point position, int mp) {
			this.mp = mp;
			this.position = position;
		}

		public Point getPosition() {
			return position;
		}

		public int getMp() {
			return mp;
		}

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
