package com.toyknight.aeii.gui.screen.internal;

import com.toyknight.aeii.Language;
import com.toyknight.aeii.core.GameManager;
import com.toyknight.aeii.core.unit.UnitFactory;
import com.toyknight.aeii.gui.AEIIPanel;
import com.toyknight.aeii.gui.ResourceManager;
import com.toyknight.aeii.gui.control.AEIIButton;
import com.toyknight.aeii.gui.screen.MapCanvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
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
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		this.getRootPane().registerKeyboardAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				close();
			}
		}, stroke, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		this.addInternalFrameListener(new InternalFrameAdapter() {
			@Override
			public void internalFrameClosing(InternalFrameEvent e) {
				close();
			}
		});
	}

	private void initComponents() {
		this.setBorder(BorderFactory.createEmptyBorder());
		AEIIPanel container = new AEIIPanel();
		container.setPreferredSize(new Dimension(10 * ts, ts * 2 + ts * 3 / 2 * 5));
		this.setContentPane(container);
		container.setLayout(null);
		unit_list.setCellRenderer(new UnitListCellRenderer());
		unit_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		unit_list.setBackground(ResourceManager.getAEIIPanelBg());
		JScrollPane sp_unit_list = new JScrollPane(unit_list);
		sp_unit_list.setBounds(ts / 2, ts / 2, ts * 9, ts * 3 / 2 * 5);
		sp_unit_list.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		this.getContentPane().add(sp_unit_list);
		btn_buy.setBounds(ts / 2, ts * 8 + ts / 2, ts * 2, ts/2);
		btn_buy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				close();
				int index = (int) unit_list.getSelectedIndex();
				manager.buyUnit(index, canvas.getSelectedX(), canvas.getSelectedY());
			}
		});
		this.getContentPane().add(btn_buy);
	}

	public void setGameManager(GameManager manager) {
		this.manager = manager;
	}

	public void display() {
		Integer[] unit_index_list = new Integer[UnitFactory.getUnitCount()];
		for (int i = 0; i < unit_index_list.length; i++) {
			//deal with buyable issues
			unit_index_list[i] = i;
		}
		unit_list.setListData(unit_index_list);
		unit_list.setSelectedIndex(0);
		canvas.setInternalFrameShown(true);
		this.show();
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
				g.setColor(ResourceManager.getAEIIPanelBg());
			}
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			g.drawImage(ResourceManager.getBigCircleImage(0), ts / 24, ts / 24, this);
			int team = manager.getGame().getCurrentTeam();
			g.drawImage(ResourceManager.getUnitImage(team, index, 0), ts / 4 - ts / 24, ts / 4 - ts / 24, this);
		}

	}

}
