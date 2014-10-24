package com.toyknight.aeii.gui.screen.internal;

import com.toyknight.aeii.Language;
import com.toyknight.aeii.Launcher;
import com.toyknight.aeii.gui.AEIIPanel;
import com.toyknight.aeii.gui.Screen;
import com.toyknight.aeii.gui.control.AEIIButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author toyknight
 */
public class MainMenu extends AEIIPanel {

	//welcome menu
	private final AEIIButton btn_single_player
			= new AEIIButton(Language.getText("LB_SINGLE_PLAYER"));
	private final AEIIButton btn_multiple_players
			= new AEIIButton(Language.getText("LB_MULTIPLE_PLAYERS"));
	private final AEIIButton btn_settings
			= new AEIIButton(Language.getText("LB_SETTINGS"));
	private final AEIIButton btn_about
			= new AEIIButton(Language.getText("LB_ABOUT"));
	private final AEIIButton btn_exit
			= new AEIIButton(Language.getText("LB_EXIT"));

	public void initComponents(int ts, Screen screen) {
		int menu_width = ts * 4;
		int menu_height = ts / 2 * 5 + ts / 4 * 6;
		int menu_y = screen.getPreferredSize().height - ts / 2 - menu_height;
		this.setBounds(ts / 2, menu_y, menu_width, menu_height);
		this.setLayout(null);
		btn_single_player.setBounds(ts / 4, ts / 4, ts * 4 - ts / 2, ts / 2);
		this.add(btn_single_player);
		btn_multiple_players.setBounds(ts / 4, ts / 4 * 2 + ts / 2, ts * 4 - ts / 2, ts / 2);
		btn_multiple_players.setEnabled(false);
		this.add(btn_multiple_players);
		btn_settings.setBounds(ts / 4, ts / 4 * 3 + ts, ts * 4 - ts / 2, ts / 2);
		btn_settings.setEnabled(false);
		this.add(btn_settings);
		btn_about.setBounds(ts / 4, ts / 4 * 4 + ts / 2 * 3, ts * 4 - ts / 2, ts / 2);
		btn_about.setEnabled(false);
		this.add(btn_about);
		btn_exit.setBounds(ts / 4, ts / 4 * 5 + ts / 2 * 4, ts * 4 - ts / 2, ts / 2);
		btn_exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Launcher.exit();
			}
		});
		this.add(btn_exit);
	}

}
