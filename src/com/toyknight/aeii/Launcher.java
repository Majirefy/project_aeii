package com.toyknight.aeii;

import com.toyknight.aeii.core.AEIIException;
import com.toyknight.aeii.gui.AEIIApplet;
import com.toyknight.aeii.gui.util.DialogUtil;

import java.awt.EventQueue;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author toyknight
 */
public class Launcher implements Runnable {

	private final int TILE_SIZE;
	private final int SCREEN_WIDTH;
	private final int SCREEN_HEIGHT;
	private final boolean FULL_SCREEN;

	private static JFrame main_frame;
	private static AEIIApplet aeii_applet;

	public Launcher(int ts, int width, int height, boolean fs) {
		this.TILE_SIZE = ts;
		this.SCREEN_WIDTH = width;
		this.SCREEN_HEIGHT = height;
		this.FULL_SCREEN = fs;
	}

	@Override
	public void run() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException |
				InstantiationException |
				IllegalAccessException |
				UnsupportedLookAndFeelException ex) {
			//do nothing
		}
		try {
			Configuration.init();
			Language.init();
			String title = Language.getText("LB_TITLE");
			main_frame = new JFrame(title);
			if (FULL_SCREEN) {
				GraphicsEnvironment ge
						= GraphicsEnvironment.getLocalGraphicsEnvironment();
				GraphicsDevice gd = ge.getDefaultScreenDevice();
				if (gd.isFullScreenSupported()) {
					main_frame.setUndecorated(true);
					gd.setFullScreenWindow(main_frame);
				}
			}
			main_frame.setIconImage(
					ImageIO.read(Launcher.class.getResource("gameicon.png")));
			main_frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			main_frame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					exit();
				}
			});
			main_frame.setResizable(false);

			aeii_applet = new AEIIApplet(TILE_SIZE, SCREEN_WIDTH, SCREEN_HEIGHT);
			aeii_applet.init();
			main_frame.setContentPane(aeii_applet.getContentPane());

			main_frame.pack();
			main_frame.setLocationRelativeTo(null);
			main_frame.setVisible(true);
			aeii_applet.start();
		} catch (IOException ex) {
			DialogUtil.showError(null, ex.getMessage());
		}
	}

	public static AEIIApplet getApplet() {
		return aeii_applet;
	}

	public static JFrame getWindow() {
		return main_frame;
	}

	public static void exit() {
		aeii_applet.stop();
		main_frame.dispose();
		if(aeii_applet.isDebugMode()) {
			System.exit(0);
		}
	}

	public static void main(String[] args) throws Exception {
		if (args.length >= 4) {
			try {
				int ts = Integer.parseInt(args[0]);
				int width = Integer.parseInt(args[1]);
				int height = Integer.parseInt(args[2]);
				boolean fs = Boolean.parseBoolean(args[3]);
				validateParam(ts, width, height, fs);
				EventQueue.invokeLater(new Launcher(ts, width, height, fs));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {
			throw new AEIIException("缺少启动参数");
		}
	}

	private static void validateParam(int ts, int width, int height, boolean fs) throws Exception {
		if (ts < 0 || ts % 24 != 0) {
			throw new AEIIException("TILE_SIZE 必须为 24的正整数倍");
		}
	}

}
