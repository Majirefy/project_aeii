package com.toyknight.aeii.gui.screen;

import com.toyknight.aeii.core.BasicGame;
import com.toyknight.aeii.core.GameListener;
import com.toyknight.aeii.core.LocalGameManager;
import com.toyknight.aeii.core.unit.Unit;
import com.toyknight.aeii.gui.AEIIApplet;
import com.toyknight.aeii.gui.AEIIPanel;
import com.toyknight.aeii.gui.Screen;
import com.toyknight.aeii.gui.animation.Animation;
import com.toyknight.aeii.gui.animation.AnimationListener;
import com.toyknight.aeii.gui.animation.UnitAnimation;
import com.toyknight.aeii.gui.animation.UnitAttackedAnimation;
import com.toyknight.aeii.gui.animation.UnitMovementAnimation;
import com.toyknight.aeii.gui.animation.UnitStandbyAnimation;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 *
 * @author toyknight
 */
public class GameScreen extends Screen implements GameListener {

	private BasicGame game;
	private LocalGameManager manager;

	private MapCanvas map_canvas;
	private TilePanel tile_panel;
	private StatusPanel status_panel;
	private ActionPanel action_panel;

	public GameScreen(Dimension size, AEIIApplet context) {
		super(size, context);
	}

	@Override
	public void initComponents(int ts) {
		this.setLayout(null);
		int apw = 3; //action panel width(in column)
		int width = getPreferredSize().width;
		int height = getPreferredSize().height;
		tile_panel = new TilePanel(this, ts);
		tile_panel.setBounds(0, height - ts, ts, ts);
		this.add(tile_panel);
		status_panel = new StatusPanel();
		status_panel.setBounds(ts, height - ts, width - ts * (apw + 1), ts);
		this.add(status_panel);
		map_canvas = new MapCanvas(this, ts);
		map_canvas.setBounds(0, 0, width - ts * apw, height - ts);
		map_canvas.init();
		this.add(map_canvas);
		action_panel = new ActionPanel(this);
		action_panel.setBounds(width - ts * apw, 0, ts * apw, height);
		action_panel.initComponents(ts);
		this.add(action_panel);
	}

	public void setGame(BasicGame game) {
		this.game = game;
		this.game.setGameListener(this);
		this.manager = new LocalGameManager(game);
		map_canvas.newGame(manager);
		action_panel.setGameManager(manager);
		tile_panel.setGameManager(manager);
		action_panel.update();
		tile_panel.update();
		this.game.startTurn();
	}
	
	public ActionPanel getActionPanel() {
		return action_panel;
	}
	
	public TilePanel getTilePanel() {
		return tile_panel;
	}
	
	public MapCanvas getCanvas() {
		return map_canvas;
	}

	@Override
	public void onKeyPress(KeyEvent e) {
		map_canvas.onKeyPress(e);
	}

	@Override
	public void onKeyRelease(KeyEvent e) {
		map_canvas.onKeyRelease(e);
	}
	
	@Override
	public void onUnitStandby(Unit unit) {
		int ts = getContext().getTileSize();
		UnitStandbyAnimation animation = new UnitStandbyAnimation(unit, ts);
		animation.addAnimationListener(new AnimationListener() {
			@Override
			public void animationCompleted(Animation animation) {
				Unit unit = ((UnitAnimation)animation).getUnit();
				manager.getGame().standbyUnit(unit.getX(), unit.getY());
			}
		});
		getCanvas().submitAnimation(animation);
	}
	
	@Override
	public void onUnitAttack(Unit attacker, Unit defender, int damage) {
		int ts = getContext().getTileSize();
		UnitAttackedAnimation animation = new UnitAttackedAnimation(defender, damage, ts);
		animation.setInterval(1);
		getCanvas().submitAnimation(animation);
	}
	
	@Override
	public void onUnitMove(Unit unit, int start_x, int start_y, int dest_x, int dest_y) {
		ArrayList<Point> path;
		if(manager.getGame().isLocalPlayer()) {
			path = manager.getMovePath(dest_x, dest_y);
		} else {
			manager.getUnitToolkit().createMovablePositions(unit);
			path = manager.getUnitToolkit().createMovePath(start_x, start_y, dest_x, dest_y);
		}
		int ts = getContext().getTileSize();
		UnitMovementAnimation animation = new UnitMovementAnimation(unit, path, ts);
		getCanvas().submitAnimation(animation);
	}

	@Override
	public void update() {
		map_canvas.update();
		tile_panel.update();
	}

	private class StatusPanel extends AEIIPanel {
		
	}

}
