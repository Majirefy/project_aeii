
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
    
      public String getUnit_code() {
        return unit_code;
    }
    
    public void setUnit_code(String unit_code) {
        this.unit_code = unit_code;
    }
    
      public int getTeam() {
        return team;
    }
    
    public void setTeam(int team) {
        this.team = team;
    }
    
      public int getMax_hp() {
        return max_hp;
    }
    
    public void setMax_hp(int max_hp) {
        this.max_hp = max_hp;
    }
    
      public int getCurrent_hp() {
        return current_hp;
    }
    
    public void setCurrent_hp(int current_hp) {
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
    
      public int getMovement_point() {
        return movement_point;
    }
    
    public void setMovement_point(int movement_point) {
        this.movement_point = movement_point;
    }
    
      public int getState() {
        return state;
    }
    
    public void setState(int state) {
        this.state = state;
    }
    
      public int getHp_growth() {
        return hp_growth;
    }
    
    public void setAttack_growth(int attack_growth) {
        this.attack_growth = attack_growth;
    }
    
      public int getDefence_growth() {
        return defence_growth;
    }
    
    public void setDefence_growth(int defence_growth) {
        this.defence_growth = defence_growth;
    }
    
      public int getX_position() {
        return x_position;
    }
    
    public void setX_position(int x_position) {
        this.x_position = x_position;
    }
    
      public int getY_position() {
        return y_position;
    }
    
    public void setY_position(int y_position) {
        this.y_position = y_position;
    } 
	
}
