package com.toyknight.aeii.gui.animation;

import com.toyknight.aeii.core.Point;
import com.toyknight.aeii.core.unit.Unit;
import com.toyknight.aeii.gui.screen.MapCanvas;
import com.toyknight.aeii.gui.sprite.UnitPainter;
import com.toyknight.aeii.gui.util.CharPainter;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Qua
 */
public class MapHpChangeAnimation extends MapAnimation {

    private final int ts;
    private final Map<Point, Integer> hp_change_map;
    private final int[] y_offset = {2, 0, -1, -1, -2, -2, -2, -2, -1, -1, 0, 1, 2, 4, 6, 4, 3, 4, 6, 6, 6, 6};

    private int current_frame = 0;

    public MapHpChangeAnimation(Map<Point, Integer> hp_change_map, int ts) {
        this.hp_change_map = hp_change_map;
        this.ts = ts;
    }
    
    @Override
	public void doUpdate() {
		if (current_frame < 21) {
			current_frame++;
		} else {
			complete();
		}
	}
    
    @Override
    public void paint(Graphics g, MapCanvas canvas) {
        for(Point position: hp_change_map.keySet()) {
            int change = hp_change_map.get(position);
            paintChange(g, canvas, position.x, position.y, change);
        }
    }
    
    private void paintChange(Graphics g, MapCanvas canvas, int map_x, int map_y, int change) {
        int sx = canvas.getXOnCanvas(map_x);
		int sy = canvas.getYOnCanvas(map_y);
		int dx = (ts - CharPainter.getLNumberWidth(Math.abs(change), true)) / 2;
		int dy = (ts - CharPainter.getLCharHeight()) / 2;
		if (change >= 0) {
			CharPainter.paintPositiveLNumber(g, change, sx + dx, sy + dy + y_offset[current_frame]);
		} else {
			CharPainter.paintNegativeLNumber(g, Math.abs(change), sx + dx, sy + dy + y_offset[current_frame] * ts / 24);
		}
    }

}
