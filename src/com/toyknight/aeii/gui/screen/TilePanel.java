package com.toyknight.aeii.gui.screen;

import com.toyknight.aeii.core.BasicGame;
import com.toyknight.aeii.gui.AEIIPanel;
import com.toyknight.aeii.gui.ResourceManager;
import java.awt.Graphics;

/**
 *
 * @author toyknight
 */
public class TilePanel extends AEIIPanel {

	private final int ts;
	private final GameScreen game_screen;

	private int tile_index;

	public TilePanel(GameScreen game_screen, int ts) {
		this.ts = ts;
		this.game_screen = game_screen;
	}

	public void update() {
		int cursor_x = game_screen.getCanvas().getCursorXOnMap();
		int cursor_y = game_screen.getCanvas().getCursorYOnMap();
		tile_index = getGame().getMap().getTileIndex(cursor_x, cursor_y);
	}

	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(ResourceManager.getTileImage(tile_index), 0, 0, ts, ts, this);
	}
	
	private BasicGame getGame() {
		return game_screen.getGame();
	}

}
