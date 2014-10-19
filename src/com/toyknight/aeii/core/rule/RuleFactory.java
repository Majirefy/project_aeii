
package com.toyknight.aeii.core.rule;

/**
 *
 * @author toyknight
 */
public class RuleFactory {
	
	private static Rule skirmish_rule;
	
	private RuleFactory() {
	}
	
	public static void init() {
		
	}
	
	public static Rule getSkirmishRule() {
		return skirmish_rule;
	}
	
}
