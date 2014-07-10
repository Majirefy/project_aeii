
package com.toyknight.aeii.gui.screen;

import com.toyknight.aeii.gui.AEIIMainFrame;
import com.toyknight.aeii.gui.AEIIPanel;
import com.toyknight.aeii.gui.Screen;
import java.awt.Color;
import java.awt.Dimension;

/**
 *
 * @author toyknight
 */
public class MainMenuScreen extends Screen {
	
	public MainMenuScreen(Dimension size, AEIIMainFrame context) {
		super(size, context);
		this.setBackground(Color.BLACK);
		this.setLayout(null);
		AEIIPanel panel = new AEIIPanel();
		panel.setBounds(100, 100, 300, 200);
		this.add(panel);
	}

	@Override
	public void update() {
		
	}
	
}
