
package com.toyknight.aeii.gui.screen;

import com.toyknight.aeii.Launcher;
import com.toyknight.aeii.gui.AEIIMainFrame;
import static com.toyknight.aeii.gui.AEIIMainFrame.ID_MAIN_MENU_SCREEN;
import com.toyknight.aeii.gui.Screen;
import com.toyknight.aeii.gui.effect.ImageWaveEffect;
import com.toyknight.aeii.gui.util.ResourceUtil;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 *
 * @author toyknight
 */
public class LogoScreen extends Screen {
	
	private BufferedImage ms_logo;
	private Color bgColor = Color.WHITE;
	
	private int logoDrapValue = 40;
	
	private long wait_time = 0;
	
	private boolean logoShown = false;
	private boolean resourceLoaded = false;

	public LogoScreen(Dimension size, AEIIMainFrame context) {
		super(size, context);
		try {
			ms_logo = ResourceUtil.getImage("res/ms_logo.png");
		} catch (IOException ex) {
			ms_logo = new BufferedImage(88, 88, BufferedImage.TYPE_INT_ARGB);
			ms_logo.getGraphics().setColor(Color.BLACK);
			ms_logo.getGraphics().fillRect(0, 0, ms_logo.getWidth(), ms_logo.getHeight());
		}

	}
	
	public void setResourceLoaded(boolean b) {
		resourceLoaded = b;
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(bgColor);
		g.fillRect(0, 0, getWidth(), getHeight());
		int x = (getWidth() - ms_logo.getWidth())/2;
		int y = (getHeight() - ms_logo.getHeight())/2;
		ImageWaveEffect.drawWavedImage(g, logoDrapValue, 40, ms_logo, x, y, 4);

	}
	
	@Override
	public void update() {
		if(logoDrapValue > 0) {
			if(wait_time < 1700) {
				wait_time += Launcher.getCurrentFpsDelay();
			} else {
				logoShown = true;
			}
			if(logoShown && resourceLoaded) {
				logoDrapValue --;
			}
		}else {
			if(bgColor.equals(Color.BLACK)) {
				getContext().setCurrentScreen(ID_MAIN_MENU_SCREEN);
			}else {
				bgColor = new Color(bgColor.getRed()-17, bgColor.getGreen()-17, bgColor.getBlue()-17);
			}
		}
	}
	
}
