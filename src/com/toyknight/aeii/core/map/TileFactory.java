package com.toyknight.aeii.core.map;

import com.toyknight.aeii.core.AEIIException;
import com.toyknight.aeii.gui.util.SuffixFileFilter;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author toyknight
 */
public class TileFactory {

	private static Tile[] tile_list;

	private TileFactory() {
	}

	public static void init(File tile_data_dir) 
			throws IOException, AEIIException {
		int tile_count = 
				tile_data_dir.listFiles(new SuffixFileFilter("dat")).length;
		tile_list = new Tile[tile_count];
		for (int i = 0; i < tile_count; i++) {
			File tile_data = new File(
					tile_data_dir.getAbsolutePath()
					+ "\\tile_" + i + ".dat");
			try {
			Scanner din = new Scanner(tile_data);
			int defence_bonus = din.nextInt();
			int step_cost = din.nextInt();
			int hp_recovery = din.nextInt();
			int type = din.nextInt();
			int top_tile_index = din.nextInt();
			int team = din.nextInt();
			tile_list[i] = new Tile(defence_bonus, step_cost, type);
			tile_list[i].setTopTileIndex(top_tile_index);
			tile_list[i].setHpRecovery(hp_recovery);
			tile_list[i].setTeam(team);
			int access_tile_count = din.nextInt();
			if (access_tile_count > 0) {
				int[] access_tile_list = new int[access_tile_count];
				for (int n = 0; n < access_tile_count; n++) {
					access_tile_list[n] = din.nextInt();
				}
				tile_list[i].setAccessTileList(access_tile_list);
			}
			boolean is_capturable = din.nextBoolean();
			tile_list[i].setCapturable(is_capturable);
			if (is_capturable) {
				int[] captured_tile_list = new int[4];
				for (int t = 0; t < 4; t++) {
					captured_tile_list[t] = din.nextInt();
				}
				tile_list[i].setCapturedTileList(captured_tile_list);
			}
			boolean is_destroyable = din.nextBoolean();
			tile_list[i].setDestroyable(is_destroyable);
			if (is_destroyable) {
				tile_list[i].setDestroyedTileIndex(din.nextInt());
			}
			boolean is_repairable = din.nextBoolean();
			tile_list[i].setRepairable(is_repairable);
			if (is_repairable) {
				tile_list[i].setRepairedTileIndex(din.nextInt());
			}
			boolean is_animated = din.nextBoolean();
			tile_list[i].setAnimated(is_animated);
			if(is_animated) {
				tile_list[i].setAnimationTileIndex(din.nextInt());
			}
			} catch (java.util.NoSuchElementException ex) {
				throw new AEIIException("bad tile data");
			}
		}
	}

	public static Tile getTile(int index) {
		return tile_list[index];
	}

	public static int getTileCount() {
		return tile_list.length;
	}

}
