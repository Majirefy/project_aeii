package com.toyknight.aeii.core.event;

import com.toyknight.aeii.core.Game;
import com.toyknight.aeii.core.GameListener;
import com.toyknight.aeii.core.unit.Ability;
import com.toyknight.aeii.core.unit.Buff;
import com.toyknight.aeii.core.unit.Unit;
import com.toyknight.aeii.core.unit.UnitToolkit;

/**
 *
 * @author toyknight
 */
public class UnitAttackEvent implements GameEvent {

	private final Game game;
	private final Unit attacker;
	private final Unit defender;

	public UnitAttackEvent(Game game, Unit attacker, Unit defender) {
		this.game = game;
		this.attacker = attacker;
		this.defender = defender;
	}

	protected Game getGame() {
		return game;
	}

	@Override
	public boolean canExecute() {
		return true;
	}

	@Override
	public void execute(GameListener listener) {
		int attack_damage = UnitToolkit.getDamage(attacker, defender, getGame().getMap());
		doDamage(attacker, defender, attack_damage, listener);
		int attack_exp = 30;
		if (defender.getCurrentHp() > 0) {
			if (UnitToolkit.canCounter(defender, attacker)) {
				int counter_damage = UnitToolkit.getDamage(defender, attacker, getGame().getMap());
				doDamage(defender, attacker, counter_damage, listener);
				int counter_exp = 20;
				if (attacker.getCurrentHp() > 0) {
					attachBuffAfterAttack(defender, attacker);
				} else {
					counter_exp += 30;
				}
				if (defender.gainExperience(counter_exp)) {
					listener.onUnitLevelUp(defender);
				}
			}
			attachBuffAfterAttack(attacker, defender);
		} else {
			attack_exp += 30;
		}
		if (attacker.getCurrentHp() > 0 && attacker.gainExperience(attack_exp)) {
			listener.onUnitLevelUp(attacker);
		}
	}

	private void doDamage(Unit attacker, Unit defender, int damage, GameListener listener) {
		damage = defender.getCurrentHp() > damage ? damage : defender.getCurrentHp();
		defender.setCurrentHp(defender.getCurrentHp() - damage);
		listener.onUnitAttack(attacker, defender, damage);
		if (defender.getCurrentHp() <= 0) {
			new UnitDestroyEvent(getGame(), defender).execute(listener);
		}
	}

	private void attachBuffAfterAttack(Unit attacker, Unit defender) {
		if (attacker.hasAbility(Ability.POISONER)) {
			defender.attachBuff(new Buff(Buff.POISONED, 2));
		}
	}

}
