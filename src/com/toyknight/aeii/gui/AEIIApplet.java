package com.toyknight.aeii.gui;

import com.toyknight.aeii.Configuration;
import com.toyknight.aeii.Launcher;
import com.toyknight.aeii.core.AEIIException;
import com.toyknight.aeii.core.map.TileRepository;
import com.toyknight.aeii.core.unit.UnitFactory;
import com.toyknight.aeii.gui.effect.ImageWaveEffect;
import com.toyknight.aeii.gui.screen.GameScreen;
import com.toyknight.aeii.gui.screen.LogoScreen;
import com.toyknight.aeii.gui.screen.MainMenuScreen;
import com.toyknight.aeii.gui.util.CharPainter;
import com.toyknight.aeii.gui.util.DialogUtil;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.KeyEventPostProcessor;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author toyknight
 */
public class AEIIApplet {

	public static final String ID_LOGO_SCREEN = "logo";
	public static final String ID_MAIN_MENU_SCREEN = "main_menu";
	public static final String ID_GAME_SCREEN = "game";

	private CommandLineDialog command_line;

	private final Object FPS_LOCK = new Object();

	private boolean isRunning;

	private final long inMenuFpsDelay;
	private final long inGameFpsDelay;
	private long currentFpsDelay;

	private final Thread animation_thread;
	private final ExecutorService executor;

	private Container content_pane;

	private final int TILE_SIZE;
	private final Dimension SCREEN_SIZE;

	private Screen current_screen;
	private LogoScreen logo_screen;
	private MainMenuScreen main_menu_screen;
	private GameScreen game_screen;

	private CardLayout screen_container;

	public AEIIApplet(int ts, int width, int height) {
		this.TILE_SIZE = ts;
		SCREEN_SIZE = new Dimension(width, height);
		inMenuFpsDelay = 1000 / 15;
		inGameFpsDelay = 1000 / Configuration.getGameSpeed();
		animation_thread = new Thread(new Animator());
		executor = Executors.newSingleThreadExecutor();
	}

	public void init() {
		ImageWaveEffect.createSinTab();

		content_pane = new Container();

		screen_container = new CardLayout();
		this.getContentPane().setLayout(screen_container);
		logo_screen = new LogoScreen(SCREEN_SIZE, this);
		this.getContentPane().add(logo_screen, ID_LOGO_SCREEN);
		main_menu_screen = new MainMenuScreen(SCREEN_SIZE, this);
		main_menu_screen.initComponents();
		this.getContentPane().add(main_menu_screen, ID_MAIN_MENU_SCREEN);
		game_screen = new GameScreen(SCREEN_SIZE, this);
		game_screen.initComponents();
		this.getContentPane().add(game_screen, ID_GAME_SCREEN);
		setCurrentScreen(ID_LOGO_SCREEN);

		CharPainter.init(TILE_SIZE);

		command_line = new CommandLineDialog(this);

		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventPostProcessor(new GlobalKeyListener());
	}

	public void start() {
		isRunning = true;
		setCurrentFpsDelayToMenu();
		executor.submit(animation_thread);
		current_screen.repaint();
		loadResources();
	}

	public void stop() {
		isRunning = false;
		executor.shutdown();
	}

	private void loadResources() {
		new Thread("resource loading thread") {
			@Override
			public void run() {
				try {
					File tile_data_dir = new File("data\\tiles");
					File unit_data_dir = new File("data\\units");
					TileRepository.init(tile_data_dir);
					UnitFactory.init(unit_data_dir);
					ResourceManager.init(getTileSize());
					logo_screen.setResourceLoaded(true);
				} catch (IOException | AEIIException ex) {
					DialogUtil.showError(Launcher.getWindow(), ex.getMessage());
					Launcher.exit();
				}
			}
		}.start();
	}

	public Container getContentPane() {
		return content_pane;
	}

	public void setCurrentScreen(String screen_id) {
		screen_container.show(getContentPane(), screen_id);
		switch (screen_id) {
			case ID_LOGO_SCREEN:
				current_screen = logo_screen;
				break;
			case ID_MAIN_MENU_SCREEN:
				current_screen = main_menu_screen;
				break;
			case ID_GAME_SCREEN:
				current_screen = game_screen;
				break;
			default:
				break;
		}
		current_screen.revalidate();
	}

	public Screen getCurrentScreen() {
		return current_screen;
	}

	public GameScreen getGameScreen() {
		return game_screen;
	}

	public int getTileSize() {
		return TILE_SIZE;
	}

	public void setCurrentFpsDelayToGame() {
		synchronized (FPS_LOCK) {
			currentFpsDelay = inGameFpsDelay;
		}
	}

	public void setCurrentFpsDelayToMenu() {
		synchronized (FPS_LOCK) {
			currentFpsDelay = inMenuFpsDelay;
		}
	}

	public void setCurrentFpsDelay(long delay) {
		synchronized (FPS_LOCK) {
			currentFpsDelay = delay;
		}
	}

	public long getCurrentFpsDelay() {
		synchronized (FPS_LOCK) {
			return currentFpsDelay;
		}
	}

	private final class Animator implements Runnable {

		@Override
		public void run() {
			while (isRunning) {
				long start_time = System.currentTimeMillis();
				getCurrentScreen().repaint();
				getCurrentScreen().update();
				long end_time = System.currentTimeMillis();
				long current_fps_delay = getCurrentFpsDelay();
				if (end_time - start_time < current_fps_delay) {
					waitDelay(current_fps_delay - (end_time - start_time));
				} else {
					waitDelay(0);
				}
			}
		}

		private void waitDelay(long time) {
			try {
				Thread.sleep(time);
			} catch (InterruptedException ex) {
			}
		}

	}
	
	private class GlobalKeyListener implements KeyEventPostProcessor {

		@Override
		public boolean postProcessKeyEvent(KeyEvent e) {
			if (!command_line.isVisible()) {
				switch (e.getID()) {
					case KeyEvent.KEY_PRESSED:
						if (e.getKeyCode() == KeyEvent.VK_F9) {
							command_line.display();
						} else {
							current_screen.onKeyPress(e);
						}
						break;
					case KeyEvent.KEY_RELEASED:
						current_screen.onKeyRelease(e);
						break;
				}
			}
			return false;
		}
		
	}

}
