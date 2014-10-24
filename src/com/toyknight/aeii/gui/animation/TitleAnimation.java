
package com.toyknight.aeii.gui.animation;

import com.toyknight.aeii.core.animation.Animation;
import com.toyknight.aeii.gui.ResourceManager;
import com.toyknight.aeii.gui.effect.ImageWaveEffect;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author toyknight
 */
public class TitleAnimation extends Animation {
	
	private int logoDrapValue = 0;
	
	@Override
	protected void doUpdate() {
		if(logoDrapValue < 40) {
			logoDrapValue++;
		} else {
			complete();
		}
	}
	
	public void paint(Graphics g) {
		BufferedImage title_image=  ResourceManager.getTitleImage();
		ImageWaveEffect.drawWavedImage(
				g, logoDrapValue, 40,
				title_image, 0, 0, 1);
	}
	
}
