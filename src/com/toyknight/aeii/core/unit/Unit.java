
package com.toyknight.aeii.core.unit;

/**
 *
 * @author toyknight
 */
public class Unit {
   
    private int type;
    private String unit_code;
    private int team;
    private int max_hp;
    private int current_hp;
    private int attack;
    private int defence;
    private int movement_point;
//  private ArrayList[] abilities[];
//  private ArrayList[] spells[];
    private int state;
    private int hp_growth;
    private int attack_growth;
    private int defence_growth;
  //  private int movement_growth;
    private int x_position;
    private int y_position;
     
    public int getType() {
        return type;
    }
    
    public void setType(int type) {
      
      this.type = type;
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
    
      public int getDefence() {
        return defence;
    }
    
    public void setDefence(int defence) {
        this.defence = defence;
    }
    
      public int getMovementPoint() {
        return movement_point;
    }
    
    public void setMovementPoint(int movement_point) {
        this.movement_point = movement_point;
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
	
	public void setDefenceGrowth(int defence_growth) {
        this.defence_growth = defence_growth;
    }
    
    public int getDefenceGrowth() {
        return defence_growth;
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
	
}
