package com.toyknight.aeii.gui.screen;

import com.toyknight.aeii.Language;
import com.toyknight.aeii.core.Game;
import com.toyknight.aeii.core.SuffixFileFilter;
import com.toyknight.aeii.core.creator.GameCreator;
import com.toyknight.aeii.core.creator.GameCreatorListener;
import com.toyknight.aeii.gui.AEIIApplet;
import com.toyknight.aeii.gui.ResourceManager;
import com.toyknight.aeii.gui.Screen;
import com.toyknight.aeii.gui.control.AEIIButton;
import com.toyknight.aeii.gui.screen.internal.MapListCellRenderer;
import com.toyknight.aeii.gui.util.ResourceUtil;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

/**
 *
 * @author toyknight
 */
public class GameCreateScreen extends Screen implements GameCreatorListener {

	private final AEIIButton btn_back = new AEIIButton(Language.getText("LB_BACK"));
	private final AEIIButton btn_next = new AEIIButton(Language.getText("LB_NEXT"));
	private final JList map_list = new JList();

	private GameCreator creator;

	public GameCreateScreen(Dimension size, AEIIApplet context) {
		super(size, context);
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
		btn_next.setBounds(width - ts * 3 - ts / 2, height - ts, ts * 3, ts / 2);
		btn_next.registerKeyboardAction(
				btn_next_listener,
				KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		btn_next.addActionListener(btn_next_listener);
		map_list.setFocusable(false);
		map_list.setBackground(Color.DARK_GRAY);
		map_list.setCellRenderer(new MapListCellRenderer(ts));
		map_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane sp_map_list = new JScrollPane(map_list);
		sp_map_list.setBounds(ts / 2, ts / 2, ts * 6, height - ts);
		sp_map_list.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		this.add(sp_map_list);
		this.add(btn_next);
	}

	public void setGameCreator(GameCreator creator) {
		this.creator = creator;
		this.creator.setGameCreatorListener(this);
	}

	public void reloadMaps() {
		String[] maps = creator.getMapList();
		if(maps.length > 0) {
			map_list.setListData(maps);
			creator.changeMap(0);
		}
		updateButtons();
	}
	
	@Override
	public void onMapChanged(int index) {
		map_list.setSelectedIndex(index);
		updateButtons();
	}
	
	private void updateButtons() {
		btn_next.setEnabled(creator.canCreate());
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
	private final ActionListener btn_next_listener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			Game game = creator.create();
			getContext().gotoGameScreen(game);
		}
	};

}
