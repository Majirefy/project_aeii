package com.toyknight.aeii.core.unit;

import java.util.ArrayList;

/**
 *
 * @author xiaosi
 */
public class Unit {

	public static final int ATTACK_PHYSICAL = 0;
	public static final int ATTACK_MAGICAL = 1;

	private final int index;

	private int price;

	private int level;     //等级（初始等级为0，最高可以升级到3级）

	private String unit_code;
	private int team;

	private int max_hp;
	private int current_hp;
	private int attack;
	private int attack_type;  //攻击类型整型（0：表示为物理攻击，1：表示魔法攻击）      
	private int physical_defence;
	private int magical_defence;
	private int movement_point;
	private int current_movement_point;

	private ArrayList<Integer> abilities;
	private ArrayList<Integer> learnable_abilities;
	private ArrayList<Buff> buff_list;

	private int hp_growth;
	private int attack_growth;
	private int physical_defence_growth;
	private int magical_defence_growth;
	private int movement_growth;

	private int x_position;
	private int y_position;

	private int max_attack_range;  //最大攻击距离
	private int min_attack_range;   //最小攻击距离

	private boolean is_standby;

	public Unit(int index) {
		this.index = index;
		this.level = 0;
		abilities = new ArrayList();
		learnable_abilities = new ArrayList();
		buff_list = new ArrayList();
	}

	public Unit(Unit unit) {
		this.abilities = new ArrayList(unit.getAbilities());
		this.buff_list = new ArrayList(unit.getBuffList());
		this.learnable_abilities = new ArrayList(unit.getLearnableAbilities());
		this.index = unit.getIndex();
		this.price = unit.getPrice();
		this.unit_code = unit.unit_code;
		this.team = unit.team;
		this.max_hp = unit.max_hp;
		this.current_hp = unit.current_hp;
		this.attack = unit.attack;
		this.attack_type = unit.attack_type;
		this.physical_defence = unit.physical_defence;
		this.magical_defence = unit.magical_defence;
		this.movement_point = unit.movement_point;
		this.current_movement_point = unit.current_movement_point;
		this.hp_growth = unit.hp_growth;
		this.attack_growth = unit.attack_growth;
		this.physical_defence_growth = unit.physical_defence_growth;
		this.magical_defence_growth = unit.magical_defence_growth;
		this.movement_growth = unit.movement_growth;
		this.x_position = unit.x_position;
		this.y_position = unit.y_position;
		this.max_attack_range = unit.max_attack_range;
		this.min_attack_range = unit.min_attack_range;
	}

	public int getIndex() {
		return index;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getUnitCode() {
		return unit_code;
	}

	public void setUnitCode(String unit_code) {
		this.unit_code = unit_code;
	}

	public int getTeam() {
		return team;
	}

	public void setTeam(int team) {
		this.team = team;
	}

	public int getMaxHp() {
		return max_hp;
	}

	public void setMaxHp(int max_hp) {
		this.max_hp = max_hp;
	}

	public int getCurrentHp() {
		return current_hp;
	}

	public void setCurrentHp(int current_hp) {
		this.current_hp = current_hp;
	}

	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public int getAttackType() {
		return attack_type;
	}

	public void setAttackType(int attack_type) {
		this.attack_type = attack_type;
	}

	public int getPhysicalDefence() {
		return physical_defence;
	}

	public void setPhysicalDefence(int physical_defence) {
		this.physical_defence = physical_defence;
	}

	public int getMagicalDefence() {
		return magical_defence;
	}

	public void setMagicalDefence(int magical_defence) {
		this.magical_defence = magical_defence;
	}

	public int getMovementGrowth() {
		return movement_growth;
	}

	public void setMovementGrowth(int movement_growth) {
		this.movement_growth = movement_growth;
	}

	public int getMovementPoint() {
		return movement_point;
	}

	public void setMovementPoint(int movement_point) {
		this.movement_point = movement_point;
	}

	public int getCurrentMovementPoint() {
		return current_movement_point;
	}

	public void setCurrentMovementPoint(int current_movement_point) {
		this.current_movement_point = current_movement_point;
	}
	
	public boolean hasAbility(int ability) {
		return abilities.contains(ability);
	}

	public ArrayList<Integer> getAbilities() {
		return abilities;
	}

	public void setAbilities(ArrayList<Integer> abilities) {
		this.abilities = abilities;
	}

	public ArrayList<Integer> getLearnableAbilities() {
		return learnable_abilities;
	}

	public void setLearnableAbilities(ArrayList<Integer> learnable_abilities) {
		this.learnable_abilities = learnable_abilities;
	}

	public ArrayList<Buff> getBuffList() {
		return buff_list;
	}

	public void setBuffList(ArrayList<Buff> buff_list) {
		this.buff_list = buff_list;
	}

	public int getHpGrowth() {
		return hp_growth;
	}

	public void setHpGrowth(int growth) {
		this.hp_growth = growth;
	}

	public void setAttackGrowth(int attack_growth) {
		this.attack_growth = attack_growth;
	}

	public int getAttackGrowth() {
		return attack_growth;
	}

	public int getPhysicalDefenceGrowth() {
		return physical_defence_growth;
	}

	public void setPhysicalDefenceGrowth(int physical_defence_growth) {
		this.physical_defence_growth = physical_defence_growth;
	}

	public int getMagicalDefenceGrowth() {
		return magical_defence_growth;
	}

	public void setMagicalDefenceGrowth(int magical_defence_growth) {
		this.magical_defence_growth = magical_defence_growth;
	}

	public int getX() {
		return x_position;
	}

	public void setX(int x_position) {
		this.x_position = x_position;
	}

	public int getY() {
		return y_position;
	}

	public void setY(int y_position) {
		this.y_position = y_position;
	}

	public int getMaxAttackRange() {
		return max_attack_range;
	}

	public void setMaxAttackRange(int max_attack_range) {
		this.max_attack_range = max_attack_range;
	}

	public int getMinAttackRange() {
		return min_attack_range;
	}

	public void setMinAttackRange(int min_attack_range) {
		this.min_attack_range = min_attack_range;
	}

	public void setStandby(boolean b) {
		this.is_standby = b;
	}

	public boolean isStandby() {
		return is_standby;
	}

	public void levelUp() {
		if (level < 3) {
			level++;
			this.attack += this.getAttackGrowth();
			this.max_hp += this.getHpGrowth();
			this.movement_point += this.getMovementGrowth();
			this.physical_defence += this.getPhysicalDefenceGrowth();
			this.magical_defence += this.getMagicalDefenceGrowth();
		}
	}

	public void learnAbility(int ability) {
		abilities.add(ability);
	}

	public void attachBuff(Buff buff) {
		if (buff_list.size() < 2) {
			buff_list.add(buff);
		}
	}

	public void updateBuff() {
		for (Buff buff : buff_list) {
			if (buff.getRemainingTurn() < 0) {
				buff_list.remove(buff);
			} else {
				buff.update();
			}
		}
	}
	
	public boolean isAt(int x, int y) {
		return this.x_position == x && this.y_position == y;
	}

}
