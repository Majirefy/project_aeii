package com.toyknight.aeii.gui.screen;

import com.toyknight.aeii.Language;
import com.toyknight.aeii.core.BasicGame;
import com.toyknight.aeii.gui.AEIIPanel;
import com.toyknight.aeii.gui.control.AEIIButton;
import javax.swing.JSeparator;

/**
 *
 * @author toyknight
 */
public class ActionPanel extends AEIIPanel {

	private int ts;
	private BasicGame game;
	private final MapCanvas map_canvas;

	private final AEIIButton btn_end_turn;
	private final AEIIButton btn_mini_map;
	private final AEIIButton btn_attack;
	private final AEIIButton btn_move;
	private final AEIIButton btn_occupy;
	private final AEIIButton btn_repair;
	private final AEIIButton btn_summon;

	public ActionPanel(MapCanvas map_canvas) {
		this.map_canvas = map_canvas;
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
//		JSeparator separator = new JSeparator(JSeparator.CENTER);
//		separator.setBounds(ts / 2, getHeight() - ts / 2 * 5, getWidth() - ts, ts / 2);
//		this.add(separator);
	}

	public void setGame(BasicGame game) {
		this.game = game;
	}

}
