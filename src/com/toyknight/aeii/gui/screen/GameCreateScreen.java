package com.toyknight.aeii.gui.screen;

import com.toyknight.aeii.Language;
import com.toyknight.aeii.audio.AudioManager;
import com.toyknight.aeii.core.Game;
import com.toyknight.aeii.core.creator.GameCreator;
import com.toyknight.aeii.core.creator.GameCreatorListener;
import com.toyknight.aeii.core.map.Map;
import com.toyknight.aeii.core.map.MapProvider;
import com.toyknight.aeii.gui.AEIIApplet;
import com.toyknight.aeii.gui.ResourceManager;
import com.toyknight.aeii.gui.Screen;
import com.toyknight.aeii.gui.control.AEIIButton;
import com.toyknight.aeii.gui.screen.internal.MapListCellRenderer;
import com.toyknight.aeii.gui.screen.internal.MiniMap;
import com.toyknight.aeii.gui.util.ResourceUtil;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author toyknight
 */
public class GameCreateScreen extends Screen implements GameCreatorListener {

	private final AEIIButton btn_back = new AEIIButton(Language.getText("LB_BACK"));
	private final AEIIButton btn_play = new AEIIButton(Language.getText("LB_PLAY"));
	private final AEIIButton btn_refresh = new AEIIButton(Language.getText("LB_REFRESH"));
	private final AEIIButton btn_preview = new AEIIButton(Language.getText("LB_PREVIEW"));
	private final JList map_list = new JList();
	private final MiniMap mini_map;

	private GameCreator game_creator;
	private MapProvider map_provider;

	public GameCreateScreen(Dimension size, AEIIApplet context) {
		super(size, context);
        this.mini_map = new MiniMap(size);
	}

	@Override
	public void initComponents() {
		this.setLayout(null);
		int width = getPreferredSize().width;
		int height = getPreferredSize().height;
		btn_back.setBounds(width - ts * 7, height - ts, ts * 3, ts / 2);
		btn_back.registerKeyboardAction(
				btn_back_listener,
				KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		btn_back.addActionListener(btn_back_listener);
		this.add(btn_back);
		btn_play.setBounds(width - ts * 3 - ts / 2, height - ts, ts * 3, ts / 2);
		btn_play.registerKeyboardAction(
				btn_play_listener,
				KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		btn_play.addActionListener(btn_play_listener);
		this.add(btn_play);
		map_list.setFocusable(false);
		map_list.setBackground(Color.DARK_GRAY);
		map_list.setCellRenderer(new MapListCellRenderer(ts));
		map_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		map_list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int index = map_list.getSelectedIndex();
				Map map = map_provider.getMap(index);
				game_creator.setMap(map);
			}
		});
		JScrollPane sp_map_list = new JScrollPane(map_list);
		sp_map_list.setBounds(ts / 2, ts / 2, ts * 6, height - ts);
		sp_map_list.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		this.add(sp_map_list);
		btn_refresh.setBounds(ts * 7, height - ts, ts * 3, ts / 2);
		btn_refresh.registerKeyboardAction(
				btn_refresh_listener,
				KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_MASK),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		btn_refresh.addActionListener(btn_refresh_listener);
		this.add(btn_refresh);
		btn_preview.setBounds(ts * 10 + ts / 2, height - ts, ts * 3, ts / 2);
		btn_preview.registerKeyboardAction(
				btn_preview_listener,
				KeyStroke.getKeyStroke(KeyEvent.VK_P, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		btn_preview.addActionListener(btn_preview_listener);
		this.add(btn_preview);
        this.mini_map.setDisplayUnits(false);
		this.add(mini_map);
	}

	public void setGameCreator(GameCreator creator) {
		creator.setGameCreatorListener(this);
		this.game_creator = creator;
	}

	public void setMapProvider(MapProvider provider) {
		this.map_provider = provider;
		this.mini_map.setVisible(false);
	}

	public void reloadMaps() {
		mini_map.setVisible(false);
		String[] maps = new String[map_provider.getMapCount()];
		for (int index = 0; index < maps.length; index++) {
			maps[index] = map_provider.getMapName(index);
			
		}
		map_list.setListData(maps);
		if(maps.length > 0) {
			map_list.setSelectedIndex(0);
		}
	}

	@Override
	public void onMapChanged(Map map) {
		updateButtons();
	}

	private void updateButtons() {
		btn_play.setEnabled(game_creator.canCreate());
	}

	@Override
	public void paint(Graphics g) {
		paintBackground(g);
		super.paint(g);
		ResourceUtil.paintBorder(g, 0, 0, getWidth(), getHeight());
	}

	private void paintBackground(Graphics g) {
		g.setColor(ResourceManager.getAEIIPanelBg());
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	private final ActionListener btn_back_listener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			getContext().gotoMainMenuScreen();
		}
	};

	private final ActionListener btn_play_listener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			Game game = game_creator.create();
			getContext().gotoGameScreen(game, AEIIApplet.MODE_LOCAL);
            AudioManager.stopBgm();
		}
	};

	private final ActionListener btn_refresh_listener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			reloadMaps();
		}
	};

	private final ActionListener btn_preview_listener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(mini_map.isVisible()) {
				mini_map.setVisible(false);
			} else {
				mini_map.display(game_creator.getMap());
			}
		}
	};

}
