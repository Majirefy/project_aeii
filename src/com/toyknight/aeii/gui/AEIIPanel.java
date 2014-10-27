package com.toyknight.aeii.gui;

import com.toyknight.aeii.gui.util.ResourceUtil;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author toyknight
 */
public class AEIIPanel extends JPanel {

	public AEIIPanel() {
		this.setOpaque(false);
	}

	@Override
	public void paint(Graphics g) {
		paintBackground(g);
		super.paint(g);
		ResourceUtil.paintBorder(g, 0, 0, getWidth(), getHeight());
	}
	
	protected void paintBackground(Graphics g) {
		g.setColor(ResourceManager.getAEIIPanelBg());
		g.fillRect(0, 0, getWidth(), getHeight());
	}

}
