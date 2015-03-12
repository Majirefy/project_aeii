package com.toyknight.aeii.gui.animation;

import com.toyknight.aeii.core.Point;
import com.toyknight.aeii.gui.screen.MapCanvas;

/**
 *
 * @author toyknight
 */
public class ViewportMoveAnimation extends MapAnimation {

    private final MapCanvas canvas;
    private final int target_x;
    private final int target_y;
    private final int delta_x;
    private final int delta_y;

    private int current_frame = 0;

    public ViewportMoveAnimation(MapCanvas canvas, int target_x, int target_y) {
        this.canvas = canvas;
        Point current_viewport_location = canvas.getViewportLocation();
        this.target_x = target_x;
        this.target_y = target_y;
        int current_x = current_viewport_location.x;
        int current_y = current_viewport_location.y;
        int temp_delta_x = (target_x - current_x) / 8;
        if (temp_delta_x == 0) {
            if (current_x < target_x) {
                temp_delta_x = 1;
            }
            if(current_x > target_x) {
                temp_delta_x = -1;
            }
        }
        delta_x = temp_delta_x;
        int temp_delta_y = (target_y - current_y) / 8;
        if (temp_delta_y == 0) {
            if (current_y < target_y) {
                temp_delta_y = 1;
            } 
            if(current_y > target_y) {
                temp_delta_y = -1;
            }
        }
        delta_y = temp_delta_y;
    }

    @Override
    public void doUpdate() {
        if (current_frame > 7) {
            Point location = canvas.getViewportLocation();
            canvas.dragViewport(target_x - location.x, target_y - location.y);
            complete();
        } else {
            canvas.dragViewport(delta_x, delta_y);
            current_frame++;
        }
    }

}
