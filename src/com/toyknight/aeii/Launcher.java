package com.toyknight.aeii;

import com.toyknight.aeii.gui.AEIIApplet;
import com.toyknight.aeii.gui.util.DialogUtil;
import java.awt.EventQueue;
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
public class Launcher {

	private static JFrame main_frame;
	private static AEIIApplet aeii_applet;

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
		String title = Language.getText("LB_TITLE");
		main_frame = new JFrame(title);
		main_frame.setIconImage(ImageIO.read(Launcher.class.getResource("gameicon.png")));
		main_frame.addWindowListener(new AEIIWindowListener());
		
		aeii_applet = new AEIIApplet();
		aeii_applet.init();
		
		main_frame.setContentPane(aeii_applet.getContentPane());
		main_frame.setResizable(false);
		main_frame.pack();
		main_frame.setLocationRelativeTo(null);
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
					main_frame.setVisible(true);
					aeii_applet.start();
				}
			}
		});
	}

	private static final class AEIIWindowListener extends WindowAdapter {

		@Override
		public void windowClosing(WindowEvent e) {
			exit();
		}

	}

}
