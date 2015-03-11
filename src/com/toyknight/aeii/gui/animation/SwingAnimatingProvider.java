package com.toyknight.aeii.gui.animation;

import com.toyknight.aeii.Language;
import com.toyknight.aeii.core.GameManager;
import com.toyknight.aeii.core.Point;
import com.toyknight.aeii.core.animation.Animation;
import com.toyknight.aeii.core.animation.AnimationListener;
import com.toyknight.aeii.core.animation.AnimationProvider;
import com.toyknight.aeii.core.unit.Unit;
import com.toyknight.aeii.gui.screen.GameScreen;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author toyknight
 */
public class SwingAnimatingProvider implements AnimationProvider {

    private final int ts;
    private GameManager manager;
    private final GameScreen game_screen;

    public SwingAnimatingProvider(GameScreen screen, int ts) {
        this.ts = ts;
        this.game_screen = screen;
    }

    public void setGameManager(GameManager manager) {
        this.manager = manager;
    }

    @Override
    public Animation getOccupiedMessageAnimation() {
        MessageAnimation animation = new MessageAnimation(Language.getText("LB_MSG_OCCUPIED"), 15, ts);
        processAnimation(animation);
        return animation;
    }

    @Override
    public Animation getRepairedMessageAnimation() {
        MessageAnimation animation = new MessageAnimation(Language.getText("LB_MSG_REPAIRED"), 15, ts);
        processAnimation(animation);
        return animation;
    }

    @Override
    public Animation getSummonAnimation(Unit summoner, int target_x, int target_y) {
        SummonAnimation animation = new SummonAnimation(summoner, target_x, target_y, ts);
        processAnimation(animation);
        animation.setInterval(1);
        return animation;
    }

    @Override
    public Animation getSmokeAnimation(int x, int y) {
        SmokeAnimation animation = new SmokeAnimation(x, y, ts);
        processAnimation(animation);
        animation.setInterval(2);
        return animation;
    }

    @Override
    public Animation getUnitHpChangeAnimation(Unit unit, int change) {
        UnitHpChangeAnimation animation = new UnitHpChangeAnimation(unit, change, ts);
        processAnimation(animation);
        return animation;
    }
    
    @Override
    public Animation getMapHpChangeAnimation(Map<Point, Integer> hp_change_map) {
        MapHpChangeAnimation animation = new MapHpChangeAnimation(hp_change_map, ts);
        processAnimation(animation);
        return animation;
    }

    @Override
    public Animation getTileAttackedAnimation(int tile_index, int x, int y) {
        TileAttackedAnimation animation = new TileAttackedAnimation(tile_index, x, y, ts);
        processAnimation(animation);
        return animation;
    }

    @Override
    public Animation getUnitAttackAnimation(Unit attacker, Unit defender, int damage) {
        UnitAttackAnimation animation = new UnitAttackAnimation(attacker, defender, damage, ts);
        processAnimation(animation);
        animation.setInterval(1);
        return animation;
    }

    @Override
    public Animation getUnitDestroyedAnimation(Unit unit) {
        UnitDestroyedAnimation animation = new UnitDestroyedAnimation(unit, ts);
        processAnimation(animation);
        animation.setInterval(1);
        return animation;
    }

    @Override
    public Animation getUnitLevelUpAnimation(Unit unit) {
        UnitLevelUpAnimation animation = new UnitLevelUpAnimation(unit, ts);
        processAnimation(animation);
        animation.setInterval(1);
        return animation;
    }

    @Override
    public Animation getUnitMoveAnimation(Unit unit, int start_x, int start_y, int dest_x, int dest_y) {
        ArrayList<Point> path = manager.getUnitToolkit().createMovePath(unit, start_x, start_y, dest_x, dest_y);
        UnitMoveAnimation animation = new UnitMoveAnimation(unit, path, ts);
        processAnimation(animation);
        return animation;
    }

    @Override
    public Animation getTurnStartAnimation(int turn, int income, int team) {
        NewTurnAnimation animation = new NewTurnAnimation(turn, income, team, ts);
        processAnimation(animation);
        return animation;
    }

    @Override
    public Animation getGameOverAnimation(int allience) {
        String message = Language.getText("LB_MSG_GAME_OVER").replaceAll("%team", "" + (allience + 1));
        MessageAnimation animation = new MessageAnimation(message, 30, ts);
        processAnimation(animation);
        return animation;
    }

    private Animation processAnimation(Animation animation) {
        animation.addAnimationListener(new AnimationListener() {
            @Override
            public void animationCompleted(Animation animation) {
                if (manager.getGame().isLocalPlayer()) {
                    game_screen.getActionPanel().update();
                    //game_screen.getCanvas().updateActionBar();
                }
            }
        });
        return animation;
    }

}
