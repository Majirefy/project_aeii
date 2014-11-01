package com.toyknight.aeii.gui.screen.internal;

import com.toyknight.aeii.core.map.Map;
import com.toyknight.aeii.core.map.Tile;
import com.toyknight.aeii.gui.ResourceManager;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicInternalFrameUI;

/**
 *
 * @author toyknight
 */
public class MiniMap extends JInternalFrame {

	private final int sts = 10;

	private final Color color_border_dark = new Color(66, 73, 99);
	private final Color color_border_light = new Color(173, 182, 173);

	private Map map;

	public MiniMap() {
		this.setContentPane(new MapPane());
		this.setBorder(BorderFactory.createEmptyBorder());
		((BasicInternalFrameUI) getUI()).setNorthPane(null);
		this.getRootPane().addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				setVisible(false);
			}
		});
	}

	private void init(Map map) {
		int map_width = map.getWidth();
		int map_height = map.getHeight();
		Dimension size = new Dimension(map_width * sts + 10, map_height * sts + 10);
		this.getContentPane().setPreferredSize(size);
		this.pack();
		int x = (getParent().getWidth() - size.width) / 2;
		int y = (getParent().getHeight() - size.height) / 2;
		this.setLocation(x, y);
	}

	public void display(Map map) {
		this.map = map;
		this.init(map);
		this.setVisible(true);
	}

	private class MapPane extends JPanel {

		@Override
		public void paintComponent(Graphics g) {
			g.setColor(color_border_dark);
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(color_border_light);
			g.fillRect(1, 1, getWidth() - 2, getHeight() - 2);
			g.setColor(color_border_dark);
			g.fillRect(4, 4, getWidth() - 8, getHeight() - 8);
			if (map != null) {
				paintMap(g);
			}
		}

		private void paintMap(Graphics g) {
			for (int x = 0; x < map.getWidth(); x++) {
				for (int y = 0; y < map.getHeight(); y++) {
					Tile tile = map.getTile(x, y);
					int stile_index = tile.getMiniMapIndex();
					g.drawImage(
							ResourceManager.getSTileImage(stile_index),
							x * sts + 5, y * sts + 5, sts, sts, this);
				}
			}
		}

	}

}
