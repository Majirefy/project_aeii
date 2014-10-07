
package com.toyknight.aeii.core.map;

import com.toyknight.aeii.core.unit.Unit;
import com.toyknight.aeii.core.unit.UnitFactory;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 *
 * @author toyknight
 */
public class MapFactory {
	
	private MapFactory() {
	}
	
	public static Map createMap(File map_file) throws IOException {
		DataInputStream fis = new DataInputStream(new FileInputStream(map_file));
		String author_name = fis.readUTF();
		int width = fis.readInt();
		int height = fis.readInt();
		short[][] map_data = new short[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				map_data[i][j] = fis.readShort();
			}
		}
		Map map = new Map(map_data, author_name);
		int unit_count = fis.readInt();
		for (int i = 0; i < unit_count; i++) {
			int team = fis.readInt();
			int index = fis.readInt();
			int x = fis.readInt();
			int y = fis.readInt();
			Unit unit = UnitFactory.createUnit(index, team);
			unit.setX(x);
			unit.setY(y);
			map.addUnit(unit);
		}
		fis.close();
		return map;
	}
	
}
