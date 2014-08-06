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
import com.toyknight.aeii.gui.animation.CursorSprite;
import com.toyknight.aeii.gui.animation.TileSprite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
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
	private TilePanel tile_panel;
	private StatusPanel status_panel;
	private ActionPanel action_panel;

	private Rectangle viewport;

	private int mouse_x;
	private int mouse_y;
	private CursorSprite cursor;

	private TileSprite[] tile_sprites;

	private boolean up_pressed = false;
	private boolean down_pressed = false;
	private boolean left_pressed = false;
	private boolean right_pressed = false;

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
		map_panel = new MapPanel();
		map_panel.setBounds(0, 0, width - ts * apw, height - ts);
		map_panel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				mouse_x = e.getX();
				mouse_y = e.getY();
			}
		});
		this.add(map_panel);
		action_panel = new ActionPanel();
		action_panel.setBounds(width - ts * apw, 0, ts * apw, height);
		this.add(action_panel);
		viewport = new Rectangle(0, 0, map_panel.getWidth(), map_panel.getHeight());
	}

	public void initSprites() {
		int tile_size = getContext().getTileSize();
		int tile_count = TileFactory.getTileCount();
		tile_sprites = new TileSprite[tile_count];
		for (int i = 0; i < tile_count; i++) {
			tile_sprites[i] = new TileSprite(tile_size, i);
			if (TileFactory.getTile(i).isAnimated()) {
				tile_sprites[i].setAnimationTileIndex(
						TileFactory.getTile(i).getAnimationTileIndex());
			}
		}
		cursor = new CursorSprite(tile_size);
	}

	public void setGame(BasicGame game) {
		this.game = game;
		this.game.setGameListener(this);
		state = ST_NORMAL;
		locateViewport(0, 0);
		mouse_x = 0;
		mouse_y = 0;
	}

	public BasicGame getGame() {
		return game;
	}

	public void locateViewport(int center_x, int center_y) {
		int ts = getContext().getTileSize();
		int map_width = game.getMap().getMapWidth() * ts;
		int map_height = game.getMap().getMapHeight() * ts;
		if (viewport.width < map_width) {
			viewport.x = center_x - (viewport.width - ts) / 2;
			if (viewport.x < 0) {
				viewport.x = 0;
			}
			if (viewport.x > map_width - viewport.width) {
				viewport.x = map_width - viewport.width;
			}
		} else {
			viewport.x = (map_width - viewport.width) / 2;
		}
		if (viewport.height < map_height) {
			viewport.y = center_y - (viewport.height - ts) / 2;
			if (viewport.y < 0) {
				viewport.y = 0;
			}
			if (viewport.y > map_height - viewport.height) {
				viewport.y = map_height - viewport.height;
			}
		} else {
			viewport.y = (map_height - viewport.height) / 2;
		}
	}

	private boolean isWithinScreen(int sx, int sy) {
		int ts = getContext().getTileSize();
		return -ts < sx && sx < viewport.width && -ts < sy && sy < viewport.height;
	}
	
	private int getCursorXOnMap() {
		int ts = getContext().getTileSize();
		return (mouse_x + viewport.x) / ts;
	}
	
	private int getCursorYOnMap() {
		int ts = getContext().getTileSize();
		return (mouse_y + viewport.y) / ts;
	}

	private int getXOnScreen(int map_x) {
		int ts = getContext().getTileSize();
		int sx = viewport.x / ts;
		sx = sx > 0 ? sx : 0;
		int x_offset = sx * ts - viewport.x;
		return (map_x - sx) * ts + x_offset;
	}

	private int getYOnScreen(int map_y) {
		int ts = getContext().getTileSize();
		int sy = viewport.y / ts;
		sy = sy > 0 ? sy : 0;
		int y_offset = sy * ts - viewport.y;
		return (map_y - sy) * ts + y_offset;
	}

	private void updateViewport() {
		if (!isAnimating() && getGame().isLocalPlayer()) {
			int ts = getContext().getTileSize();
			int map_width = game.getMap().getMapWidth() * ts;
			int map_height = game.getMap().getMapHeight() * ts;
			if (down_pressed && viewport.y < map_height - viewport.height) {
				viewport.y += 16;
			}
			if (up_pressed && viewport.y > 0) {
				viewport.y -= 16;
			}
			if (right_pressed && viewport.x < map_width - viewport.width) {
				viewport.x += 16;
			}
			if (left_pressed && viewport.x > 0) {
				viewport.x -= 16;
			}
		}
	}

	@Override
	public void onKeyPress(KeyEvent e) {
		if (e.getKeyCode() == Configuration.getKeyUp()) {
			this.up_pressed = true;
		}
		if (e.getKeyCode() == Configuration.getKeyDown()) {
			this.down_pressed = true;
		}
		if (e.getKeyCode() == Configuration.getKeyLeft()) {
			this.left_pressed = true;
		}
		if (e.getKeyCode() == Configuration.getKeyRight()) {
			this.right_pressed = true;
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
		cursor.update();
		updateViewport();
	}

	private class MapPanel extends JPanel {

		@Override
		public void paint(Graphics g) {
			int ts = getContext().getTileSize();
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, getWidth(), getHeight());
			paintTiles(g, ts);
			if (getGame().isLocalPlayer()) {
				paintCursor(g, ts);
			}
		}

		private void paintTiles(Graphics g, int ts) {
			for (int x = 0; x < getGame().getMap().getMapWidth(); x++) {
				for (int y = 0; y < getGame().getMap().getMapHeight(); y++) {
					int sx = getXOnScreen(x);
					int sy = getYOnScreen(y);
					if (isWithinScreen(sx, sy)) {
						int index = game.getMap().getTileIndex(x, y);
						tile_sprites[index].paint(g, sx, sy);
						Tile tile = TileFactory.getTile(index);
						if (tile.getTopTileIndex() != -1) {
							int top_tile_index = tile.getTopTileIndex();
							g.drawImage(
									ResourceManager.getTopTileImage(top_tile_index),
									sx, sy - ts, this);
						}
					}
				}
			}
		}

		private void paintCursor(Graphics g, int ts) {
			int mx = getCursorXOnMap();
			int my = getCursorYOnMap();
			int sx = getXOnScreen(mx);
			int sy = getYOnScreen(my);
			cursor.paint(g, sx, sy);
		}

	}

	private class TilePanel extends AEIIPanel {

	}

	private class StatusPanel extends AEIIPanel {

	}

	private class ActionPanel extends AEIIPanel {

	}

}
