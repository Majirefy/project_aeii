package com.toyknight.aeii.gui.screen;

import com.toyknight.aeii.Language;
import com.toyknight.aeii.core.GameManager;
import com.toyknight.aeii.core.unit.Unit;
import com.toyknight.aeii.gui.AEIIPanel;
import com.toyknight.aeii.gui.ResourceManager;
import com.toyknight.aeii.gui.control.AEIIButton;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author toyknight
 */
public class ActionPanel extends AEIIPanel {

	private int ts;
	private GameManager manager;
	private final GameScreen game_screen;

	private final JLabel lb_unit_icon;

	private final AEIIButton btn_standby;
	private final AEIIButton btn_attack;
	private final AEIIButton btn_move;
	private final AEIIButton btn_occupy;
	private final AEIIButton btn_repair;
	private final AEIIButton btn_summon;
	private final AEIIButton btn_mini_map;
	private final AEIIButton btn_end_turn;

	public ActionPanel(GameScreen game_screen) {
		this.game_screen = game_screen;
		btn_end_turn = new AEIIButton(Language.getText("LB_END_TURN"));
		btn_mini_map = new AEIIButton(Language.getText("LB_MINI_MAP"));
		btn_attack = new AEIIButton(Language.getText("LB_ATTACK"));
		btn_move = new AEIIButton(Language.getText("LB_MOVE"));
		btn_occupy = new AEIIButton(Language.getText("LB_OCCUPY"));
		btn_repair = new AEIIButton(Language.getText("LB_REPAIR"));
		btn_summon = new AEIIButton(Language.getText("LB_SUMMON"));
		btn_standby = new AEIIButton(Language.getText("LB_STANDBY"));
		lb_unit_icon = new JLabel();
	}

	public void initComponents(int ts) {
		this.ts = ts;
		this.setLayout(null);
		btn_end_turn.setBounds(ts / 2, getHeight() - ts, getWidth() - ts, ts / 2);
		btn_end_turn.setToolTipText("Ctrl + Enter");
		this.add(btn_end_turn);
		btn_mini_map.setBounds(ts / 2, getHeight() - ts * 2, getWidth() - ts, ts / 2);
		this.add(btn_mini_map);
		btn_summon.setBounds(ts / 2, getHeight() - ts * 3, getWidth() - ts, ts / 2);
		this.add(btn_summon);
		btn_repair.setBounds(ts / 2, getHeight() - ts * 4, getWidth() - ts, ts / 2);
		this.add(btn_repair);
		btn_occupy.setBounds(ts / 2, getHeight() - ts * 5, getWidth() - ts, ts / 2);
		this.add(btn_occupy);
		btn_move.setBounds(ts / 2, getHeight() - ts * 6, getWidth() - ts, ts / 2);
		btn_move.addActionListener(btn_move_listener);
		this.add(btn_move);
		btn_attack.setBounds(ts / 2, getHeight() - ts * 7, getWidth() - ts, ts / 2);
		btn_attack.addActionListener(btn_attack_listener);
		this.add(btn_attack);
		btn_standby.setBounds(ts / 2, getHeight() - ts * 8, getWidth() - ts, ts / 2);
		btn_standby.addActionListener(btn_standby_listener);
		this.add(btn_standby);

		lb_unit_icon.setBounds(ts / 2, ts / 2, ts, ts);
		lb_unit_icon.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		this.add(lb_unit_icon);
	}

	public void setGameManager(GameManager manager) {
		this.manager = manager;
	}

	public void update() {
		updateButtons();
		Unit unit = manager.getSelectedUnit();
		if (unit != null) {
			BufferedImage unit_image
					= ResourceManager.getUnitImage(unit.getTeam(), unit.getIndex(), 0);
			lb_unit_icon.setIcon(new ImageIcon(unit_image));
		}
	}

	private void updateButtons() {
		btn_standby.setEnabled(false);
		btn_attack.setEnabled(false);
		btn_move.setEnabled(false);
		btn_occupy.setEnabled(false);
		btn_repair.setEnabled(false);
		btn_summon.setEnabled(false);
		btn_end_turn.setEnabled(false);
		Unit unit = manager.getSelectedUnit();
		if (isOperatable()) {
			if (manager.getUnitToolkit().isUnitAccessible(unit)) {
				switch (manager.getState()) {
					case GameManager.STATE_SELECT:
						btn_move.setEnabled(true);
					case GameManager.STATE_ACTION:
						btn_attack.setEnabled(true);
					case GameManager.STATE_RMOVE:
						btn_standby.setEnabled(true);
						break;
					default:
					//do nothing
				}
			}
			if (manager.getGame().isLocalPlayer()) {
				btn_end_turn.setEnabled(true);
			}
		}
	}
	
	private boolean isOperatable() {
		return manager.getGame().isLocalPlayer() && !game_screen.getCanvas().isAnimating();
	}

	private final ActionListener btn_move_listener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			manager.beginMovePhase();
			updateButtons();
		}
	};

	private final ActionListener btn_attack_listener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			manager.beginAttackPhase();
			updateButtons();
		}
	};

	private final ActionListener btn_standby_listener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			manager.standbySelectedUnit();
			updateButtons();
		}
	};

}
