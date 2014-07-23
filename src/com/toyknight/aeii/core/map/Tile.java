
package com.toyknight.aeii.core.map;

/**
 *
 * @author toyknight
 */
public class Tile {
	
	public static final byte TYPE_LAND = 0;
	public static final byte TYPE_WATER = 1;
	public static final byte TYPE_FOREST = 2;
	public static final byte TYPE_MOUNTAIN = 3;
	
	private int defence_bonus;
	private int step_cost;
	private int hp_recovery = 0;
	private int type;
	private int top_tile_index = -1;
	
	private int team = -1;
	
	private int[] access_tile_list = null;
	
	private boolean can_be_captured = false;
	private boolean can_be_destroyed = false;
	private boolean can_be_repaired = false;
	
	private int[] captured_tile_list;
	private int destroyed_index;
	private int repaired_index;
	
	public Tile(int defence_bonus, int step_cost, int type) {
		this.defence_bonus = defence_bonus;
		this.step_cost = step_cost;
		this.type = type;
	}
	
	public void setDefenceBonus(int bonus) {
		this.defence_bonus = bonus;
	}
	
	public int getDefenceBonus() {
		return defence_bonus;
	}
	
	public void setStepCost(int cost) {
		this.step_cost = cost;
	}
	
	public int getStepCost() {
		return step_cost;
	}
	
	public void setHpRecovery(int recovery) {
		this.hp_recovery = recovery;
	}
	
	public int getHpRecovery() {
		return hp_recovery;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public int getType() {
		return type;
	}
	
	public void setTopTileIndex(int index) {
		this.top_tile_index = index;
	}
	
	public int getTopTileIndex() {
		return top_tile_index;
	}
	
	public void setTeam(int team) {
		this.team = team;
	}
	
	public int getTeam() {
		return team;
	}
	
	public void setAccessTileList(int[] index) {
		this.access_tile_list = index;
	}
	
	public int[] getAccessTileList() {
		return access_tile_list;
	}
	
	public void setCapturable(boolean b) {
		this.can_be_captured = b;
	}
	
	public boolean isCapturable() {
		return can_be_captured;
	}
	
	public void setDestroyable(boolean b) {
		this.can_be_destroyed = b;
	}
	
	public boolean isDestroyable() {
		return can_be_destroyed;
	}
	
	public void setRepairable(boolean b) {
		this.can_be_repaired = b;
	}
	
	public boolean isRepairable() {
		return can_be_repaired;
	}
	
	public void setCapturedTileList(int[] list) {
		this.captured_tile_list = list;
	}
	
	public int getCapturedTileIndex(int team) {
		return captured_tile_list[team];
	}
	
	public void setDestroyedTileIndex(int index) {
		this.destroyed_index = index;
	}
	
	public int getDestroyedTileIndex() {
		return destroyed_index;
	}
	
	public void setRepairedTileIndex(int index) {
		this.repaired_index = index;
	}
	
	public int getRepairedTileIndex() {
		return repaired_index;
	}
	
}
