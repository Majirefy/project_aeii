package com.toyknight.aeii.gui.screen.internal;

import com.toyknight.aeii.Language;
import com.toyknight.aeii.Launcher;
import com.toyknight.aeii.gui.AEIIApplet;
import com.toyknight.aeii.gui.AEIIPanel;
import com.toyknight.aeii.gui.Screen;
import com.toyknight.aeii.gui.control.AEIIButton;
import com.toyknight.aeii.gui.screen.LocalMapScreen;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

/**
 *
 * @author toyknight
 */
public class MainMenu extends JPanel {

	public static final String ID_WELCOME_MENU = "welcome_menu";
	public static final String ID_SINGLE_PLAYER_MENU = "single_player_menu";

	private final AEIIApplet context;
	private final CardLayout menu_container;
	
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

	//single player menu
	private final AEIIButton btn_campaign
			= new AEIIButton(Language.getText("LB_CAMPAIGN"));
	private final AEIIButton btn_skirmish
			= new AEIIButton(Language.getText("LB_SKIRMISH"));
	private final AEIIButton btn_ctf
			= new AEIIButton(Language.getText("LB_CTF"));
	private final AEIIButton btn_load
			= new AEIIButton(Language.getText("LB_LOAD"));
	private final AEIIButton btn_back
			= new AEIIButton(Language.getText("LB_BACK"));

	public MainMenu(AEIIApplet context) {
		this.context = context;
		this.menu_container = new CardLayout();
	}

	public void initComponents(int ts, Screen screen) {
		int menu_width = ts * 4;
		int menu_height = ts / 2 * 5 + ts / 4 * 6;
		int menu_y = screen.getPreferredSize().height - ts / 2 - menu_height;
		this.setBounds(ts / 2, menu_y, menu_width, menu_height);
		this.setLayout(menu_container);
		JPanel welcome_menu = new AEIIPanel();
		welcome_menu.setLayout(null);
		btn_single_player.setBounds(ts / 4, ts / 4, ts * 4 - ts / 2, ts / 2);
		btn_single_player.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showMenu(ID_SINGLE_PLAYER_MENU);
			}
		});
		welcome_menu.add(btn_single_player);
		btn_multiple_players.setBounds(ts / 4, ts / 4 * 2 + ts / 2, ts * 4 - ts / 2, ts / 2);
		btn_multiple_players.setEnabled(false);
		welcome_menu.add(btn_multiple_players);
		btn_settings.setBounds(ts / 4, ts / 4 * 3 + ts, ts * 4 - ts / 2, ts / 2);
		btn_settings.setEnabled(false);
		welcome_menu.add(btn_settings);
		btn_about.setBounds(ts / 4, ts / 4 * 4 + ts / 2 * 3, ts * 4 - ts / 2, ts / 2);
		btn_about.setEnabled(false);
		welcome_menu.add(btn_about);
		btn_exit.setBounds(ts / 4, ts / 4 * 5 + ts / 2 * 4, ts * 4 - ts / 2, ts / 2);
		btn_exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Launcher.exit();
			}
		});
		welcome_menu.add(btn_exit);
		this.add(welcome_menu, ID_WELCOME_MENU);
		JPanel single_player_menu = new AEIIPanel();
		single_player_menu.setLayout(null);
		btn_campaign.setBounds(ts / 4, ts / 4, ts * 4 - ts / 2, ts / 2);
		btn_campaign.setEnabled(false);
		single_player_menu.add(btn_campaign);
		btn_skirmish.setBounds(ts / 4, ts / 4 * 2 + ts / 2, ts * 4 - ts / 2, ts / 2);
		btn_skirmish.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				context.gotoLocalMapScreen(LocalMapScreen.MODE_SKIRMISH);
			}
		});
		single_player_menu.add(btn_skirmish);
		btn_ctf.setBounds(ts / 4, ts / 4 * 3 + ts, ts * 4 - ts / 2, ts / 2);
		btn_ctf.setEnabled(false);
		single_player_menu.add(btn_ctf);
		btn_load.setBounds(ts / 4, ts / 4 * 4 + ts / 2 * 3, ts * 4 - ts / 2, ts / 2);
		btn_load.setEnabled(false);
		single_player_menu.add(btn_load);
		btn_back.setBounds(ts / 4, ts / 4 * 5 + ts / 2 * 4, ts * 4 - ts / 2, ts / 2);
		btn_back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showMenu(ID_WELCOME_MENU);
			}
		});
		single_player_menu.add(btn_back);
		this.add(single_player_menu, ID_SINGLE_PLAYER_MENU);
	}

	public void showMenu(String menu_id) {
		menu_container.show(this, menu_id);
		this.revalidate();
	}

}
