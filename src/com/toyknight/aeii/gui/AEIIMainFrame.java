package com.toyknight.aeii.gui;

import com.toyknight.aeii.Configuration;
import com.toyknight.aeii.gui.effect.ImageWaveEffect;
import com.toyknight.aeii.gui.screen.LogoScreen;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 *
 * @author toyknight
 */
public class AEIIMainFrame extends JFrame implements WindowListener {

	private final int ROWS = 10;
	private final int COLUMNS = 15;
	private final int CANVAS_SCALE;
	private final int BASE_TILE_SIZE;
	private final Dimension BASE_CANVAS_SIZE;

	private final long inMenuFpsDelay = 1000 / 15;
	private final long inGameFpsDelay;

	private long currentFpsDelay = inMenuFpsDelay;

	private boolean isRunning;
	private boolean isUpdating;
	private GameCanvas canvas;
	private final AnimationThread animation_thread = new AnimationThread();
	
	private Screen current_screen;
	private LogoScreen logo_screen;

	public AEIIMainFrame(String title) {
		super(title);
		CANVAS_SCALE = Configuration.getCanvasScale();
		BASE_TILE_SIZE = Configuration.getBaseTileSize();
		BASE_CANVAS_SIZE = new Dimension(
				BASE_TILE_SIZE * COLUMNS,
				BASE_TILE_SIZE * ROWS);
		inGameFpsDelay = 1000 / Configuration.getGameSpeed();
		this.setIconImage(
				new ImageIcon(getClass().getResource("gameicon.png")).getImage());
	}

	public void init() {
		canvas = new GameCanvas(BASE_CANVAS_SIZE, CANVAS_SCALE);
		this.getContentPane().add(canvas);
		this.getContentPane().setIgnoreRepaint(true);

		this.setResizable(false);
		this.pack();
		this.setLocationRelativeTo(null);
		this.addWindowListener(this);
		
		logo_screen = new LogoScreen(BASE_CANVAS_SIZE, this);
		
		ImageWaveEffect.createSinTab();
	}
	
	public void startRunning() {
		isRunning = true;
		isUpdating = true;
		current_screen = logo_screen;
		animation_thread.start();
		loadResources();
	}

	public void exit() {
		isRunning = false;
		isUpdating = false;
		while(animation_thread.isAlive()) {}
		this.dispose();
	}
	
	public long getCurrentFpsDelay() {
		return currentFpsDelay;
	}

	public GameCanvas getCanvas() {
		return canvas;
	}
	
	private void loadResources() {
		logo_screen.setResourceLoaded(true);
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		exit();
	}

	@Override
	public void windowClosed(WindowEvent e) {
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

	private final class AnimationThread extends Thread {

		@Override
		public void run() {
			while (isRunning) {
				long start_time = System.currentTimeMillis();
				if (isUpdating) {
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

}
