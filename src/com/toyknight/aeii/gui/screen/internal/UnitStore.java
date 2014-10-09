package com.toyknight.aeii.gui.screen.internal;

import com.toyknight.aeii.Language;
import com.toyknight.aeii.core.GameManager;
import com.toyknight.aeii.core.unit.UnitFactory;
import com.toyknight.aeii.gui.AEIIPanel;
import com.toyknight.aeii.gui.ResourceManager;
import com.toyknight.aeii.gui.control.AEIIButton;
import com.toyknight.aeii.gui.screen.MapCanvas;
import com.toyknight.aeii.gui.util.CharPainter;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

/**
 *
 * @author toyknight
 */
public class UnitStore extends JInternalFrame {

	private final int ts;
	private final MapCanvas canvas;
	private GameManager manager;

	private final JList unit_list;
	private final AEIIButton btn_buy;

	public UnitStore(MapCanvas canvas, int ts) {
		super("Buy Unit", false, true, false, false);
		this.ts = ts;
		this.canvas = canvas;
		unit_list = new JList();
		btn_buy = new AEIIButton(Language.getText("LB_BUY"));
		this.initComponents();
		this.pack();
		this.setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);
		this.addInternalFrameListener(new InternalFrameAdapter() {
			@Override
			public void internalFrameClosing(InternalFrameEvent e) {
				close();
			}
		});
	}

	private void initComponents() {
		this.setBorder(BorderFactory.createEmptyBorder());
		StoreContainer container = new StoreContainer();
		container.setPreferredSize(new Dimension(11 * ts, ts + ts * 3 / 2 * 5));
		this.setContentPane(container);
		container.setLayout(null);
		unit_list.setCellRenderer(new UnitListCellRenderer());
		unit_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		unit_list.setBackground(ResourceManager.getAEIIPanelBg());
		JScrollPane sp_unit_list = new JScrollPane(unit_list);
		sp_unit_list.setBounds(ts / 2, ts / 2, ts * 5, ts * 3 / 2 * 5);
		sp_unit_list.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		this.getContentPane().add(sp_unit_list);
		btn_buy.setBounds(ts * 6, ts * 7 + ts / 2, ts * 2, ts / 2);
		btn_buy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				close();
				int index = (int) unit_list.getSelectedIndex();
				manager.buyUnit(index, canvas.getSelectedX(), canvas.getSelectedY());
			}
		});
		this.getContentPane().add(btn_buy);
		AEIIButton btn_close = new AEIIButton(Language.getText("LB_CLOSE"));
		btn_close.setBounds(ts * 8 + ts / 2, ts * 7 + ts / 2, ts * 2, ts / 2);
		btn_close.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		btn_close.registerKeyboardAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				close();
			}
		}, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
		this.getContentPane().add(btn_close);
	}

	public void setGameManager(GameManager manager) {
		this.manager = manager;
	}

	public void display() {
		ArrayList<Integer> unit_index_list = new ArrayList();
		for (int i = 0; i < UnitFactory.getUnitCount(); i++) {
			if (UnitFactory.getUnitPrice(i) > 0) {
				unit_index_list.add(i);
			}
		}
		unit_list.setListData(unit_index_list.toArray());
		unit_list.setSelectedIndex(0);
		canvas.setInternalFrameShown(true);
		this.show();
		canvas.getDesktopManager().activateFrame(this);
	}

	public void close() {
		canvas.setInternalFrameShown(false);
		this.setVisible(false);
	}

	private class UnitListCellRenderer implements ListCellRenderer {

		@Override
		public Component getListCellRendererComponent(
				JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			Integer unit_index = (Integer) value;
			UnitLabel label = new UnitLabel(unit_index, list.getWidth());
			label.setSelected(isSelected);
			return label;
		}

	}

	private class UnitLabel extends JLabel {

		private final int index;
		private boolean selected;

		public UnitLabel(int index, int width) {
			this.index = index;
			this.setPreferredSize(new Dimension(width - 18, ts / 2 * 3));
			this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		}

		public void setSelected(boolean b) {
			this.selected = b;
		}

		@Override
		public void paint(Graphics g) {
			if (selected) {
				g.setColor(Color.GRAY);
			} else {
				g.setColor(Color.DARK_GRAY);
			}
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			g.drawImage(ResourceManager.getBigCircleImage(0), ts / 24, ts / 24, this);
			int team = manager.getGame().getCurrentTeam();
			g.drawImage(ResourceManager.getUnitImage(team, index, 0), ts / 4 - ts / 24, ts / 4 - ts / 24, this);

			((Graphics2D) g).setRenderingHint(
					RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			String unit_name = Language.getText("LB_UNIT_NAME_" + index + "_0");
			g.setColor(Color.WHITE);
			g.setFont(ResourceManager.getLabelFont());
			FontMetrics fm = g.getFontMetrics();
			g.drawString(unit_name,
					fm.stringWidth(" ") + ts / 2 * 3,
					(getHeight() - fm.getHeight()) / 2 + fm.getAscent());
			((Graphics2D) g).setRenderingHint(
					RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		}

	}

	private class StoreContainer extends AEIIPanel {

		@Override
		public void paintComponent(Graphics g) {
			int lw = CharPainter.getLCharWidth();
			int lh = CharPainter.getLCharHeight();
			int interval = ts / 24 * 13;
			int index = (int) unit_list.getSelectedValue();
			((Graphics2D) g).setRenderingHint(
					RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setColor(Color.WHITE);
			g.setFont(ResourceManager.getLabelFont());
			FontMetrics fm = g.getFontMetrics();
			g.drawString(
					Language.getText("LB_UNIT_NAME_" + index + "_0"),
					ts * 6, (interval - fm.getHeight()) / 2 + fm.getAscent() + ts / 2
			);
			g.drawLine(ts * 6, ts / 2 + interval, ts * 10 + ts / 2, ts / 2 + interval);
			g.drawImage(ResourceManager.getGoldIcon(), ts * 10 + ts / 2 - lw * 4 - ts / 24 * 11, ts / 2, this);
			CharPainter.paintLNumber(g, UnitFactory.getUnitPrice(index),
					ts * 10 + ts / 2 - lw * 4, ts / 2 + (interval - lh) / 2);
			((Graphics2D) g).setRenderingHint(
					RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
			super.paintComponent(g);
		}

	}

}
