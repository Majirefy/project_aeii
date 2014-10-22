package com.toyknight.aeii.core.rule;

import com.toyknight.aeii.core.Point;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author toyknight
 */
public class Rule implements Serializable {

	private final boolean clean_enemy_units;
	private final boolean clean_enemy_castles;
	private final ArrayList<Integer>[] buyable_units;
	private final ArrayList<Point>[] winning_move_positions;
	private final ArrayList<Point>[] winning_occupy_positions;
	private final ArrayList<String>[] winning_targets;

	public Rule(
			boolean clean_enemy_units,
			boolean clean_enemy_castles,
			ArrayList<Integer>[] buyable_units,
			ArrayList<Point>[] winning_move_positions,
			ArrayList<Point>[] winning_occupy_positions,
			ArrayList<String>[] winning_targets) {
		this.clean_enemy_units = clean_enemy_units;
		this.clean_enemy_castles = clean_enemy_castles;
		this.buyable_units = buyable_units;
		this.winning_move_positions = winning_move_positions;
		this.winning_occupy_positions = winning_occupy_positions;
		this.winning_targets = winning_targets;
	}

	public boolean cleanEnemyUnits() {
		return clean_enemy_units;
	}

	public boolean cleanEnemyCastles() {
		return clean_enemy_castles;
	}
	
	public ArrayList<Integer> getBuyableUnits(int team) {
		return buyable_units[team];
	}
	
	public ArrayList<Point> getWinningMovePisitions(int team) {
		return winning_move_positions[team];
	}
	
	public ArrayList<Point> getWinningOccupyPositions(int team) {
		return winning_occupy_positions[team];
	}
	
	public ArrayList<String> getWinningTargets(int team) {
		return winning_targets[team];
	}
	
}
