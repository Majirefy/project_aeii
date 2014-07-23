
package com.toyknight.aeii.gui;

import com.toyknight.aeii.Configuration;
import com.toyknight.aeii.Launcher;
import com.toyknight.aeii.core.map.TileFactory;
import com.toyknight.aeii.core.unit.UnitFactory;
import com.toyknight.aeii.gui.effect.ImageWaveEffect;
import com.toyknight.aeii.gui.screen.LogoScreen;
import com.toyknight.aeii.gui.screen.MainMenuScreen;
import com.toyknight.aeii.gui.util.DialogUtil;
import java.awt.AWTEvent;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
	
	private CommandLineWindow command_line;

	private final Object FPS_LOCK = new Object();

	private boolean isRunning;

	private final long inMenuFpsDelay;
	private final long inGameFpsDelay;
	private long currentFpsDelay;
	
	private final Thread animation_thread;
	private final ExecutorService executor;
	
	private Container content_pane;

	private final int ROWS = 11;
	private final int COLUMNS = 19;
	private final int SCREEN_SCALE;
	private final int BASE_TILE_SIZE;
	private final Dimension SCREEN_SIZE;

	private Screen current_screen;
	private LogoScreen logo_screen;
	private MainMenuScreen main_menu_screen;

	private CardLayout screen_container;
	
	public AEIIApplet() {
		SCREEN_SCALE = Configuration.getCanvasScale();
		BASE_TILE_SIZE = Configuration.getBaseTileSize();
		SCREEN_SIZE = new Dimension(
				BASE_TILE_SIZE * COLUMNS * SCREEN_SCALE,
				BASE_TILE_SIZE * ROWS * SCREEN_SCALE);
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
		this.getContentPane().add(main_menu_screen, ID_MAIN_MENU_SCREEN);
		setCurrentScreen(ID_LOGO_SCREEN);
		
		command_line = new CommandLineWindow(this);
		
		Toolkit.getDefaultToolkit().addAWTEventListener(
				new GlobalKeyListener(),
				AWTEvent.KEY_EVENT_MASK);
	}
	
	public void start() {
		try {
			isRunning = true;
			setCurrentFpsDelayToMenu();
			executor.submit(animation_thread);
			loadResources();
		} catch (IOException ex) {
			DialogUtil.showError(Launcher.getWindow(), ex.getMessage());
			Launcher.exit();
		}
	}
	
	public void stop() {
		isRunning = false;
		executor.shutdown();
	}
	
	private void loadResources() throws IOException {
		ResManager.init(getTileSize());
		TileFactory.init();
		UnitFactory.init();
		logo_screen.setResourceLoaded(true);
	}
	
	public Container getContentPane() {
		return content_pane;
	}
	
	public void setCurrentScreen(String screen_id) {
		screen_container.show(getContentPane(), screen_id);
		switch(screen_id) {
			case ID_LOGO_SCREEN:
				current_screen = logo_screen;
				break;
			case ID_MAIN_MENU_SCREEN:
				current_screen = main_menu_screen;
			default:
				break;
		}
		current_screen.revalidate();
	}

	public Screen getCurrentScreen() {
		return current_screen;
	}
	
	public int getScreenScale() {
		return SCREEN_SCALE;
	}

	public int getTileSize() {
		return BASE_TILE_SIZE * SCREEN_SCALE;
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
				getCurrentScreen().update();
				getCurrentScreen().repaint();
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
	
	private class GlobalKeyListener implements AWTEventListener {

		@Override
		public void eventDispatched(AWTEvent event) {
			if (event.getClass() == KeyEvent.class) {
				KeyEvent e = (KeyEvent) event;
				switch (e.getID()) {
					case KeyEvent.KEY_PRESSED:
						if (e.getKeyCode() == KeyEvent.VK_F9) {
							command_line.display();
						}
						for(KeyListener listener: current_screen.getKeyListeners()) {
							listener.keyPressed(e);
						}
						break;
					case KeyEvent.KEY_RELEASED:
						for(KeyListener listener: current_screen.getKeyListeners()) {
							listener.keyReleased(e);
						}
						break;
				}
			}
		}

	}
	
}
