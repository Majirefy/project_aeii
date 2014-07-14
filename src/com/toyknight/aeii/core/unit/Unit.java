
package com.toyknight.aeii.core.unit;

import java.util.ArrayList;

/**
 *
 * @author toyknight
 */
public class Unit {
   
    private int type;
    private int grade;     //等级（初始等级为0，最高可以升级到3级）
    private String unit_code;
    private int team;
    private int max_hp;
    private int current_hp;
    private int attack;
    private int attack_type;  //攻击类型整型（0：表示为物理攻击，1：表示魔法攻击）
    private int physical_defence;
    private int magic_defence;
    private int movement_point;
    private ArrayList<Integer> abilities;
    private int state;
    private int hp_growth;
    private int attack_growth;
    private int physical_defence_growth;
    private int magic_defence_growth;
    private int movement_growth;
    private int x_position;
    private int y_position;
    private Unit unit;         //得到自身变量
    private int max_spell_range;  //最大施法距离
    private int min_spell_range;   //最小施法距离
    
    public Unit getUnit () {
        return unit;
    }
    
    public void setUnit(Unit unit) {
        this.unit = unit;
    }
     
    public int getType() {
        return type;
    }
    
    public void setType(int type) {
      
      this.type = type;
    }
    
    public int getGrade() {
        if(grade == 3) {
            this.getAbilities();
        }
        return grade;
    }
    
    public void setGrade(int grade) {
        this.grade = grade;
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
    
    public void  setAttackType(int attack_type) {
        this.attack_type = attack_type;
    }
      public int getPhysicalDefence() {
        return physical_defence;
    }
    
    public void setPhysicalDefence(int physical_defence) {
        this.physical_defence = physical_defence;
    }
    
    public int getMagicDefence() {
        return magic_defence;
    }
    
    public void setMagicDefence(int magic_defence) {
        this.magic_defence = magic_defence;
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
    
    public ArrayList<Integer> getAbilities() {
        return abilities;
    }
    
    public void setAbilities(ArrayList<Integer> abilities) {
        this.abilities = abilities;
    }
    
      public int getState() {
        return state;
    }
    
    public void setState(int state) {
        this.state = state;
    }
    
      public int getHpGrowth() {
        return hp_growth;
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
    
    public int getMagicDefenceGrowth() {
        return magic_defence_growth;
    }
    
    public void setMagicDefenceGrowth(int magic_defence_growth) {
        this.magic_defence_growth = magic_defence_growth;
    }
    
    public int getXPosition() {
        return x_position;
    }
    
    public void setXPosition(int x_position) {
        this.x_position = x_position;
    }
    
      public int getYPosition() {
        return y_position;
    }
    
    public void setYPosition(int y_position) {
        this.y_position = y_position;
    } 

    public int getMaxSpellRange() {
        return max_spell_range;
    }
    
    public void setMaxSpellRange(int max_spell_range) {
        this.max_spell_range = max_spell_range;
    }
    
    public int getMinSpellRange() {
        return min_spell_range;
    }
    
    public void setMinSpellRange(int min_spell_range) {
        this.min_spell_range = min_spell_range;
    }
}
