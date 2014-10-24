package com.toyknight.aeii.gui.screen;

import com.toyknight.aeii.core.animation.Animation;
import com.toyknight.aeii.core.animation.AnimationListener;
import com.toyknight.aeii.gui.AEIIApplet;
import com.toyknight.aeii.gui.Screen;
import com.toyknight.aeii.gui.animation.LogoAnimation;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 *
 * @author toyknight
 */
public class LogoScreen extends Screen {

	private final LogoAnimation animation;

	private long wait_time = 0;
	private boolean resourceLoaded = false;

	public LogoScreen(Dimension size, AEIIApplet context) {
		super(size, context);
		animation = new LogoAnimation();
		animation.setInterval(1);
		animation.addAnimationListener(new AnimationListener() {
			@Override
			public void animationCompleted(Animation animation) {
				getContext().setCurrentScreen(AEIIApplet.ID_MAIN_MENU_SCREEN);
			}
		});
	}

	public void setResourceLoaded(boolean b) {
		resourceLoaded = b;
	}

	@Override
	public void paint(Graphics g) {
		animation.paint(g, this);
		super.paint(g);
	}

	@Override
	public void update() {
		if (wait_time < 1700) {
			wait_time += getContext().getCurrentFpsDelay();
		} else {
			if (resourceLoaded) {
				animation.update();
			}
		}
	}

}
