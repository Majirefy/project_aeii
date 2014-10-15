package com.toyknight.aeii.gui.screen.internal;

import com.toyknight.aeii.Language;
import com.toyknight.aeii.core.GameManager;
import com.toyknight.aeii.core.LocalGameManager;
import com.toyknight.aeii.core.unit.Ability;
import com.toyknight.aeii.core.unit.Unit;
import com.toyknight.aeii.gui.ResourceManager;
import com.toyknight.aeii.gui.control.ActionButton;
import com.toyknight.aeii.gui.screen.MapCanvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/**
 *
 * @author toyknight
 */
public class ActionBar extends JPanel {

	private final int bw;
	private final int bh;
	private LocalGameManager manager;
	private final MapCanvas canvas;

	private final ActionButton btn_buy;
	private final ActionButton btn_standby;
	private final ActionButton btn_attack;
	private final ActionButton btn_move;
	private final ActionButton btn_occupy;
	private final ActionButton btn_repair;
	private final ActionButton btn_summon;

	private int map_x;
	private int map_y;

	public ActionBar(MapCanvas canvas, int ts) {
		this.bw = ts / 24 * 20;
		this.bh = ts / 24 * 21;
		this.canvas = canvas;
		this.setLayout(null);
		this.setOpaque(false);
		btn_attack = new ActionButton(2, ts);
		btn_attack.setToolTipText(Language.getText("LB_ATTACK"));
		btn_attack.registerKeyboardAction(
				btn_attack_listener,
				KeyStroke.getKeyStroke(KeyEvent.VK_A, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		btn_attack.addActionListener(btn_attack_listener);
		btn_move = new ActionButton(4, ts);
		btn_move.setToolTipText(Language.getText("LB_MOVE"));
		btn_move.registerKeyboardAction(
				btn_move_listener,
				KeyStroke.getKeyStroke(KeyEvent.VK_M, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		btn_move.addActionListener(btn_move_listener);
		btn_occupy = new ActionButton(1, ts);
		btn_occupy.setToolTipText(Language.getText("LB_OCCUPY"));
		btn_occupy.registerKeyboardAction(
				btn_occupy_listener,
				KeyStroke.getKeyStroke(KeyEvent.VK_O, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		btn_occupy.addActionListener(btn_occupy_listener);
		btn_repair = new ActionButton(1, ts);
		btn_repair.setToolTipText(Language.getText("LB_REPAIR"));
		btn_repair.registerKeyboardAction(
				btn_repair_listener,
				KeyStroke.getKeyStroke(KeyEvent.VK_R, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		btn_repair.addActionListener(btn_repair_listener);
		btn_summon = new ActionButton(3, ts);
		btn_summon.setToolTipText(Language.getText("LB_SUMMON"));
		btn_summon.registerKeyboardAction(
				btn_summon_listener,
				KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.ALT_MASK),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		btn_summon.addActionListener(btn_summon_listener);
		btn_standby = new ActionButton(5, ts);
		btn_standby.setToolTipText(Language.getText("LB_STANDBY"));
		btn_standby.registerKeyboardAction(
				btn_standby_listener,
				KeyStroke.getKeyStroke(KeyEvent.VK_S, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		btn_standby.addActionListener(btn_standby_listener);
		btn_buy = new ActionButton(0, ts);
		btn_buy.setToolTipText(Language.getText("LB_BUY"));
		btn_buy.registerKeyboardAction(
				btn_buy_listener,
				KeyStroke.getKeyStroke(KeyEvent.VK_B, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		btn_buy.addActionListener(btn_buy_listener);
	}

	public void setGameManager(LocalGameManager manager) {
		this.manager = manager;
	}

	public void display(int x, int y) {
		this.map_x = x;
		this.map_y = y;
		this.removeAll();
		if (manager.isAccessibleCastle(x, y)
				&& manager.getState() == GameManager.STATE_SELECT) {
			addButton(btn_buy);
		}
		Unit unit = manager.getSelectedUnit();
		if (manager.getUnitToolkit().isUnitAccessible(unit)
				&& unit.getX() == x && unit.getY() == y) {
			switch (manager.getState()) {
				case GameManager.STATE_SELECT:
					addButton(btn_move);
				case GameManager.STATE_ACTION:
					addButton(btn_attack);
					if (unit.hasAbility(Ability.NECROMANCER)) {
						addButton(btn_summon);
					}
					if (manager.getGame().canOccupy(unit, unit.getX(), unit.getY())) {
						addButton(btn_occupy);
					}
					if (manager.getGame().canRepair(unit, unit.getX(), unit.getY())) {
						addButton(btn_repair);
					}
					addButton(btn_standby);
					break;
				default:
				//do nothing
			}
		}
		int btn_count = this.getComponentCount();
		if (btn_count > 0) {
			this.setBounds(0, canvas.getHeight() - bh * 2, btn_count * bw + (btn_count + 1) * bw / 4, bh);
			this.setVisible(true);
		}
	}

	private void addButton(ActionButton button) {
		int count = this.getComponentCount();
		button.setBounds(count * bw + (count + 1) * bw / 4, 0, bw, bh);
		this.add(button);
	}

	@Override
	public void paint(Graphics g) {
		((Graphics2D) g).setRenderingHint(
				RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		RoundRectangle2D background = new RoundRectangle2D.Float(0, bh / 2, getWidth(), bh / 2, 8, 8);
		g.setColor(ResourceManager.getAEIIPanelBg());
		((Graphics2D) g).fill(background);
		((Graphics2D) g).setRenderingHint(
				RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		super.paint(g);
	}

	private final ActionListener btn_move_listener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			synchronized (getTreeLock()) {
				manager.beginMovePhase();
			}
		}
	};

	private final ActionListener btn_attack_listener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			synchronized (getTreeLock()) {
				manager.beginAttackPhase();
			}
		}
	};

	private final ActionListener btn_summon_listener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			synchronized (getTreeLock()) {
				manager.beginSummonPhase();
			}
		}
	};

	private final ActionListener btn_occupy_listener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			synchronized (getTreeLock()) {
				Unit unit = manager.getSelectedUnit();
				manager.doOccupy(unit.getX(), unit.getY());
				setVisible(false);
			}
		}
	};

	private final ActionListener btn_repair_listener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			synchronized (getTreeLock()) {
				Unit unit = manager.getSelectedUnit();
				manager.doRepair(unit.getX(), unit.getY());
				setVisible(false);
			}
		}
	};

	private final ActionListener btn_standby_listener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			synchronized (getTreeLock()) {
				manager.standbySelectedUnit();
				setVisible(false);
			}
		}
	};

	private final ActionListener btn_buy_listener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			synchronized (getTreeLock()) {
				canvas.showUnitStore(map_x, map_y);
				setVisible(false);
			}
		}
	};

}
