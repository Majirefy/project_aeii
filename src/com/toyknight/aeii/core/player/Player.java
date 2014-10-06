package com.toyknight.aeii.core.player;

/**
 *
 * @author toyknight
 */
public abstract class Player {
	
	private int alliance;
	private int gold = 0;
	private int population = 0;
	
	public void setPopulation(int population) {
		this.population = population;
	}
	
	public int getPopulation() {
		return population;
	}
	
	public void setGold(int gold) {
		this.gold = gold;
	}
	
	public int getGold() {
		return gold;
	}

	public void setAlliance(int alliance) {
		this.alliance = alliance;
	}

	public int getAlliance() {
		return alliance;
	}

}
