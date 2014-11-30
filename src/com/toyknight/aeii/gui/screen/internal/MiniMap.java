package com.toyknight.aeii.gui.screen.internal;

import com.toyknight.aeii.core.Point;
import com.toyknight.aeii.core.map.Map;
import com.toyknight.aeii.core.map.Tile;
import com.toyknight.aeii.core.unit.Unit;
import com.toyknight.aeii.gui.ResourceManager;
import com.toyknight.aeii.gui.sprite.UnitPainter;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Set;
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
    private int offset_x;
    private int offset_y;
    private boolean display_units;

    public MiniMap(Dimension screen_size) {
        this.setContentPane(new MapPane());
        this.setBorder(BorderFactory.createEmptyBorder());
        ((BasicInternalFrameUI) getUI()).setNorthPane(null);

        Dimension max_map_size = new Dimension(50 * sts + 10, 50 * sts + 10);
        this.getContentPane().setPreferredSize(max_map_size);
        this.pack();
        int x = (screen_size.width - max_map_size.width) / 2;
        int y = (screen_size.height - max_map_size.height) / 2;
        this.setLocation(x, y);

        this.getRootPane().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                setVisible(false);
            }
        });

        display_units = true;
    }

    public void setDisplayUnits(boolean b) {
        synchronized (getTreeLock()) {
            this.display_units = b;
        }
    }

    public void display(Map map) {
        synchronized (getTreeLock()) {
            this.map = map;
            this.offset_x = (getWidth() - map.getWidth() * sts) / 2;
            this.offset_y = (getHeight() - map.getHeight() * sts) / 2;
            this.setVisible(true);
        }
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
            g.setColor(Color.BLACK);
            g.fillRect(5, 5, getWidth() - 10, getHeight() - 10);
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
                            x * sts + 5 + offset_x,
                            y * sts + 5 + offset_y,
                            sts, sts, this);
                }
            }
            if (display_units == true) {
                Set<Point> unit_positions = map.getUnitPositionSet();
                for (Point position : unit_positions) {
                    Unit unit = map.getUnit(position.x, position.y);
                    UnitPainter.paintMiniUnitIcon(g, unit.getTeam(),
                            unit.getX() * sts + 5 + offset_x,
                            unit.getY() * sts + 5 + offset_y);
                }
            }
        }

    }

}
