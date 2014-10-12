package com.toyknight.aeii.gui.screen;

import com.toyknight.aeii.Configuration;
import com.toyknight.aeii.core.Game;
import com.toyknight.aeii.core.GameManager;
import com.toyknight.aeii.core.LocalGameManager;
import com.toyknight.aeii.core.Point;
import com.toyknight.aeii.core.animation.Animation;
import com.toyknight.aeii.core.map.Tile;
import com.toyknight.aeii.core.map.TileRepository;
import com.toyknight.aeii.core.map.Tomb;
import com.toyknight.aeii.core.unit.Unit;
import com.toyknight.aeii.gui.AEIIApplet;
import com.toyknight.aeii.gui.ResourceManager;
import com.toyknight.aeii.gui.Screen;
import com.toyknight.aeii.gui.animation.SwingAnimation;
import com.toyknight.aeii.gui.animation.UnitAnimation;
import com.toyknight.aeii.gui.animation.UnitDestroyedAnimation;
import com.toyknight.aeii.gui.screen.internal.ActionBar;
import com.toyknight.aeii.gui.screen.internal.UnitStore;
import com.toyknight.aeii.gui.sprite.AttackCursorSprite;
import com.toyknight.aeii.gui.sprite.CursorSprite;
import com.toyknight.aeii.gui.sprite.TilePainter;
import com.toyknight.aeii.gui.sprite.UnitPainter;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Set;

/**
 *
 * @author toyknight
 */
public class MapCanvas extends Screen {

	private LocalGameManager manager;
	private final GameScreen game_screen;
	private final int SPRITE_INTERVAL = 8;

	private final ActionBar action_bar;
	private final UnitStore unit_store;

	private Rectangle viewport;

	private int current_delay;

	private int mouse_x;
	private int mouse_y;
	private CursorSprite cursor;
	private AttackCursorSprite attack_cursor;

	private int selected_x;
	private int selected_y;
	private boolean internal_frame_shown;

	private boolean up_pressed = false;
	private boolean down_pressed = false;
	private boolean left_pressed = false;
	private boolean right_pressed = false;

	public MapCanvas(Dimension size, AEIIApplet context, GameScreen game_screen) {
		super(size, context);
		this.game_screen = game_screen;
		this.setLayout(null);
		this.setOpaque(false);
		action_bar = new ActionBar(this, ts);
		unit_store = new UnitStore(this, ts);
		MouseAdapter mouse_adapter = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				onMousePress(e);
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				onMouseMove(e);
			}
		};
		this.addMouseMotionListener(mouse_adapter);
		this.addMouseListener(mouse_adapter);
	}

	public void init() {
		this.add(action_bar);
		this.add(unit_store);
		cursor = new CursorSprite(ts);
		cursor.setInterval(SPRITE_INTERVAL);
		attack_cursor = new AttackCursorSprite(ts);
		attack_cursor.setInterval(SPRITE_INTERVAL);
		viewport = new Rectangle(0, 0, getWidth(), getHeight());
	}

	public void setGameManager(LocalGameManager manager) {
		this.manager = manager;
		this.action_bar.setGameManager(manager);
		this.unit_store.setGameManager(manager);
		locateViewport(0, 0);
		mouse_x = 0;
		mouse_y = 0;
		current_delay = 0;
		internal_frame_shown = false;
	}

	public boolean isOperatable() {
		return getGame().isLocalPlayer() && !isAnimating() && !internalFrameShown();
	}

	public boolean isAnimating() {
		return manager.isAnimating();
	}

	public boolean internalFrameShown() {
		return internal_frame_shown;
	}

	public void setInternalFrameShown(boolean shown) {
		this.internal_frame_shown = shown;
	}

	private boolean isOnUnitAnimation(int x, int y) {
		if (isAnimating()) {
			Animation current_animation = manager.getCurrentAnimation();
			return current_animation.hasLocation(x, y) && current_animation instanceof UnitAnimation;
		} else {
			return false;
		}
	}

	private boolean needTombDisplay(int x, int y) {
		Animation current_animation = manager.getCurrentAnimation();
		if (current_animation == null) {
			return true;
		} else {
			if (current_animation.hasLocation(x, y)) {
				return !(current_animation instanceof UnitDestroyedAnimation);
			} else {
				return true;
			}
		}
	}

	public void showUnitStore() {
		unit_store.setLocation(
				(getWidth() - unit_store.getWidth()) / 2,
				(getHeight() - unit_store.getHeight()) / 2);
		unit_store.display();
	}

	public void onMousePress(MouseEvent e) {
		if (isOperatable()) {
			int click_x = getCursorXOnMap();
			int click_y = getCursorYOnMap();
			if (e.getButton() == MouseEvent.BUTTON1) {
				this.selected_x = click_x;
				this.selected_y = click_y;
				switch (manager.getState()) {
					case GameManager.STATE_SELECT:
						doSelect(click_x, click_y);
						break;
					case GameManager.STATE_MOVE:
					case GameManager.STATE_RMOVE:
						manager.moveSelectedUnit(click_x, click_y);
						break;
					case GameManager.STATE_ATTACK:
						manager.doAttack(click_x, click_y);
						break;
					case GameManager.STATE_SUMMON:
						manager.doSummon(click_x, click_y);
						break;
					default:
					//do nothing
				}
			}
			if (e.getButton() == MouseEvent.BUTTON3) {
				switch (manager.getState()) {
					case GameManager.STATE_SELECT:
						action_bar.setVisible(false);
						break;
					case GameManager.STATE_MOVE:
						manager.cancelMovePhase();
						break;
					case GameManager.STATE_ACTION:
						manager.reverseMove();
						break;
					case GameManager.STATE_ATTACK:
					case GameManager.STATE_SUMMON:
						manager.cancelActionPhase();
						break;
					default:
					//do nothing
				}
			}
			game_screen.getActionPanel().update();
		}
	}

	public void onMouseMove(MouseEvent e) {
		mouse_x = e.getX();
		mouse_y = e.getY();
	}

	@Override
	public void onKeyPress(KeyEvent e) {
		if (!isAnimating()) {
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
	}

	@Override
	public void onKeyRelease(KeyEvent e) {
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

	private void doSelect(int x, int y) {
		Unit unit = manager.getUnit(x, y);
		if (unit != null) {
			manager.selectUnit(x, y);
			if (manager.getUnitToolkit().isUnitAccessible(unit)) {
				action_bar.display();
			} else {
				action_bar.setVisible(false);
			}
		} else {
			if (manager.isAccessibleCastle(selected_x, selected_y)) {
				action_bar.display();
			} else {
				action_bar.setVisible(false);
			}
		}
	}

	public boolean isWithinPaintArea(int sx, int sy) {
		return -ts < sx && sx < viewport.width && -ts < sy && sy < viewport.height;
	}

	public boolean isWithinCanvas(int sx, int sy) {
		return 0 < sx && sx < viewport.width && 0 < sy && sy < viewport.height;
	}

	public int getCursorXOnMap() {
		int map_width = manager.getGame().getMap().getWidth();
		int cursor_x = (mouse_x + viewport.x) / ts;
		if (cursor_x >= map_width) {
			return map_width - 1;
		}
		if (cursor_x < 0) {
			return 0;
		}
		return cursor_x;
	}

	public int getCursorYOnMap() {
		int map_height = manager.getGame().getMap().getHeight();
		int cursor_y = (mouse_y + viewport.y) / ts;
		if (cursor_y >= map_height) {
			return map_height - 1;
		}
		if (cursor_y < 0) {
			return 0;
		}
		return cursor_y;
	}

	public int getXOnCanvas(int map_x) {
		int sx = viewport.x / ts;
		sx = sx > 0 ? sx : 0;
		int x_offset = sx * ts - viewport.x;
		return (map_x - sx) * ts + x_offset;
	}

	public int getYOnCanvas(int map_y) {
		int sy = viewport.y / ts;
		sy = sy > 0 ? sy : 0;
		int y_offset = sy * ts - viewport.y;
		return (map_y - sy) * ts + y_offset;
	}

	public int getSelectedX() {
		return selected_x;
	}

	public int getSelectedY() {
		return selected_y;
	}

	private Game getGame() {
		return manager.getGame();
	}

	@Override
	public void update() {
		if (current_delay < SPRITE_INTERVAL) {
			current_delay++;
		} else {
			current_delay = 0;
			TilePainter.updateFrame();
			UnitPainter.updateFrame();
		}
		cursor.update();
		attack_cursor.update();
		updateViewport();
	}

	public void updateActionBar() {
		if (!isAnimating()) {
			switch (manager.getState()) {
				case GameManager.STATE_RMOVE:
				case GameManager.STATE_ACTION:
					action_bar.display();
					break;
				case GameManager.STATE_SELECT:
					Unit selected_unit = manager.getSelectedUnit();
					if (manager.getUnitToolkit().isUnitAccessible(selected_unit)) {
						action_bar.display();
					} else {
						action_bar.setVisible(false);
					}
					break;
				default:
					action_bar.setVisible(false);
			}
		} else {
			action_bar.setVisible(false);
		}
	}

	public void locateViewport(int map_x, int map_y) {
		int center_sx = map_x * ts;
		int center_sy = map_y * ts;
		int map_width = getGame().getMap().getWidth() * ts;
		int map_height = getGame().getMap().getHeight() * ts;
		if (viewport.width < map_width) {
			viewport.x = center_sx - (viewport.width - ts) / 2;
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
			viewport.y = center_sy - (viewport.height - ts) / 2;
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

	public void dragViewport(int delta_x, int delta_y) {
		int map_width = getGame().getMap().getWidth() * ts;
		int map_height = getGame().getMap().getHeight() * ts;
		if (0 <= viewport.x + delta_x
				&& viewport.x + delta_x <= map_width - viewport.width) {
			viewport.x += delta_x;
		} else {
			viewport.x = viewport.x + delta_x < 0 ? 0 : map_width - viewport.width;
		}
		if (0 <= viewport.y + delta_y
				&& viewport.y + delta_y <= map_height - viewport.height) {
			viewport.y += delta_y;
		} else {
			viewport.y = viewport.y + delta_y < 0 ? 0 : map_height - viewport.height;
		}
	}

	private void updateViewport() {
		if (isOperatable()) {
			if (down_pressed) {
				dragViewport(0, ts / 3);
			}
			if (up_pressed) {
				dragViewport(0, -ts / 3);
			}
			if (right_pressed) {
				dragViewport(ts / 3, 0);
			}
			if (left_pressed) {
				dragViewport(-ts / 3, 0);
			}
		}
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		paintTiles(g, ts);
		if (!isAnimating() && getGame().isLocalPlayer()) {
			switch (manager.getState()) {
				case GameManager.STATE_RMOVE:
				case GameManager.STATE_MOVE:
					paintMoveAlpha(g);
					paintMovePath(g, ts);
					break;
				case GameManager.STATE_ATTACK:
				case GameManager.STATE_SUMMON:
					paintAttackAlpha(g);
					break;
				default:
				//do nothing
			}
		}
		paintTombs(g);
		paintUnits(g);
		paintAnimation(g);
		if (getGame().isLocalPlayer() && !isAnimating()) {
			paintCursor(g);
		}
		super.paint(g);
	}

	private void paintTiles(Graphics g, int ts) {
		for (int x = 0; x < getGame().getMap().getWidth(); x++) {
			for (int y = 0; y < getGame().getMap().getHeight(); y++) {
				int sx = getXOnCanvas(x);
				int sy = getYOnCanvas(y);
				if (isWithinPaintArea(sx, sy)) {
					int index = getGame().getMap().getTileIndex(x, y);
					TilePainter.paint(g, index, sx, sy, ts);
					Tile tile = TileRepository.getTile(index);
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

	private void paintTombs(Graphics g) {
		ArrayList<Tomb> tomb_list = manager.getGame().getMap().getTombList();
		for (Tomb tomb : tomb_list) {
			if (needTombDisplay(tomb.x, tomb.y)) {
				int sx_tomb = getXOnCanvas(tomb.x);
				int sy_tomb = getYOnCanvas(tomb.y);
				g.drawImage(ResourceManager.getTombImage(), sx_tomb, sy_tomb, this);
			}
		}
	}

	private void paintMoveAlpha(Graphics g) {
		ArrayList<Point> movable_positions = manager.getMovablePositions();
		for (Point position : movable_positions) {
			int sx = getXOnCanvas(position.x);
			int sy = getYOnCanvas(position.y);
			if (isWithinPaintArea(sx, sy)) {
				g.drawImage(ResourceManager.getMoveAlpha(), sx, sy, this);
			}
		}
	}

	private void paintMovePath(Graphics g, int ts) {
		g.setColor(ResourceManager.getMovePathColor());
		Unit unit = manager.getSelectedUnit();
		int start_x = unit.getX();
		int start_y = unit.getY();
		int dest_x = getCursorXOnMap();
		int dest_y = getCursorYOnMap();
		ArrayList<Point> move_path = manager.getUnitToolkit().createMovePath(unit, start_x, start_y, dest_x, dest_y);
		for (int i = 0; i < move_path.size(); i++) {
			if (i < move_path.size() - 1) {
				Point p1 = move_path.get(i);
				Point p2 = move_path.get(i + 1);
				if (p1.x == p2.x) {
					int x = p1.x;
					int y = p1.y < p2.y ? p1.y : p2.y;
					int sx = getXOnCanvas(x);
					int sy = getYOnCanvas(y);
					g.fillRect(sx + ts / 3, sy + ts / 3, ts / 3, ts / 3 * 4);
				}
				if (p1.y == p2.y) {
					int x = p1.x < p2.x ? p1.x : p2.x;
					int y = p1.y;
					int sx = getXOnCanvas(x);
					int sy = getYOnCanvas(y);
					g.fillRect(sx + ts / 3, sy + ts / 3, ts / 3 * 4, ts / 3);
				}
			} else {
				Point dest = move_path.get(i);
				int sx = getXOnCanvas(dest.x);
				int sy = getYOnCanvas(dest.y);
				g.drawImage(ResourceManager.getMoveTargetCursorImage(), sx, sy, this);
			}
		}
	}

	private void paintAttackAlpha(Graphics g) {
		ArrayList<Point> attackable_positions = manager.getAttackablePositions();
		for (Point position : attackable_positions) {
			int sx = getXOnCanvas(position.x);
			int sy = getYOnCanvas(position.y);
			if (isWithinPaintArea(sx, sy)) {
				g.drawImage(ResourceManager.getAttackAlpha(), sx, sy, this);
			}
		}
	}

	private void paintUnits(Graphics g) {
		Set<Point> unit_positions = getGame().getMap().getUnitPositionSet();
		for (Point position : unit_positions) {
			Unit unit = manager.getUnit(position.x, position.y);
			//if this unit isn't animating, then paint it. otherwise, let animation paint it
			if (!isOnUnitAnimation(unit.getX(), unit.getY())) {
				int unit_x = unit.getX();
				int unit_y = unit.getY();
				int sx = getXOnCanvas(unit_x);
				int sy = getYOnCanvas(unit_y);
				if (isWithinPaintArea(sx, sy)) {
					UnitPainter.paint(g, unit, sx, sy, ts);
				}
			}
		}
	}

	private void paintAnimation(Graphics g) {
		if (isAnimating()) {
			SwingAnimation animation = (SwingAnimation) manager.getCurrentAnimation();
			animation.paint(g, this);
		}
	}

	private void paintCursor(Graphics g) {
		if (isOperatable()) {
			int cursor_x = getCursorXOnMap();
			int cursor_y = getCursorYOnMap();
			int sx = getXOnCanvas(cursor_x);
			int sy = getYOnCanvas(cursor_y);
			if (isWithinPaintArea(sx, sy)) {
				switch (manager.getState()) {
					case GameManager.STATE_ATTACK:
						if (manager.canAttack(cursor_x, cursor_y)) {
							attack_cursor.paint(g, sx, sy);
						} else {
							cursor.paint(g, sx, sy);
						}
						break;
					case GameManager.STATE_SUMMON:
						if (manager.canSummon(cursor_x, cursor_y)) {
							attack_cursor.paint(g, sx, sy);
						} else {
							cursor.paint(g, sx, sy);
						}
						break;
					default:
						cursor.paint(g, sx, sy);
				}
			}
		}
	}

}
