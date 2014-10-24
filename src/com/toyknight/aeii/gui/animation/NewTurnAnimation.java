package com.toyknight.aeii.gui.animation;

import com.toyknight.aeii.Language;
import com.toyknight.aeii.gui.ResourceManager;
import com.toyknight.aeii.gui.screen.MapCanvas;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 *
 * @author toyknight
 */
public class NewTurnAnimation extends MapAnimation {

	private final int ts;
	private final int turn;
	private final int income;
	private final int team;

	private int delay = 0;
	private float alpha = 1.0f;

	public NewTurnAnimation(int turn, int income, int team, int ts) {
		this.ts = ts;
		this.turn = turn;
		this.income = income;
		this.team = team;
	}

	@Override
	protected void doUpdate() {
		if (delay < 30) {
			delay++;
		} else {
			if (alpha > 0.2f) {
				alpha -= 0.2f;
			} else {
				complete();
			}
		}
	}

	@Override
	public void paint(Graphics g, MapCanvas canvas) {
		int height = ts / 3 * 5;
		int y = (canvas.getHeight() - height) / 2;
		Graphics2D g2d = (Graphics2D) g;
		AlphaComposite acomp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
		g2d.setComposite(acomp);
		g2d.setColor(ResourceManager.getTeamColor(team));
		g2d.fillRect(0, y, canvas.getWidth(), height);
		g2d.drawImage(ResourceManager.getBorderImage(1), 0, y, canvas.getWidth(), 16, canvas);
		g2d.drawImage(ResourceManager.getBorderImage(6), 0, y + height - 16, canvas.getWidth(), 16, canvas);
		g2d.setFont(ResourceManager.getTitleFont());
		g2d.setColor(Color.WHITE);
		FontMetrics fm = g.getFontMetrics();
		((Graphics2D) g).setRenderingHint(
				RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		String str_turn = Language.getText("LB_TURN_COUNT") + turn;
		g.drawString(str_turn,
				(canvas.getWidth() - fm.stringWidth(str_turn)) / 2,
				y + (height / 2 - fm.getHeight()) / 2 + fm.getAscent() + 5);
		String str_income = income >= 0 ? Language.getText("LB_INCOME") + income : Language.getText("LB_INCOME") + "?";
		g.drawString(str_income,
				(canvas.getWidth() - fm.stringWidth(str_income)) / 2,
				y + height / 2 + (height / 2 - fm.getHeight()) / 2 + fm.getAscent() - 5);
		((Graphics2D) g).setRenderingHint(
				RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
	}

}
