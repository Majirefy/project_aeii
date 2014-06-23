package com.toyknight.aeii;

import com.toyknight.aeii.gui.AEIIMainFrame;
import com.toyknight.aeii.gui.util.DialogUtil;
import java.io.IOException;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author toyknight
 */
public class Launcher {
	
	private static AEIIMainFrame MF;
	
	private static void launch() {
		String title = Language.getText("LB_TITLE");
		MF = new AEIIMainFrame(title);
		MF.init();
		MF.setVisible(true);
		MF.startRunning();
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			Configuration.init();
			Language.init();
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

}
