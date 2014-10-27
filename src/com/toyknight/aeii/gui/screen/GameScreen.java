package com.toyknight.aeii.gui.screen;

import com.toyknight.aeii.core.Displayable;
import com.toyknight.aeii.core.Game;
import com.toyknight.aeii.core.GameManager;
import com.toyknight.aeii.core.LocalGameManager;
import com.toyknight.aeii.core.ManagerStateListener;
import com.toyknight.aeii.gui.AEIIApplet;
import com.toyknight.aeii.gui.Screen;
import com.toyknight.aeii.gui.animation.SwingAnimatingProvider;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 *
 * @author toyknight
 */
public class GameScreen extends Screen implements Displayable, ManagerStateListener {

	private Game game;
	private LocalGameManager manager;
	private SwingAnimatingProvider animation_provider;

	private MapCanvas map_canvas;
	private TilePanel tile_panel;
	private StatusPanel status_panel;
	private ActionPanel action_panel;

	private boolean up_approached = false;
	private boolean down_approached = false;
	private boolean left_approached = false;
	private boolean right_approached = false;

	public GameScreen(Dimension size, AEIIApplet context) {
		super(size, context);
		this.setIgnoreRepaint(true);
	}

	@Override
	public void initComponents() {
		this.setLayout(null);
		int apw = ts * 3 + ts / 4; //action panel width
		int width = getPreferredSize().width;
		int height = getPreferredSize().height;
		this.tile_panel = new TilePanel(this, ts);
		this.tile_panel.setBounds(0, height - ts, ts, ts);
		this.add(tile_panel);
		this.status_panel = new StatusPanel(ts);
		this.status_panel.setBounds(ts, height - ts, width - apw - ts, ts);
		this.add(status_panel);
		Dimension canvas_size = new Dimension(width - apw, height - ts);
		this.map_canvas = new MapCanvas(canvas_size, getContext(), this);
		this.map_canvas.setPreferredSize(canvas_size);
		this.map_canvas.setBounds(0, 0, width - apw, height - ts);
		this.map_canvas.init();
		this.add(map_canvas);
		this.action_panel = new ActionPanel(this, ts);
		this.action_panel.setBounds(width - apw, 0, apw, height);
		this.action_panel.initComponents(ts);
		this.add(action_panel);
		this.animation_provider = new SwingAnimatingProvider(this, ts);
		MouseAdapter mouse_adapter = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (getCanvas().isWithinCanvas(e.getX(), e.getY())) {
					getCanvas().onMousePress(e);
				}
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				checkBorderApporach(e);
				if (getCanvas().isWithinCanvas(e.getX(), e.getY())) {
					getCanvas().onMouseMove(e);
				}
			}
		};
		this.addMouseMotionListener(mouse_adapter);
		this.addMouseListener(mouse_adapter);
	}

	public void setGame(Game game) {
		this.game = game;
		this.manager = new LocalGameManager(game, animation_provider);
		this.manager.setStateListener(this);
		this.animation_provider.setGameManager(manager);
		this.map_canvas.setGameManager(manager);
		this.action_panel.setGameManager(manager);
		this.status_panel.setGameManager(manager);
		this.tile_panel.setGameManager(manager);
		this.game.setAnimationDispatcher(manager);
		this.game.setGameListener(manager);
		this.game.setDisplayable(this);
		this.game.startTurn();
		this.action_panel.update();
		this.tile_panel.update();
	}

	public Game getGame() {
		return game;
	}

	@Override
	public void updateControllers() {
		getActionPanel().update();
	}

	@Override
	public void locateViewport(int map_x, int map_y) {
		getCanvas().locateViewport(map_x, map_y);
	}

	@Override
	public void managerStateChanged(GameManager manager) {
		action_panel.update();
		map_canvas.updateActionBar();
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

	private void checkBorderApporach(MouseEvent e) {
		up_approached = false;
		down_approached = false;
		left_approached = false;
		right_approached = false;
		if (e.getX() < ts / 4) {
			left_approached = true;
		}
		if (e.getX() > getWidth() - ts / 4) {
			right_approached = true;
		}
		if (e.getY() < ts / 4) {
			up_approached = true;
		}
		if (e.getY() > getHeight() - ts / 4) {
			down_approached = true;
		}
	}

	public boolean isUpApproached() {
		return up_approached;
	}

	public boolean isDownApproached() {
		return down_approached;
	}

	public boolean isLeftApproached() {
		return left_approached;
	}

	public boolean isRightApproached() {
		return right_approached;
	}

	@Override
	public void update() {
		if (manager != null) {
			map_canvas.update();
			tile_panel.update();
			manager.update();
		}
	}

}
