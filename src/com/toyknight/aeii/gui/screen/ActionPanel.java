package com.toyknight.aeii.gui.screen;

import com.toyknight.aeii.Language;
import com.toyknight.aeii.core.BasicGame;
import com.toyknight.aeii.gui.AEIIPanel;
import com.toyknight.aeii.gui.control.AEIIButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author toyknight
 */
public class ActionPanel extends AEIIPanel {

	private int ts;
	private BasicGame game;

	private final AEIIButton btn_end_turn;
	private final AEIIButton btn_mini_map;
	private final AEIIButton btn_attack;
	private final AEIIButton btn_move;
	private final AEIIButton btn_occupy;
	private final AEIIButton btn_repair;
	private final AEIIButton btn_summon;

	public ActionPanel() {
		btn_end_turn = new AEIIButton(Language.getText("LB_END_TURN"));
		btn_mini_map = new AEIIButton(Language.getText("LB_MINI_MAP"));
		btn_attack = new AEIIButton(Language.getText("LB_ATTACK"));
		btn_move = new AEIIButton(Language.getText("LB_MOVE"));
		btn_occupy = new AEIIButton(Language.getText("LB_OCCUPY"));
		btn_repair = new AEIIButton(Language.getText("LB_REPAIR"));
		btn_summon = new AEIIButton(Language.getText("LB_SUMMON"));
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
		this.add(btn_attack);
	}

	public void setGame(BasicGame game) {
		this.game = game;
	}
	
	public BasicGame getGame() {
		return game;
	}
	
	private final ActionListener btn_move_listener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			getGame().beginMovePhase();
		}
	};

}
