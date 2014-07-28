package com.toyknight.aeii.gui;

import java.awt.Color;
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
		g.setColor(ResourceManager.getAEIIPanelBg());
		g.fillRect(0, 0, getWidth(), getHeight());
		g.drawImage(ResourceManager.getBorderImage(0), 0, 0, 16, 16, this);
		g.drawImage(ResourceManager.getBorderImage(1), 16, 0, getWidth() - 32, 16, this);
		g.drawImage(ResourceManager.getBorderImage(2), getWidth() - 16, 0, 16, 16, this);
		g.drawImage(ResourceManager.getBorderImage(3), 0, 16, 16, getHeight() - 32, this);
		g.drawImage(ResourceManager.getBorderImage(4), getWidth() - 16, 16, 16, getHeight() - 32, this);
		g.drawImage(ResourceManager.getBorderImage(5), 0, getHeight() - 16, 16, 16, this);
		g.drawImage(ResourceManager.getBorderImage(6), 16, getHeight() - 16, getWidth() - 32, 16, this);
		g.drawImage(ResourceManager.getBorderImage(7), getWidth() - 16, getHeight() - 16, 16, 16, this);
		super.paint(g);
	}

}
