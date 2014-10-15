package com.toyknight.aeii.gui.screen;

import com.toyknight.aeii.Language;
import com.toyknight.aeii.core.GameManager;
import com.toyknight.aeii.core.LocalGameManager;
import com.toyknight.aeii.core.unit.Unit;
import com.toyknight.aeii.gui.AEIIPanel;
import com.toyknight.aeii.gui.ResourceManager;
import com.toyknight.aeii.gui.control.AEIIButton;
import java.awt.Color;
import java.awt.FontMetrics;
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
	private LocalGameManager manager;
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

	public void setGameManager(LocalGameManager manager) {
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
			btn_mini_map.setEnabled(true);
			if (!game_screen.getCanvas().isInternalFrameShown()
					&& manager.getState() == GameManager.STATE_SELECT) {
				btn_end_turn.setEnabled(true);
			} else {
				btn_end_turn.setEnabled(false);
			}
		} else {
			btn_mini_map.setEnabled(false);
			btn_end_turn.setEnabled(false);
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		g.setColor(Color.WHITE);
		g.setFont(ResourceManager.getTextFont());
		FontMetrics fm = g.getFontMetrics();
		int margin = ts / 24;
		g.drawString("HP", ts / 2 + ts / 3 * 4 + margin, ts / 2 + fm.getAscent());
		g.drawString("XP", ts / 2 + ts / 3 * 4 + margin, ts / 2 + fm.getHeight() * 2 + fm.getAscent());
		Unit unit = manager.getSelectedUnit();
		if (unit != null) {
			String str_hp = unit.getCurrentHp() + "/" + unit.getMaxHp();
			g.setColor(Color.GREEN);
			g.drawString(str_hp, ts / 2 + ts / 3 * 4 + margin, ts / 2 + fm.getHeight() + fm.getAscent());
			int level_up_exp = unit.getLevelUpExperience();
			String str_exp = level_up_exp > 0
					? unit.getCurrentExperience() + "/" + level_up_exp
					: unit.getCurrentExperience() + "/-";
			g.setColor(Color.CYAN);
			g.drawString(str_exp, ts / 2 + ts / 3 * 4 + margin, ts / 2 + fm.getHeight() * 3 + fm.getAscent());
		}
		super.paintComponent(g);
	}

	private boolean isOperatable() {
		return manager.getGame().isLocalPlayer()
				&& !manager.getGame().isDispatchingEvents()
				&& !game_screen.getCanvas().isAnimating();
	}

	private final ActionListener btn_end_turn_listener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			synchronized (getTreeLock()) {
				manager.getGame().endTurn();
				game_screen.getCanvas().hideActionBar();
				game_screen.getCanvas().closeAllInternalFrames();
			}
		}

	};

}
