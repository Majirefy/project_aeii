package com.toyknight.aeii.gui.screen;

import com.toyknight.aeii.Configuration;
import com.toyknight.aeii.core.BasicGame;
import com.toyknight.aeii.core.map.Tile;
import com.toyknight.aeii.core.map.TileRepository;
import com.toyknight.aeii.core.unit.Unit;
import com.toyknight.aeii.gui.ResourceManager;
import com.toyknight.aeii.gui.animation.Animation;
import com.toyknight.aeii.gui.animation.AnimationListener;
import com.toyknight.aeii.gui.animation.UnitAnimation;
import com.toyknight.aeii.gui.sprite.AttackCursorSprite;
import com.toyknight.aeii.gui.sprite.CursorSprite;
import com.toyknight.aeii.gui.sprite.TilePainter;
import com.toyknight.aeii.gui.sprite.UnitPainter;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.PriorityQueue;
import javax.swing.JPanel;

/**
 *
 * @author toyknight
 */
public class MapCanvas extends JPanel {

	private final int ts;
	private BasicGame game;

	private final PriorityQueue<Animation> animation_dispatcher;
	private Animation current_animation;

	private Rectangle viewport;

	private int mouse_x;
	private int mouse_y;
	private CursorSprite cursor;
	private AttackCursorSprite attack_cursor;

	private final int sprite_delay = 5;
	private int current_sprite_delay = 0;

	private boolean up_pressed = false;
	private boolean down_pressed = false;
	private boolean left_pressed = false;
	private boolean right_pressed = false;

	public MapCanvas(int ts) {
		this.ts = ts;
		this.setOpaque(false);
		MouseAdapter mouse_adapter = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				onMouseClick(e);
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				onMouseMove(e);
			}
		};
		this.addMouseMotionListener(mouse_adapter);
		this.addMouseListener(mouse_adapter);
		this.animation_dispatcher = new PriorityQueue();
	}

	public void init() {
		cursor = new CursorSprite(ts);
		attack_cursor = new AttackCursorSprite(ts);
		viewport = new Rectangle(0, 0, getWidth(), getHeight());
	}

	public void setGame(BasicGame game) {
		this.game = game;
		locateViewport(0, 0);
		mouse_x = 0;
		mouse_y = 0;
		current_animation = null;
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

	public boolean isOperatable() {
		return getGame().isLocalPlayer() && !isAnimating();
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

	public void onMouseClick(MouseEvent e) {
		if (isOperatable()) {
			int x = getCursorXOnMap();
			int y = getCursorYOnMap();
			if (e.getButton() == MouseEvent.BUTTON1) {
				switch (game.getState()) {
					case BasicGame.STATE_NORMAL:
						getGame().selectUnit(x, y);
						break;
					case BasicGame.STATE_MOVE:
						int unit_x = getGame().getSelectedUnit().getX();
						int unit_y = getGame().getSelectedUnit().getY();
						getGame().moveUnit(unit_x, unit_y, x, y);
						break;
					default:
					//do nothing
				}
			}
			if (e.getButton() == MouseEvent.BUTTON3) {
				switch (game.getState()) {
					case BasicGame.STATE_MOVE:
						getGame().cancelMovePhase();
						break;
					case BasicGame.STATE_ACTION:
						getGame().reverseMove();
						break;
					case BasicGame.STATE_ATTACK:
						getGame().cancelAttackPhase();
					default:
					//do nothing
				}
			}
		}
	}

	public void onMouseMove(MouseEvent e) {
		mouse_x = e.getX();
		mouse_y = e.getY();
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

	public BasicGame getGame() {
		return game;
	}

	public void update() {
		if (current_sprite_delay < sprite_delay) {
			current_sprite_delay++;
		} else {
			current_sprite_delay = 0;
			TilePainter.updateFrame();
			UnitPainter.updateFrame();
			cursor.update();
			attack_cursor.update();
		}
		if (isOperatable()) {
			updateViewport();
		}
		if (current_animation != null) {
			current_animation.update();
		}
	}

	public void locateViewport(int center_x, int center_y) {
		int map_width = getGame().getMap().getWidth() * ts;
		int map_height = getGame().getMap().getHeight() * ts;
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
		int map_width = getGame().getMap().getWidth() * ts;
		int map_height = getGame().getMap().getHeight() * ts;
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

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		paintTiles(g, ts);
		if (!isAnimating()) {
			switch (getGame().getState()) {
				case BasicGame.STATE_MOVE:
				case BasicGame.STATE_RMOVE:
					paintMoveAlpha(g);
					paintMovePath(g, ts);
					break;
				case BasicGame.STATE_ATTACK:
					paintAttackAlpha(g);
					break;
				default:
				//do nothing
			}
		}
		paintUnits(g);
		paintAnimation(g);
		if (getGame().isLocalPlayer()) {
			paintCursor(g, ts);
		}
		super.paint(g);
	}

	private void paintTiles(Graphics g, int ts) {
		for (int x = 0; x < getGame().getMap().getWidth(); x++) {
			for (int y = 0; y < getGame().getMap().getHeight(); y++) {
				int sx = getXOnCanvas(x);
				int sy = getYOnCanvas(y);
				if (isWithinScreen(sx, sy)) {
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

	private void paintMoveAlpha(Graphics g) {
		ArrayList<Point> movable_positions = getGame().getMovablePositions();
		for (Point position : movable_positions) {
			int sx = getXOnCanvas(position.x);
			int sy = getYOnCanvas(position.y);
			if (isWithinScreen(sx, sy)) {
				g.drawImage(ResourceManager.getMoveAlpha(), sx, sy, this);
			}
		}
	}

	private void paintMovePath(Graphics g, int ts) {
		g.setColor(ResourceManager.getMovePathColor());
		int dx = getCursorXOnMap();
		int dy = getCursorYOnMap();
		ArrayList<Point> move_path = getGame().getMovePath(dx, dy);
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
		ArrayList<Point> attackable_positions = getGame().getAttackablePositions();
		for (Point position : attackable_positions) {
			int sx = getXOnCanvas(position.x);
			int sy = getYOnCanvas(position.y);
			if (isWithinScreen(sx, sy)) {
				g.drawImage(ResourceManager.getAttackAlpha(), sx, sy, this);
			}
		}
	}

	private void paintUnits(Graphics g) {
		ArrayList<Unit> unit_list = getGame().getMap().getUnitList();
		for (Unit unit : unit_list) {
			//if this unit isn't animating, then paint it. otherwise, let animation paint it
			if (!isUnitAnimating(unit)) {
				int unit_x = unit.getX();
				int unit_y = unit.getY();
				int sx = getXOnCanvas(unit_x);
				int sy = getYOnCanvas(unit_y);
				if (isWithinScreen(sx, sy)) {
					UnitPainter.paint(g, unit, sx, sy, ts);
				}
			}
		}
	}

	private void paintAnimation(Graphics g) {
		if (isAnimating()) {
			current_animation.paint(g, this);
		}
	}

	private void paintCursor(Graphics g, int ts) {
		int mx = getCursorXOnMap();
		int my = getCursorYOnMap();
		int sx = getXOnCanvas(mx);
		int sy = getYOnCanvas(my);
		if (isWithinScreen(sx, sy)) {
			if (getGame().getState() == BasicGame.STATE_ATTACK && getGame().canAttack(mx, my)) {
				attack_cursor.paint(g, sx, sy);
			} else {
				cursor.paint(g, sx, sy);
			}
		}
	}

}
