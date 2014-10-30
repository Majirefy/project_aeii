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
	private final boolean is_commander;

	private int price;

	private int level;     //等级（初始等级为0，最高可以升级到3级）
	private int experience = 0;
	private final int[] level_up_experience = {100, 150, 300};

	private final String unit_code;
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

	//private final Object BUFF_LOCK = new Object();
	public Unit(int index, boolean commander, String unit_code) {
		this.level = 0;
		this.index = index;
		this.is_commander = commander;
		this.abilities = new ArrayList();
		this.buff_list = new ArrayList();
		this.unit_code = unit_code;
	}

	public Unit(int index, boolean commander) {
		this(index, commander, "#");
	}

	public Unit(Unit unit, String unit_code) {
		this(unit.getIndex(), unit.isCommander(), unit_code);
		this.level = unit.getLevel();
		this.experience = unit.getTotalExperience();
		this.price = unit.getPrice();
		this.team = unit.getTeam();
		this.max_hp = unit.getMaxHp();
		this.current_hp = unit.getCurrentHp();
		this.attack = unit.getAttack();
		this.attack_type = unit.getAttackType();
		this.physical_defence = unit.getPhysicalDefence();
		this.magical_defence = unit.getMagicalDefence();
		this.movement_point = unit.getMovementPoint();
		this.current_movement_point = unit.getCurrentMovementPoint();
		this.hp_growth = unit.getHpGrowth();
		this.attack_growth = unit.getAttackGrowth();
		this.physical_defence_growth = unit.getPhysicalDefenceGrowth();
		this.magical_defence_growth = unit.getMovementGrowth();
		this.movement_growth = unit.getMovementGrowth();
		this.x_position = unit.getX();
		this.y_position = unit.getY();
		this.max_attack_range = unit.getMaxAttackRange();
		this.min_attack_range = unit.getMinAttackRange();
		this.abilities = new ArrayList(unit.getAbilities());
		this.buff_list = new ArrayList(unit.getBuffList());
	}

	public int getIndex() {
		return index;
	}

	public boolean isCommander() {
		return is_commander;
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

	private ArrayList<Integer> getAbilities() {
		return abilities;
	}

	public void setAbilities(ArrayList<Integer> abilities) {
		this.abilities = abilities;
	}

	public int getBuffCount() {
		return buff_list.size();
	}

	public Buff getBuff(int index) {
		if (0 <= index && index <= buff_list.size()) {
			return buff_list.get(index);
		} else {
			return null;
		}
	}

	public ArrayList<Buff> getBuffList() {
		return buff_list;
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

	protected void levelUp() {
		if (level < 3) {
			level++;
			this.attack += this.getAttackGrowth();
			this.max_hp += this.getHpGrowth();
			this.current_hp += this.getHpGrowth();
			this.movement_point += this.getMovementGrowth();
			this.physical_defence += this.getPhysicalDefenceGrowth();
			this.magical_defence += this.getMagicalDefenceGrowth();
		}
	}

	/**
	 * 
	 * @param exp
	 * @return returns if unit level is up after gaining exp
	 */
	public boolean gainExperience(int exp) {
		if (level < 3) {
			boolean level_up_flag = false;
			if (getLevelUpExperience() - getCurrentExperience() <= exp) {
				level_up_flag = true;
				levelUp();
			}
			experience += exp;
			return level_up_flag;
		} else {
			return false;
		}
	}

	public int getTotalExperience() {
		return experience;
	}

	public int getCurrentExperience() {
		int exp = experience;
		for (int i = 0; i < level; i++) {
			exp -= level_up_experience[i];
		}
		return exp;
	}

	public int getLevelUpExperience() {
		if (0 <= level && level < 3) {
			return level_up_experience[level];
		} else {
			return -1;
		}
	}

	public void learnAbility(int ability) {
		abilities.add(ability);
	}

	public void attachBuff(Buff buff) {
		if (buff_list.size() < 2 && !buff_list.contains(buff)) {
			buff_list.add(buff);
		}
	}

	public void updateBuff() {
		ArrayList<Buff> tmp_buff_list = new ArrayList(buff_list);
		for (Buff buff : tmp_buff_list) {
			buff.update();
			if (buff.getRemainingTurn() < 0) {
				buff_list.remove(buff);
			}
		}
	}

	public boolean hasBuff(int type) {
		for (Buff buff : buff_list) {
			if (buff.getType() == type) {
				return true;
			}
		}
		return false;
	}

	public boolean isAt(int x, int y) {
		return this.x_position == x && this.y_position == y;
	}

}
