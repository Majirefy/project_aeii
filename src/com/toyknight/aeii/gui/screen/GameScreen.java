package com.toyknight.aeii.gui.screen;

import com.toyknight.aeii.core.BasicGame;
import com.toyknight.aeii.core.GameManager;
import com.toyknight.aeii.core.Point;
import com.toyknight.aeii.core.animation.Animation;
import com.toyknight.aeii.core.animation.AnimationListener;
import com.toyknight.aeii.core.animation.AnimationProvider;
import com.toyknight.aeii.core.unit.Unit;
import com.toyknight.aeii.gui.AEIIApplet;
import com.toyknight.aeii.gui.Screen;
import com.toyknight.aeii.gui.animation.SmokeAnimation;
import com.toyknight.aeii.gui.animation.UnitAttackAnimation;
import com.toyknight.aeii.gui.animation.UnitDestroyedAnimation;
import com.toyknight.aeii.gui.animation.UnitMoveAnimation;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 *
 * @author toyknight
 */
public class GameScreen extends Screen implements AnimationProvider {

	private BasicGame game;
	private GameManager manager;

	private MapCanvas map_canvas;
	private TilePanel tile_panel;
	private StatusPanel status_panel;
	private ActionPanel action_panel;

	public GameScreen(Dimension size, AEIIApplet context) {
		super(size, context);
	}

	@Override
	public void initComponents() {
		this.setLayout(null);
		int apw = 3; //action panel width(in column)
		int width = getPreferredSize().width;
		int height = getPreferredSize().height;
		tile_panel = new TilePanel(this, ts);
		tile_panel.setBounds(0, height - ts, ts, ts);
		this.add(tile_panel);
		status_panel = new StatusPanel(ts);
		status_panel.setBounds(ts, height - ts, width - ts * (apw + 1), ts);
		this.add(status_panel);
		Dimension canvas_size = new Dimension(width - ts * apw, height - ts);
		map_canvas = new MapCanvas(canvas_size, getContext(), this);
		map_canvas.setPreferredSize(canvas_size);
		map_canvas.setBounds(0, 0, width - ts * apw, height - ts);
		map_canvas.init();
		this.add(map_canvas);
		action_panel = new ActionPanel(this, ts);
		action_panel.setBounds(width - ts * apw, 0, ts * apw, height);
		action_panel.initComponents(ts);
		this.add(action_panel);
	}

	public void setGame(BasicGame game) {
		this.game = game;
		this.manager = new GameManager(game, this);
		map_canvas.setGameManager(manager);
		action_panel.setGameManager(manager);
		status_panel.setGameManager(manager);
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
	public Animation getSmokeAnimation(int x, int y) {
		SmokeAnimation animation = new SmokeAnimation(x, y, ts);
		processAnimation(animation);
		animation.setInterval(1);
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
	public Animation getUnitMoveAnimation(Unit unit, int start_x, int start_y, int dest_x, int dest_y) {
		ArrayList<Point> path = manager.getUnitToolkit().createMovePath(unit, start_x, start_y, dest_x, dest_y);
		UnitMoveAnimation animation = new UnitMoveAnimation(unit, path, ts);
		processAnimation(animation);
		return animation;
	}
	
	private Animation processAnimation(Animation animation) {
		animation.addAnimationListener(new AnimationListener() {
			@Override
			public void animationCompleted(Animation animation) {
				if (manager.getGame().isLocalPlayer()) {
					action_panel.update();
					map_canvas.updateActionBar();
				}
			}
		});
		return animation;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
	}

	@Override
	public void update() {
		map_canvas.update();
		tile_panel.update();
		manager.updateAnimation();
	}

}
