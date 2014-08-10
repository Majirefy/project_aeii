package com.toyknight.aeii.gui.screen;

import com.toyknight.aeii.Configuration;
import com.toyknight.aeii.core.BasicGame;
import com.toyknight.aeii.core.map.Tile;
import com.toyknight.aeii.core.map.TileEntitySet;
import com.toyknight.aeii.core.unit.Unit;
import com.toyknight.aeii.core.unit.UnitFactory;
import com.toyknight.aeii.gui.ResourceManager;
import com.toyknight.aeii.gui.animation.Animation;
import com.toyknight.aeii.gui.animation.AnimationListener;
import com.toyknight.aeii.gui.animation.CursorSprite;
import com.toyknight.aeii.gui.animation.TileSprite;
import com.toyknight.aeii.gui.animation.UnitAnimation;
import com.toyknight.aeii.gui.animation.UnitSprite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.PriorityQueue;
import javax.swing.JPanel;

/**
 *
 * @author toyknight
 */
public class MapCanvas extends JPanel {

	private static final int ST_NORMAL = 0x1;
	private static final int ST_MOVE = 0x2;
	private static final int ST_RMOVE = 0x3;
	private static final int ST_ATTACK = 0x4;

	private final int ts;
	private BasicGame game;
	private int state;

	private final PriorityQueue<Animation> animation_dispatcher;
	private Animation current_animation = null;

	private Rectangle viewport;

	private int mouse_x;
	private int mouse_y;
	private CursorSprite cursor;

	private TileSprite[] tile_sprites;
	private UnitSprite[][] unit_sprites;

	private boolean up_pressed = false;
	private boolean down_pressed = false;
	private boolean left_pressed = false;
	private boolean right_pressed = false;

	public MapCanvas(int ts) {
		this.ts = ts;
		this.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				mouse_x = e.getX();
				mouse_y = e.getY();
			}
		});
		this.animation_dispatcher = new PriorityQueue();
	}

	public void init() {
		int tile_count = TileEntitySet.getTileCount();
		tile_sprites = new TileSprite[tile_count];
		for (int i = 0; i < tile_count; i++) {
			tile_sprites[i] = new TileSprite(ts, i);
			if (TileEntitySet.getTile(i).isAnimated()) {
				tile_sprites[i].setAnimationTileIndex(
						TileEntitySet.getTile(i).getAnimationTileIndex());
			}
		}
		int unit_count = UnitFactory.getUnitCount();
		unit_sprites = new UnitSprite[4][unit_count];
		for (int team = 0; team < 4; team++) {
			for (int index = 0; index < unit_count; index++) {
				unit_sprites[team][index] = new UnitSprite(team, index, ts, ts);
			}
		}
		cursor = new CursorSprite(ts);
		viewport = new Rectangle(0, 0, getWidth(), getHeight());
	}

	public void setGame(BasicGame game) {
		this.game = game;
		state = ST_NORMAL;
		locateViewport(0, 0);
		mouse_x = 0;
		mouse_y = 0;
	}

	public void submitAnimation(Animation animation) {
		animation.addAnimationListener(new AnimationListener() {
			@Override
			public void animationCompleted(Animation animation) {
				current_animation = animation_dispatcher.poll();
			}
		});
		if (current_animation == null) {
			current_animation = animation;
		} else {
			animation_dispatcher.add(animation);
		}
	}

	public Animation getCurrentAnimation() {
		return current_animation;
	}

	public boolean isAnimating() {
		return current_animation != null;
	}
	
	private boolean isUnitAnimating(Unit unit) {
		return isAnimating()
				&& unit.getX() == current_animation.getX()
				&& unit.getY() == current_animation.getY()
				&& current_animation instanceof UnitAnimation;
	}

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

	public boolean isWithinScreen(int sx, int sy) {
		return -ts < sx && sx < viewport.width && -ts < sy && sy < viewport.height;
	}

	public int getCursorXOnMap() {
		return (mouse_x + viewport.x) / ts;
	}

	public int getCursorYOnMap() {
		return (mouse_y + viewport.y) / ts;
	}

	public int getXOnScreen(int map_x) {
		int sx = viewport.x / ts;
		sx = sx > 0 ? sx : 0;
		int x_offset = sx * ts - viewport.x;
		return (map_x - sx) * ts + x_offset;
	}

	public int getYOnScreen(int map_y) {
		int sy = viewport.y / ts;
		sy = sy > 0 ? sy : 0;
		int y_offset = sy * ts - viewport.y;
		return (map_y - sy) * ts + y_offset;
	}

	public BasicGame getGame() {
		return game;
	}

	public void update() {
		TileSprite.updateFrame();
		UnitSprite.updateFrame();
		cursor.update();
		updateViewport();
		if(current_animation != null) {
			current_animation.update();
		}
	}

	public void locateViewport(int center_x, int center_y) {
		int map_width = game.getMapWidth() * ts;
		int map_height = game.getMapHeight() * ts;
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

	private void updateViewport() {
		if (!isAnimating() && getGame().isLocalPlayer()) {
			int map_width = game.getMapWidth() * ts;
			int map_height = game.getMapHeight() * ts;
			if (down_pressed && viewport.y < map_height - viewport.height) {
				viewport.y += ts / 3;
			}
			if (up_pressed && viewport.y > 0) {
				viewport.y -= ts / 3;
			}
			if (right_pressed && viewport.x < map_width - viewport.width) {
				viewport.x += ts / 3;
			}
			if (left_pressed && viewport.x > 0) {
				viewport.x -= ts / 3;
			}
		}
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		paintTiles(g, ts);
		paintUnits(g, ts);
		if (getGame().isLocalPlayer()) {
			paintCursor(g, ts);
		}
	}

	private void paintTiles(Graphics g, int ts) {
		for (int x = 0; x < getGame().getMapWidth(); x++) {
			for (int y = 0; y < getGame().getMapHeight(); y++) {
				int sx = getXOnScreen(x);
				int sy = getYOnScreen(y);
				if (isWithinScreen(sx, sy)) {
					int index = getGame().getTileIndex(x, y);
					tile_sprites[index].paint(g, sx, sy);
					Tile tile = TileEntitySet.getTile(index);
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

	private void paintUnits(Graphics g, int ts) {
		for (int i = 0; i < game.getUnitCount(); i++) {
			Unit unit = game.getUnit(i);
			//if this unit isn't animating, then paint it. otherwise, let animation paint it
			if (!isUnitAnimating(unit)) {
				int unit_x = unit.getX();
				int unit_y = unit.getY();
				int team = unit.getTeam();
				int index = unit.getIndex();
				int sx = getXOnScreen(unit_x);
				int sy = getYOnScreen(unit_y);
				if (isWithinScreen(sx, sy)) {
					unit_sprites[team][index].paint(g, sx, sy);
				}
			}
		}
	}

	private void paintCursor(Graphics g, int ts) {
		int mx = getCursorXOnMap();
		int my = getCursorYOnMap();
		int sx = getXOnScreen(mx);
		int sy = getYOnScreen(my);
		if (isWithinScreen(sx, sy)) {
			cursor.paint(g, sx, sy);
		}
	}

}
