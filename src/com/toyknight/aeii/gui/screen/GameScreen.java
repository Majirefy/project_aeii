package com.toyknight.aeii.gui.screen;

import com.toyknight.aeii.core.BasicGame;
import com.toyknight.aeii.core.GameListener;
import com.toyknight.aeii.gui.AEIIApplet;
import com.toyknight.aeii.gui.AEIIPanel;
import com.toyknight.aeii.gui.ResourceManager;
import com.toyknight.aeii.gui.Screen;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

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
		initComponents(size.width, size.height);
	}

	private void initComponents(int width, int height) {
		this.setLayout(null);
		int apw = 3; //action panel width(in column)
		int ts = getContext().getTileSize();
		tile_panel = new TilePanel();
		tile_panel.setBounds(0, height - ts, ts, ts);
		this.add(tile_panel);
		status_panel = new StatusPanel();
		status_panel.setBounds(ts, height - ts, width - ts * (apw + 1), ts);
		this.add(status_panel);
		map_canvas = new MapCanvas(ts);
		map_canvas.setBounds(0, 0, width - ts * apw, height - ts);
		this.add(map_canvas);
		action_panel = new ActionPanel();
		action_panel.setBounds(width - ts * apw, 0, ts * apw, height);
		this.add(action_panel);
	}

	public void setGame(BasicGame game) {
		this.game = game;
		this.game.setGameListener(this);
		map_canvas.setGame(game);
		tile_panel.update();
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
	public void update() {
		map_canvas.update();
		tile_panel.update();
	}

	private class TilePanel extends AEIIPanel {
		
		private int tile_index;
		
		public void update() {
			int mx = getCanvas().getCursorXOnMap();
			int my = getCanvas().getCursorYOnMap();
			tile_index = getGame().getTileIndex(mx, my);
		}
		
		@Override
		public void paintComponent(Graphics g) {
			int ts = getContext().getTileSize();
			g.drawImage(ResourceManager.getTileImage(tile_index), 0, 0, ts, ts, this);
		}
		
	}

	private class StatusPanel extends AEIIPanel {

	}

	private class ActionPanel extends AEIIPanel {

	}

}
