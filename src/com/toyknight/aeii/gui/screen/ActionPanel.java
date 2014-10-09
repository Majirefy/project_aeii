package com.toyknight.aeii.gui.screen;

import com.toyknight.aeii.Language;
import com.toyknight.aeii.core.GameManager;
import com.toyknight.aeii.core.unit.Unit;
import com.toyknight.aeii.gui.AEIIPanel;
import com.toyknight.aeii.gui.ResourceManager;
import com.toyknight.aeii.gui.control.AEIIButton;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.KeyStroke;

/**
 *
 * @author toyknight
 */
public class ActionPanel extends AEIIPanel {

	private final int ts;
	private GameManager manager;
	private final GameScreen game_screen;

	private final JLabel lb_unit_icon;

	private final AEIIButton btn_mini_map;
	private final AEIIButton btn_end_turn;

	public ActionPanel(GameScreen game_screen, int ts) {
		this.ts = ts;
		this.game_screen = game_screen;
		btn_end_turn = new AEIIButton(Language.getText("LB_END_TURN"));
		btn_mini_map = new AEIIButton(Language.getText("LB_MINI_MAP"));
		lb_unit_icon = new JLabel() {
			@Override
			public void paint(Graphics g) {
				g.setColor(Color.DARK_GRAY);
				g.fillRect(0, 0, this.getWidth(), this.getHeight());
				super.paint(g);
			}
		};
	}

	public void initComponents(int ts) {
		this.setLayout(null);
		btn_end_turn.setBounds(ts / 2, getHeight() - ts, getWidth() - ts, ts / 2);
		btn_end_turn.addActionListener(btn_end_turn_listener);
		btn_end_turn.registerKeyboardAction(
				btn_end_turn_listener, 
				KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, KeyEvent.ALT_MASK), 
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		btn_end_turn.setToolTipText("Alt + Enter");
		this.add(btn_end_turn);
		btn_mini_map.setBounds(ts / 2, getHeight() - ts * 2, getWidth() - ts, ts / 2);
		this.add(btn_mini_map);
		lb_unit_icon.setBounds(ts / 2, ts / 2, ts / 3 * 4, ts / 3 * 4);
		lb_unit_icon.setHorizontalAlignment(JLabel.CENTER);
		lb_unit_icon.setBorder(BorderFactory.createLineBorder(Color.GRAY));
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
		if (isOperatable()) {
			btn_end_turn.setEnabled(true);
		} else {
			btn_end_turn.setEnabled(false);
		}
	}

	private boolean isOperatable() {
		return manager.getGame().isLocalPlayer() && !game_screen.getCanvas().isAnimating();
	}
	
	private final ActionListener btn_end_turn_listener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			manager.getGame().endTurn();
			game_screen.getCanvas().updateActionBar();
		}
		
	};

}
