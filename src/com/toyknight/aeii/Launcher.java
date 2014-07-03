package com.toyknight.aeii;

import com.toyknight.aeii.gui.AEIIMainFrame;
import com.toyknight.aeii.gui.GameCanvas;
import com.toyknight.aeii.gui.Screen;
import com.toyknight.aeii.gui.screen.LogoScreen;
import com.toyknight.aeii.gui.util.DialogUtil;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author toyknight
 */
public class Launcher {

	private static AEIIMainFrame MF;

	private static boolean isRunning;
	private static boolean isUpdating;
	private static long inMenuFpsDelay = 1000 / 15;
	private static long inGameFpsDelay;
	private static long currentFpsDelay = inMenuFpsDelay;
	private static final AnimationThread animation_thread = new AnimationThread();

	private static void init()
			throws
			IOException,
			ClassNotFoundException,
			InstantiationException,
			IllegalAccessException,
			UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		Configuration.init();
		Language.init();
		inGameFpsDelay = 1000 / Configuration.getGameSpeed();
		String title = Language.getText("LB_TITLE");
		MF = new AEIIMainFrame(title);
		MF.addWindowListener(new AEIIWindowListener());
		MF.init();
	}
	private static void loadResources() {
		((LogoScreen)MF.getCurrentScreen()).setResourceLoaded(true);
	}

	private static void launch() {
		isRunning = true;
		isUpdating = true;
		MF.setVisible(true);
		animation_thread.start();
		loadResources();
	}
	
	public static long getCurrentFpsDelay() {
		return currentFpsDelay;
	}

	public static void exit() {
		isRunning = false;
		isUpdating = false;
		while (animation_thread.isAlive()) {
		}
		MF.dispose();
	}

	public static void main(String[] args) {
		try {
			init();
		} catch (ClassNotFoundException |
				InstantiationException |
				IllegalAccessException |
				UnsupportedLookAndFeelException ex) {
			//do nothing
		} catch (IOException ex) {
			DialogUtil.showError(null, ex.getMessage());
		} finally {
			launch();
		}
	}

	private static final class AnimationThread extends Thread {
		
		private Screen current_screen;
		private GameCanvas canvas;

		@Override
		public void run() {
			while (isRunning) {
				long start_time = System.currentTimeMillis();
				if (isUpdating) {
					canvas = MF.getCanvas();
					current_screen = MF.getCurrentScreen();
					current_screen.paint(canvas.getOffScreenGraphics());
					canvas.paint(canvas.getGraphics());
					current_screen.update();
				}
				long end_time = System.currentTimeMillis();
				if (end_time - start_time < currentFpsDelay) {
					waitDelay(currentFpsDelay - (end_time - start_time));
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

	private static final class AEIIWindowListener extends WindowAdapter {

		@Override
		public void windowClosing(WindowEvent e) {
			exit();
		}

		@Override
		public void windowIconified(WindowEvent e) {
			isUpdating = false;
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			isUpdating = true;
		}

		@Override
		public void windowActivated(WindowEvent e) {
			isUpdating = true;
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			isUpdating = false;
		}

	}

}
