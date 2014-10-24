package com.toyknight.aeii.gui.animation;

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
public class MessageAnimation extends MapAnimation {

	private int delay = 0;
	private float alpha = 1.0f;

	private final int ts;
	private final String message;

	public MessageAnimation(String message, int ts) {
		this.ts = ts;
		this.message = message;
	}

	@Override
	protected void doUpdate() {
		if (delay < 15) {
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
		int y = (canvas.getHeight() - ts) / 2;
		Graphics2D g2d = (Graphics2D) g;
		AlphaComposite acomp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
		g2d.setComposite(acomp);
		g2d.setColor(ResourceManager.getAEIIPanelBg());
		g2d.fillRect(0, y, canvas.getWidth(), ts);
		g2d.drawImage(ResourceManager.getBorderImage(1), 0, y, canvas.getWidth(), 16, canvas);
		g2d.drawImage(ResourceManager.getBorderImage(6), 0, y + ts - 16, canvas.getWidth(), 16, canvas);
		g2d.setFont(ResourceManager.getTitleFont());
		g2d.setColor(Color.WHITE);
		FontMetrics fm = g.getFontMetrics();
		((Graphics2D) g).setRenderingHint(
				RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.drawString(message,
				(canvas.getWidth() - fm.stringWidth(message)) / 2,
				y + (ts - fm.getHeight()) / 2 + fm.getAscent());
		((Graphics2D) g).setRenderingHint(
				RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
	}

}
