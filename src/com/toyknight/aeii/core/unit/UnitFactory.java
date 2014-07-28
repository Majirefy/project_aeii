package com.toyknight.aeii.core.unit;

import com.toyknight.aeii.core.AEIIException;
import com.toyknight.aeii.gui.util.SuffixFileFilter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author toyknight
 */
public class UnitFactory {

	private static Unit[] units;

	private UnitFactory() {
	}

	public static void init(File unit_data_dir)
			throws IOException, AEIIException {
		int unit_count = 
				unit_data_dir.listFiles(new SuffixFileFilter("dat")).length;
		units = new Unit[unit_count];
		for (int i = 0; i < unit_count; i++) {
			File unit_data = new File(
					unit_data_dir.getAbsolutePath()
					+ "\\unit_" + i + ".dat");
			try {
				Scanner din = new Scanner(unit_data);
				int price = din.nextInt();
				int max_hp = din.nextInt();
				int mp = din.nextInt();
				int attack = din.nextInt();
				int pdef = din.nextInt();
				int mdef = din.nextInt();
				int atk_type = din.nextInt();
				int hp_growth = din.nextInt();
				int mp_growth = din.nextInt();
				int atk_growth = din.nextInt();
				int pdefgrowth = din.nextInt();
				int mdefgrowth = din.nextInt();
				int max_atk_rng = din.nextInt();
				int min_atk_rng = din.nextInt();
				int ability_count = din.nextInt();
				ArrayList<Integer> ability_list = new ArrayList();
				for (int n = 0; n < ability_count; n++) {
					ability_list.add(din.nextInt());
				}
				int learnable_ability_count = din.nextInt();
				ArrayList<Integer> learnable_ability_list = new ArrayList();
				for (int n = 0; n < learnable_ability_count; n++) {
					learnable_ability_list.add(din.nextInt());
				}
				din.close();
				Unit unit = new Unit(i);
				unit.setPrice(price);
				unit.setMaxHp(max_hp);
				unit.setMovementPoint(mp);
				unit.setAttack(attack);
				unit.setPhysicalDefence(pdef);
				unit.setMagicalDefence(mdef);
				unit.setAttackType(atk_type);
				unit.setHpGrowth(hp_growth);
				unit.setMovementGrowth(mp_growth);
				unit.setAttackGrowth(atk_growth);
				unit.setPhysicalDefenceGrowth(pdefgrowth);
				unit.setMagicalDefenceGrowth(mdefgrowth);
				unit.setMaxAttackRange(max_atk_rng);
				unit.setMinAttackRange(min_atk_rng);
				unit.setAbilities(ability_list);
				unit.setLearnableAbilities(learnable_ability_list);
				units[i] = unit;
			} catch (java.util.NoSuchElementException ex) {
				throw new AEIIException("bad unit data");
			}
		}
	}

	public static Unit createUnit(int index) {
		return new Unit(units[index]);
	}

}
