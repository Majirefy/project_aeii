package com.toyknight.aeii.gui.screen;

import com.toyknight.aeii.Configuration;
import com.toyknight.aeii.core.BasicGame;
import com.toyknight.aeii.core.GameListener;
import com.toyknight.aeii.core.map.Tile;
import com.toyknight.aeii.core.map.TileFactory;
import com.toyknight.aeii.gui.AEIIApplet;
import com.toyknight.aeii.gui.AEIIPanel;
import com.toyknight.aeii.gui.ResourceManager;
import com.toyknight.aeii.gui.Screen;
import com.toyknight.aeii.gui.animation.TileSprite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;

/**
 *
 * @author toyknight
 */
public class GameScreen extends Screen implements GameListener {
	
	private static final int ST_NORMAL = 0x1;
	private static final int ST_MOVE = 0x2;
	private static final int ST_RMOVE = 0x3;
	private static final int ST_ATTACK = 0x4;

	private BasicGame game;
	
	private int state;

	private MapPanel map_panel;
	private StatusPanel status_panel;
	private ActionPanel action_panel;

	private Rectangle viewport;

	private TileSprite[] tile_sprites;

	private boolean up_pressed = false;
	private boolean down_pressed = false;
	private boolean left_pressed = false;
	private boolean right_pressed = false;

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

	public void initSprites() {
		int tile_count = TileFactory.getTileCount();
		tile_sprites = new TileSprite[tile_count];
		int tile_size = getContext().getTileSize();
		for (int i = 0; i < tile_count; i++) {
			tile_sprites[i] = new TileSprite(tile_size, i);
			if (TileFactory.getTile(i).isAnimated()) {
				tile_sprites[i].setAnimationTileIndex(
						TileFactory.getTile(i).getAnimationTileIndex());
			}
		}
	}

	public void setGame(BasicGame game) {
		int ts = getContext().getTileSize();
		this.game = game;
		this.game.setGameListener(this);
		viewport = new Rectangle(
				0, 0,
				map_panel.getWidth() / ts,
				map_panel.getHeight() / ts);
		state = ST_NORMAL;
	}
	
	private void moveViewport() {
			if (down_pressed
					&& viewport.y < game.getMap().getMapHeight() - viewport.height) {
				moveViewport(viewport.x, viewport.y + 1);
			}
			if (up_pressed && viewport.y > 0) {
				moveViewport(viewport.x, viewport.y - 1);
			}
			if (right_pressed
					&& viewport.x < game.getMap().getMapWidth() - viewport.width) {
				moveViewport(viewport.x + 1, viewport.y);
			}
			if (left_pressed && viewport.x > 0) {
				moveViewport(viewport.x - 1, viewport.y);
			}
	}

	public void moveViewport(int dest_x, int dest_y) {
		viewport.x = dest_x;
		viewport.y = dest_y;
	}

	@Override
	public void onKeyPress(KeyEvent e) {
		if (!isAnimating()) {
			if (e.getKeyCode() == Configuration.getKeyUp()) {
				this.up_pressed = true;
				moveViewport();
			}
			if (e.getKeyCode() == Configuration.getKeyDown()) {
				this.down_pressed = true;
				moveViewport();
			}
			if (e.getKeyCode() == Configuration.getKeyLeft()) {
				this.left_pressed = true;
				moveViewport();
			}
			if (e.getKeyCode() == Configuration.getKeyRight()) {
				this.right_pressed = true;
				moveViewport();
			}
		}
	}

	@Override
	public void onKeyRelease(KeyEvent e) {
		if (!isAnimating()) {
			if (e.getKeyCode() == Configuration.getKeyUp()) {
				this.up_pressed = false;
			}
			if (e.getKeyCode() == Configuration.getKeyDown()) {
				this.down_pressed = false;
			}
			if (e.getKeyCode() == Configuration.getKeyLeft()) {
				this.left_pressed = false;
			}
			if (e.getKeyCode() == Configuration.getKeyRight()) {
				this.right_pressed = false;
			}
		}
	}

	@Override
	public void update() {
		super.update();
		TileSprite.updateFrame();
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
			for (int x = viewport.x; x < viewport.x + viewport.width; x++) {
				for (int y = viewport.y; y < viewport.y + viewport.height; y++) {
					int index = game.getMap().getTileIndex(x, y);
					tile_sprites[index].paint(g,
							(x - viewport.x) * ts, (y - viewport.y) * ts);
					Tile tile = TileFactory.getTile(index);
					if (tile.getTopTileIndex() != -1) {
						int top_tile_index = tile.getTopTileIndex();
						g.drawImage(
								ResourceManager.getTopTileImage(top_tile_index),
								(x - viewport.x) * ts, (y - viewport.y - 1), this);
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
