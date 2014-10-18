package com.toyknight.aeii.gui.screen.internal;

import com.toyknight.aeii.Language;
import com.toyknight.aeii.core.GameManager;
import com.toyknight.aeii.core.unit.Unit;
import com.toyknight.aeii.core.unit.UnitFactory;
import com.toyknight.aeii.gui.AEIIPanel;
import com.toyknight.aeii.gui.ResourceManager;
import com.toyknight.aeii.gui.control.AEIIButton;
import com.toyknight.aeii.gui.screen.MapCanvas;
import com.toyknight.aeii.gui.util.CharPainter;
import java.awt.Color;
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
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicInternalFrameUI;

/**
 *
 * @author toyknight
 */
public class UnitStore extends JInternalFrame {

	private final int ts;
	private final MapCanvas canvas;
	private GameManager manager;

	private int last_selection;
	private final JList unit_list;
	private final AEIIButton btn_buy;

	private int current_team;
	private boolean commander_alive;
	private Unit selected_unit;
	private int store_x;
	private int store_y;

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
		//init components
		this.setBorder(BorderFactory.createEmptyBorder());
		StoreContainer container = new StoreContainer();
		container.setPreferredSize(new Dimension(11 * ts, ts + ts * 3 / 2 * 5));
		this.setContentPane(container);
		container.setLayout(null);
		unit_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		unit_list.setBackground(ResourceManager.getAEIIPanelBg());
		unit_list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (unit_list.getSelectedIndex() >= 0) {
					int select = unit_list.getSelectedIndex();
					int unit_index = (int) unit_list.getModel().getElementAt(select);
					selected_unit = UnitFactory.getSample(unit_index);
					if (selected_unit.isCommander()) {
						selected_unit = manager.getGame().getCommander(selected_unit.getTeam());
					}
					last_selection = unit_list.getSelectedIndex();
					updateButton();
				} else {
					selected_unit = null;
				}
			}
		});
		JScrollPane sp_unit_list = new JScrollPane(unit_list);
		sp_unit_list.setBounds(ts / 2, ts / 2, ts * 5, ts * 3 / 2 * 5);
		sp_unit_list.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		this.getContentPane().add(sp_unit_list);
		btn_buy.setBounds(ts * 6, ts * 7 + ts / 2, ts * 2, ts / 2);
		btn_buy.registerKeyboardAction(
				btn_buy_listener,
				KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		btn_buy.addActionListener(btn_buy_listener);
		this.getContentPane().add(btn_buy);
		AEIIButton btn_close = new AEIIButton(Language.getText("LB_CLOSE"));
		btn_close.setBounds(ts * 8 + ts / 2, ts * 7 + ts / 2, ts * 2, ts / 2);
		btn_close.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doDefaultCloseAction();
			}
		});
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		btn_close.registerKeyboardAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doDefaultCloseAction();
			}
		}, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
		this.getContentPane().add(btn_close);
		last_selection = 0;
		//hide title
		JComponent store_title = new JComponent() {
		};
		store_title.setPreferredSize(new Dimension(11 * ts, ts / 2));
		((BasicInternalFrameUI) getUI()).setNorthPane(store_title);
	}

	public void setGameManager(GameManager manager) {
		this.manager = manager;
		this.unit_list.setSelectedIndex(0);
		this.unit_list.setCellRenderer(new UnitListCellRenderer(manager, ts));
	}

	public void display(int x, int y) {
		this.store_x = x;
		this.store_y = y;
		current_team = manager.getGame().getCurrentTeam();
		commander_alive = manager.getGame().isCommanderAlive(current_team);
		ArrayList<Integer> unit_index_list = new ArrayList();
		for (int i = 0; i < UnitFactory.getUnitCount(); i++) {
			if (UnitFactory.getSample(i).getPrice() > 0 && i != 10 && i != 11) {
				unit_index_list.add(i);
			}
		}
		unit_list.setListData(unit_index_list.toArray());
		this.show();
		canvas.getDesktopManager().activateFrame(this);
		unit_list.setSelectedIndex(last_selection);
	}

	public void close() {
		canvas.closeUnitStore();
	}

	private void buyUnit(int index) {
		synchronized (getTreeLock()) {
			if (selected_unit.isCommander()) {
				manager.restoreCommander(store_x, store_y);
			} else {
				manager.buyUnit(index, store_x, store_y);
			}
			canvas.updateActionBar();
		}
	}

	private void updateButton() {
		if (selected_unit != null
				&& manager.getGame().getCurrentPlayer().getPopulation() < manager.getGame().getMaxPopulation()) {
			if (selected_unit.isCommander()) {
				btn_buy.setEnabled(!commander_alive
						&& manager.getGame().getCurrentPlayer().getGold() > manager.getGame().getCommanderPrice(current_team));
			} else {
				btn_buy.setEnabled(manager.getGame().getCurrentPlayer().getGold() >= selected_unit.getPrice());
			}
		} else {
			btn_buy.setEnabled(false);
		}
	}

	private final ActionListener btn_buy_listener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			close();
			buyUnit((int) unit_list.getSelectedValue());
		}
	};

	private class StoreContainer extends AEIIPanel {

		@Override
		public void paintComponent(Graphics g) {
			if (unit_list.getSelectedIndex() >= 0) {
				paintUnitData(g);
			}
			super.paintComponent(g);
		}

		private void paintUnitData(Graphics g) {
			int lw = CharPainter.getLCharWidth();
			int lh = CharPainter.getLCharHeight();
			int interval = ts / 24 * 13;
			int index = (int) unit_list.getSelectedValue();
			((Graphics2D) g).setRenderingHint(
					RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setColor(Color.WHITE);
			g.setFont(ResourceManager.getLabelFont());
			FontMetrics fm = g.getFontMetrics();
			//paint unit info title
			g.drawString(
					Language.getText("LB_UNIT_NAME_" + index + "_0"),
					ts * 6, (interval - fm.getHeight()) / 2 + fm.getAscent() + ts / 2
			);
			g.drawLine(ts * 6, ts / 2 + interval, ts * 10 + ts / 2, ts / 2 + interval);
			g.drawImage(
					ResourceManager.getGoldIcon(),
					ts * 10 + ts / 2 - lw * 4 - ts / 24 * 11,
					ts / 2, this);
			int price = selected_unit.isCommander()
					? manager.getGame().getCommanderPrice(current_team)
					: selected_unit.getPrice();
			if (price > 0) {
				CharPainter.paintLNumber(g, price,
						ts * 10 + ts / 2 - lw * 4,
						ts / 2 + (interval - lh) / 2);
			} else {
				g.drawImage(ResourceManager.getLMinus(),
						ts * 10 + ts / 2 - lw * 2 - lw / 2,
						ts / 2 + (interval - lh) / 2, this);
			}
			//paint unit property
			int scw = ts / 24 * 20;
			int sch = ts / 24 * 21;
			int acs = ts / 24 * 16;
			int hw = ts / 24 * 13;
			int hh = ts / 24 * 16;
			int itemh = sch + ts / 6;
			int tfh = sch - ts / 4;
			g.setColor(ResourceManager.getTextBackgroundColor());
			//paint unit attack
			g.fillRect(
					ts * 6 + scw / 2,
					ts / 2 + interval + (itemh - tfh) / 2,
					ts * 2 + ts / 4 - scw / 2 - ts / 12, tfh);
			CharPainter.paintNumber(
					g, selected_unit.getAttack(),
					ts * 6 + scw + ts / 12,
					ts / 2 + interval + (itemh - CharPainter.getCharHeight()) / 2);
			switch (selected_unit.getAttackType()) {
				case Unit.ATTACK_PHYSICAL:
					g.drawImage(
							ResourceManager.getSmallUnitImage(7),
							ts * 6 + scw + ts / 12 + CharPainter.getCharWidth() * 3 + ts / 24,
							ts / 2 + interval + (itemh - ts / 24 * 10) / 2, this);
					break;
				case Unit.ATTACK_MAGICAL:
					g.drawImage(
							ResourceManager.getSmallUnitImage(4),
							ts * 6 + scw + ts / 12 + CharPainter.getCharWidth() * 3 + ts / 24,
							ts / 2 + interval + (itemh - ts / 24 * 10) / 2, this);
					break;
				default:
				//do nothing
			}
			g.drawImage(
					ResourceManager.getSmallCircleImage(0),
					ts * 6,
					ts / 2 + interval + (itemh - sch) / 2, this);
			g.drawImage(
					ResourceManager.getAttackIcon(),
					ts * 6 + (scw - hw) / 2,
					ts / 2 + interval + (itemh - sch) / 2 + (sch - hh) / 2, this);
			//paint unit movement point
			g.fillRect(
					ts * 8 + ts / 4 + scw / 2,
					ts / 2 + interval + (itemh - tfh) / 2,
					ts * 2 + ts / 4 - scw / 2 - ts / 12, tfh);
			CharPainter.paintNumber(
					g, selected_unit.getMovementPoint(),
					ts * 8 + ts / 4 + scw + ts / 12,
					ts / 2 + interval + (itemh - CharPainter.getCharHeight()) / 2);
			g.drawImage(
					ResourceManager.getSmallCircleImage(0),
					ts * 8 + ts / 4,
					ts / 2 + interval + (itemh - sch) / 2, this);
			g.drawImage(
					ResourceManager.getActionIcon(4),
					ts * 8 + ts / 4 + (scw - acs) / 2,
					ts / 2 + interval + (itemh - sch) / 2 + (sch - hh) / 2, this);
			//paint unit physical defence
			g.fillRect(
					ts * 6 + scw / 2,
					ts / 2 + interval + itemh + (itemh - tfh) / 2,
					ts * 2 + ts / 4 - scw / 2 - ts / 12, tfh);
			CharPainter.paintNumber(
					g, selected_unit.getPhysicalDefence(),
					ts * 6 + scw + ts / 12,
					ts / 2 + interval + itemh + (itemh - CharPainter.getCharHeight()) / 2);
			g.drawImage(
					ResourceManager.getSmallCircleImage(0),
					ts * 6,
					ts / 2 + interval + itemh + (itemh - sch) / 2, this);
			g.drawImage(
					ResourceManager.getRedDefenceIcon(),
					ts * 6 + (scw - hw) / 2,
					ts / 2 + interval + itemh + (itemh - sch) / 2 + (sch - hh) / 2, this);
			//paint unit magical defence
			g.fillRect(
					ts * 8 + ts / 4 + scw / 2,
					ts / 2 + interval + itemh + (itemh - tfh) / 2,
					ts * 2 + ts / 4 - scw / 2 - ts / 12, tfh);
			CharPainter.paintNumber(
					g, selected_unit.getMagicalDefence(),
					ts * 8 + ts / 4 + scw + ts / 12,
					ts / 2 + interval + itemh + (itemh - CharPainter.getCharHeight()) / 2);
			g.drawImage(
					ResourceManager.getSmallCircleImage(0),
					ts * 8 + ts / 4,
					ts / 2 + interval + itemh + (itemh - sch) / 2, this);
			g.drawImage(
					ResourceManager.getBlueDefenceIcon(),
					ts * 8 + ts / 4 + (scw - hw) / 2,
					ts / 2 + interval + itemh + (itemh - sch) / 2 + (sch - hh) / 2, this);
			g.setColor(Color.WHITE);
			g.drawLine(ts * 6, ts / 2 + interval + itemh * 2, ts * 10 + ts / 2, ts / 2 + interval + itemh * 2);
			//paint unit description
			((Graphics2D) g).setRenderingHint(
					RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		}

	}

}
