package com.toyknight.aeii.gui.screen;

import com.toyknight.aeii.core.Game;
import com.toyknight.aeii.core.GameManager;
import com.toyknight.aeii.gui.AEIIApplet;
import com.toyknight.aeii.gui.Screen;
import com.toyknight.aeii.gui.animation.SwingAnimatingProvider;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

/**
 *
 * @author toyknight
 */
public class GameScreen extends Screen {

	private Game game;
	private GameManager manager;
	private SwingAnimatingProvider animation_provider;

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
		this.tile_panel = new TilePanel(this, ts);
		this.tile_panel.setBounds(0, height - ts, ts, ts);
		this.add(tile_panel);
		this.status_panel = new StatusPanel(ts);
		this.status_panel.setBounds(ts, height - ts, width - ts * (apw + 1), ts);
		this.add(status_panel);
		Dimension canvas_size = new Dimension(width - ts * apw, height - ts);
		this.map_canvas = new MapCanvas(canvas_size, getContext(), this);
		this.map_canvas.setPreferredSize(canvas_size);
		this.map_canvas.setBounds(0, 0, width - ts * apw, height - ts);
		this.map_canvas.init();
		this.add(map_canvas);
		this.action_panel = new ActionPanel(this, ts);
		this.action_panel.setBounds(width - ts * apw, 0, ts * apw, height);
		this.action_panel.initComponents(ts);
		this.add(action_panel);
		this.animation_provider = new SwingAnimatingProvider(this, ts);
	}

	public void setGame(Game game) {
		this.game = game;
		this.manager = new GameManager(game, animation_provider);
		this.animation_provider.setGameManager(manager);
		this.map_canvas.setGameManager(manager);
		this.action_panel.setGameManager(manager);
		this.status_panel.setGameManager(manager);
		this.tile_panel.setGameManager(manager);
		this.action_panel.update();
		this.tile_panel.update();
		this.game.startTurn();
	}

	public Game getGame() {
		return game;
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
