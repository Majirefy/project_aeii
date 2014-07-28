package com.toyknight.aeii.gui.screen;

import com.toyknight.aeii.core.BasicGame;
import com.toyknight.aeii.core.GameListener;
import com.toyknight.aeii.core.map.Tile;
import com.toyknight.aeii.core.map.TileFactory;
import com.toyknight.aeii.gui.AEIIApplet;
import com.toyknight.aeii.gui.AEIIPanel;
import com.toyknight.aeii.gui.ResourceManager;
import com.toyknight.aeii.gui.Screen;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author toyknight
 */
public class GameScreen extends Screen implements GameListener {

	private BasicGame game;

	private MapPanel map_panel;
	private StatusPanel status_panel;
	private ActionPanel action_panel;

	private int view_port_x;
	private int view_port_y;

	public GameScreen(Dimension size, AEIIApplet context) {
		super(size, context);
		initComponents();
	}

	private void initComponents() {
		this.setLayout(null);
		int ts = getContext().getTileSize();
		int rows = getContext().getScreenRows();
		int columns = getContext().getScreenColumns();
		status_panel = new StatusPanel();
		status_panel.setBounds(0, (rows - 1) * ts, (columns - 3) * ts, ts);
		this.add(status_panel);
		map_panel = new MapPanel();
		map_panel.setBounds(0, 0, (columns - 3) * ts, (rows - 1) * ts);
		this.add(map_panel);
		action_panel = new ActionPanel();
		action_panel.setBounds((columns - 3) * ts, 0, 3 * ts, rows * ts);
		this.add(action_panel);
	}

	public void setGame(BasicGame game) {
		this.game = game;
		this.game.setGameListener(this);
		view_port_x = 0;
		view_port_y = 0;
	}

	@Override
	public void update() {

	}

	private class MapPanel extends JPanel {

		@Override
		public void paint(Graphics g) {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, getWidth(), getHeight());
			paintTiles(g);
		}

		private void paintTiles(Graphics g) {
			int ts = getContext().getTileSize();
			for (int x = view_port_x; x < game.getMap().getMapWidth(); x++) {
				for (int y = view_port_y; y < game.getMap().getMapHeight(); y++) {
					int index = game.getMap().getTileIndex(x, y);
					g.drawImage(ResourceManager.getTileImage(index), x*ts, y*ts, this);
					Tile tile = TileFactory.getTile(index);
					if(tile.getTopTileIndex() != -1) {
						int top_tile_index = tile.getTopTileIndex();
						g.drawImage(
								ResourceManager.getTopTileImage(top_tile_index), 
								x*ts, (y-1)*ts, this);
					}
				}
			}
		}

	}

	private class StatusPanel extends AEIIPanel {

	}

	private class ActionPanel extends AEIIPanel {

	}

}
