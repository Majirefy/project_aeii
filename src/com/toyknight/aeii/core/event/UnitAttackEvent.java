package com.toyknight.aeii.core.event;

import com.toyknight.aeii.core.GameListener;
import com.toyknight.aeii.core.unit.Ability;
import com.toyknight.aeii.core.unit.Buff;
import com.toyknight.aeii.core.unit.Unit;

/**
 *
 * @author toyknight
 */
public class UnitAttackEvent implements GameEvent {

	private final int damage;
	private final Unit attacker;
	private final Unit defender;

	public UnitAttackEvent(Unit attacker, Unit defender, int damage) {
		this.damage = damage;
		this.attacker = attacker;
		this.defender = defender;
	}

	@Override
	public void execute(GameListener listener) {
		defender.setCurrentHp(defender.getCurrentHp() - damage);
		//deal with buff issues
		if (attacker.hasAbility(Ability.POISONER)) {
			defender.attachBuff(new Buff(Buff.POISONED, 2));
		}
		listener.onUnitAttack(attacker, defender, damage);
	}

}
