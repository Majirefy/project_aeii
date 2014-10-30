package com.toyknight.aeii.core.unit;

import com.toyknight.aeii.core.AEIIException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author toyknight
 */
public class UnitFactory {

	private static int unit_count;
	private static Unit[] units;
	private static long current_code;

	private static int commander_index;
	private static int skeleton_index;
	private static int crystal_index;

	private UnitFactory() {
	}

	public static void init(File unit_data_dir) throws IOException, AEIIException {
		try {
			File unit_config = new File(
					unit_data_dir.getAbsolutePath() + "/unit_config.dat");
			Scanner din = new Scanner(unit_config);
			unit_count = din.nextInt();
			commander_index = din.nextInt();
			skeleton_index = din.nextInt();
			crystal_index = din.nextInt();
		} catch (java.util.NoSuchElementException ex) {
			throw new AEIIException("Unit configuration file broken");
		}
		units = new Unit[unit_count];
		for (int i = 0; i < unit_count; i++) {
			File unit_data = new File(
					unit_data_dir.getAbsolutePath()
					+ "/unit_" + i + ".dat");
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
				din.close();
				Unit unit = 
						i == commander_index ? new Unit(i, true) : new Unit(i, false);
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
				units[i] = unit;
			} catch (java.util.NoSuchElementException ex) {
				throw new AEIIException("Bad unit data file: unit_" + i + ".dat");
			}
		}
		current_code = 0;
	}

	public static int getSkeletonIntex() {
		return skeleton_index;
	}

	public static int getCrystalIndex() {
		return crystal_index;
	}

	public static int getUnitCount() {
		return units.length;
	}

	public static Unit getSample(int index) {
		return cloneUnit(units[index]);
	}

	public static Unit createUnit(int index, int team) {
		String unit_code = "#" + Long.toString(current_code++);
		return createUnit(index, team, unit_code);
	}

	public static Unit cloneUnit(Unit unit) {
		String unit_code = unit.getUnitCode();
		return new Unit(unit, unit_code);
	}

	public static Unit createUnit(int index, int team, String unit_code) {
		Unit unit = new Unit(units[index], unit_code);
		unit.setTeam(team);
		unit.setStandby(false);
		unit.setCurrentHp(unit.getMaxHp());
		unit.setCurrentMovementPoint(unit.getMovementPoint());
		return unit;
	}

}
