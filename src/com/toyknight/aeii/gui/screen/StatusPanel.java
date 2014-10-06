package com.toyknight.aeii.gui.screen;

import com.toyknight.aeii.core.GameManager;
import com.toyknight.aeii.gui.AEIIPanel;
import com.toyknight.aeii.gui.ResourceManager;
import com.toyknight.aeii.gui.util.CharPainter;
import java.awt.Graphics;

/**
 *
 * @author toyknight
 */
public class StatusPanel extends AEIIPanel {

	private final int ts;
	private GameManager manager;

	public StatusPanel(int ts) {
		this.ts = ts;
	}

	public void setGameManager(GameManager manager) {
		this.manager = manager;
	}

	@Override
	public void paintComponent(Graphics g) {
		int i2w = ts / 24 * 11;
		int lw = CharPainter.getLCharWidth();
		int margin = (getHeight() - i2w) / 2;
		int gold = manager.getGame().getCurrentPlayer().getGold();
		int max_population = manager.getGame().getMaxPopulation();
		int current_population = manager.getGame().getCurrentPlayer().getPopulation();
		g.drawImage(ResourceManager.getGoldIcon(), margin, margin, this);
		CharPainter.paintLNumber(g, gold, i2w + margin * 2, margin);
		int interval = ts * 5;
		g.drawImage(ResourceManager.getPopulationIcon(), interval, margin, this);
		CharPainter.paintLFraction(g, current_population, max_population, interval + i2w + margin, margin);
	}

}
