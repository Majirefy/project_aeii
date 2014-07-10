package com.toyknight.aeii;

import com.toyknight.aeii.gui.AEIIMainFrame;
import com.toyknight.aeii.gui.ResManager;
import com.toyknight.aeii.gui.util.DialogUtil;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
	private static final Thread animation_thread = new Thread(new Animator());
	private static final ExecutorService excecutor = Executors.newSingleThreadExecutor();

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

	private static void loadResources() throws IOException {
		ResManager.init();
		MF.setResourceLoaded();
	}

	private static void launch() {
		try {
			isRunning = true;
			isUpdating = true;
			MF.setVisible(true);
			excecutor.submit(animation_thread);
			loadResources();
		} catch (IOException ex) {
			DialogUtil.showError(MF, ex.getMessage());
			exit();
		}
	}

	public static long getCurrentFpsDelay() {
		return currentFpsDelay;
	}

	public static void exit() {
		isRunning = false;
		isUpdating = false;
		excecutor.shutdown();
		MF.dispose();
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
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
		});
	}

	private static final class Animator implements Runnable {

		@Override
		public void run() {
			while (isRunning) {
				long start_time = System.currentTimeMillis();
				if (isUpdating) {
					MF.getCurrentScreen().update();
					MF.getCurrentScreen().repaint();
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
