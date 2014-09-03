package com.toyknight.aeii.gui.screen;

import com.toyknight.aeii.core.BasicGame;
import com.toyknight.aeii.core.GameListener;
import com.toyknight.aeii.core.unit.Unit;
import com.toyknight.aeii.gui.AEIIApplet;
import com.toyknight.aeii.gui.AEIIPanel;
import com.toyknight.aeii.gui.ResourceManager;
import com.toyknight.aeii.gui.Screen;
import com.toyknight.aeii.gui.animation.UnitMovementAnimation;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 *
 * @author toyknight
 */
public class GameScreen extends Screen implements GameListener {

	private BasicGame game;

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
		map_canvas.newGame();
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

	public BasicGame getGame() {
		return game;
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
	public void onUnitMove(Unit unit, ArrayList<Point> path) {
		Point dest = path.get(path.size() - 1);
		int x = dest.x;
		int y = dest.y;
		int ts = getContext().getTileSize();
		UnitMovementAnimation animation = new UnitMovementAnimation(unit, x, y, path, ts);
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
