package com.toyknight.aeii.core.event;

import com.toyknight.aeii.core.Game;
import com.toyknight.aeii.core.GameListener;
import com.toyknight.aeii.core.Point;
import com.toyknight.aeii.core.unit.Unit;
import java.util.Set;

/**
 *
 * @author toyknight
 */
public class BuffUpdateEvent implements GameEvent {

    private final Game game;
    private final Set<Point> unit_position_set;

    public BuffUpdateEvent(Game game, Set<Point> unit_position_set) {
        this.game = game;
        this.unit_position_set = unit_position_set;
    }

    @Override
    public boolean canExecute() {
        return !unit_position_set.isEmpty();
    }

    @Override
    public void execute(GameListener listener) {
        for (Point position : unit_position_set) {
            Unit unit = game.getMap().getUnit(position.x, position.y);
            if(unit != null) {
                unit.updateBuff();
            }
        }
    }

}
