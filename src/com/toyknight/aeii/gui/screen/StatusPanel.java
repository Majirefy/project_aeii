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
	private final int i2w;
	private final int margin;
	
	private GameManager manager;

	public StatusPanel(int ts) {
		this.ts = ts;
		this.i2w = ts / 24 * 11;
		this.margin = (ts - i2w) / 2;
	}
	
	public void setGameManager(GameManager manager) {
		this.manager = manager;
	}

	@Override
	public void paintComponent(Graphics g) {
		paintGold(g);
		paintPopulation(g);
	}

	private void paintGold(Graphics g) {
		int interval = ts;
		int gold = manager.getGame().getCurrentPlayer().getGold();
		g.drawImage(ResourceManager.getGoldIcon(), interval, margin, this);
		CharPainter.paintLNumber(g, gold, interval + i2w + margin, margin);
	}

	private void paintPopulation(Graphics g) {
		int interval = ts * 5;
		int max = manager.getGame().getMaxPopulation();
		int current = manager.getGame().getCurrentPlayer().getPopulation();
		g.drawImage(ResourceManager.getPopulationIcon(), interval, margin, this);
		CharPainter.paintLFraction(g, current, max, interval + i2w + margin, margin);
	}
	
	@Override
	protected void paintBackground(Graphics g) {
		if (manager != null) {
			int team = manager.getGame().getCurrentTeam();
			g.setColor(ResourceManager.getTeamColor(team));
		} else {
			g.setColor(ResourceManager.getAEIIPanelBg());
		}
		g.fillRect(0, 0, getWidth(), getHeight());
	}

}
