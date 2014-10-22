
package com.toyknight.aeii.core;

import com.toyknight.aeii.core.map.Map;
import com.toyknight.aeii.core.player.Player;
import com.toyknight.aeii.core.rule.Rule;
import com.toyknight.aeii.core.unit.Unit;

/**
 *
 * @author toyknight
 */
public class CampaignGame extends SkirmishGame {
	
	private final Rule rule;

	public CampaignGame(Map map, Player[] players, Rule rule, int max_population) {
		super(map, players, max_population);
		this.rule = rule;
	}
	
	@Override
	public void onUnitMoved(Unit unit, int dest_x, int dest_y) {
		
	}
	
	@Override
	public void onUnitDestroyed(Unit unit) {
		
	}
	
	@Override
	public void onOccupy(int x, int y) {
		
	}
	
}
