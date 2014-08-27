package com.toyknight.aeii.core.unit;

import com.toyknight.aeii.core.BasicGame;
import com.toyknight.aeii.core.map.Tile;
import com.toyknight.aeii.core.map.TileEntitySet;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author toyknight
 */
public class UnitToolkit {

	private final BasicGame game;
	private HashSet<Point> movable_positions;
	private ArrayList<Point> move_path;
	private int[][] move_mark_map;
	private final int[] x_dir = {1, -1, 0, 0};
	private final int[] y_dir = {0, 0, 1, -1};

	public UnitToolkit(BasicGame game) {
		this.game = game;

	}

	private void createMoveMarkMap() {
		int width = game.getMap().getWidth();
		int height = game.getMap().getHeight();
		move_mark_map = new int[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				move_mark_map[x][y] = Integer.MIN_VALUE;
			}
		}
	}

	public ArrayList<Point> createMovablePositions(Unit unit) {
		createMoveMarkMap();
		movable_positions = new HashSet();
		Point start_position = new Point(unit.getX(), unit.getY());
		Step start_step = new Step(start_position, unit.getCurrentMovementPoint());
		Queue<Step> start_steps = new LinkedList();
		start_steps.add(start_step);
		createMovablePisitions(start_steps, unit);
		return new ArrayList(movable_positions);
	}

	private void createMovablePisitions(Queue<Step> current_steps, Unit unit) {
		Queue<Step> next_steps = new LinkedList();
		while (!current_steps.isEmpty()) {
			Step current_step = current_steps.poll();
			int step_x = current_step.getPosition().x;
			int step_y = current_step.getPosition().y;
			if (game.getMap().getUnit(step_x, step_y) == null) {
				movable_positions.add(current_step.getPosition());
			}
			for (int i = 0; i < 4; i++) {
				int next_x = current_step.getPosition().x + x_dir[i];
				int next_y = current_step.getPosition().y + y_dir[i];
				Point next = new Point(next_x, next_y);
				int current_mp = current_step.getMp();
				if (game.getMap().isWithinMap(next_x, next_y)) {
					int mp_cost = getMovementPointCost(unit, next_x, next_y);
					if (current_mp - mp_cost > move_mark_map[next_x][next_y]) {
						if (mp_cost <= current_mp) {
							Unit target_unit = game.getMap().getUnit(next_x, next_y);
							if (target_unit == null) {
								Step next_step = new Step(next, current_mp - mp_cost);
								move_mark_map[next_x][next_y] = current_mp - mp_cost;
								next_steps.add(next_step);
							} else {
								if (target_unit.getTeam() == unit.getTeam()) {
									Step next_step = new Step(next, current_mp - mp_cost);
									move_mark_map[next_x][next_y] = current_mp - mp_cost;
									next_steps.add(next_step);
								}
							}
						}
					}
				}
			}
		}
		if (!next_steps.isEmpty()) {
			createMovablePisitions(next_steps, unit);
		}
	}

	public ArrayList<Point> createMovePath(Unit unit, int dest_x, int dest_y) {
		Point dest_position = new Point(dest_x, dest_y);
		if (movable_positions.contains(dest_position)) {
			move_path = new ArrayList();
			int current_x = dest_x;
			int current_y = dest_y;
			int start_x = unit.getX();
			int start_y = unit.getY();
			createMovePath(current_x, current_y, start_x, start_y);
			return move_path;
		} else {
			return new ArrayList();
		}

	}

	private void createMovePath(int current_x, int current_y, int start_x, int start_y) {
		move_path.add(0, new Point(current_x, current_y));
		if (current_x != start_x || current_y != start_y) {
			int next_x = 0;
			int next_y = 0;
			int next_mark = Integer.MIN_VALUE;
			for (int i = 0; i < 4; i++) {
				int tmp_next_x = current_x + x_dir[i];
				int tmp_next_y = current_y + y_dir[i];
				if (game.getMap().isWithinMap(tmp_next_x, tmp_next_y)) {
					if (tmp_next_x == start_x && tmp_next_y == start_y) {
						next_x = tmp_next_x;
						next_y = tmp_next_y;
						next_mark = Integer.MAX_VALUE;
					} else {
						int tmp_next_mark = move_mark_map[tmp_next_x][tmp_next_y];
						if (tmp_next_mark > next_mark) {
							next_x = tmp_next_x;
							next_y = tmp_next_y;
							next_mark = tmp_next_mark;
						}
					}
				}
			}
			createMovePath(next_x, next_y, start_x, start_y);
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

}
