package com.toyknight.aeii.gui.screen;

import com.toyknight.aeii.core.animation.Animation;
import com.toyknight.aeii.core.animation.AnimationListener;
import com.toyknight.aeii.gui.AEIIApplet;
import com.toyknight.aeii.gui.Screen;
import com.toyknight.aeii.gui.animation.TitleAnimation;
import com.toyknight.aeii.gui.animation.TitleGlowAnimation;
import com.toyknight.aeii.gui.screen.internal.MainMenu;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 *
 * @author toyknight
 */
public class MainMenuScreen extends Screen {
	
	private final TitleAnimation title_merge_animation;
	private final TitleGlowAnimation title_glow_animation;
	
	private final MainMenu menu;
	private boolean title_shown = false;

	public MainMenuScreen(Dimension size, AEIIApplet context) {
		super(size, context);
		title_merge_animation = new TitleAnimation();
		title_merge_animation.setInterval(1);
		title_merge_animation.addAnimationListener(new AnimationListener() {
			@Override
			public void animationCompleted(Animation animation) {
				showMenu();
			}
		});
		title_glow_animation = new TitleGlowAnimation(this);
		title_glow_animation.setInterval(1);
		menu = new MainMenu(context);
	}
	
	@Override
	public void initComponents() {
		this.setLayout(null);
		this.menu.initComponents(ts, this);
		this.menu.setVisible(false);
		this.add(menu);
	}
	
	private void showMenu() {
		title_shown = true;
		this.menu.setVisible(true);
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		if(title_shown) {
			title_glow_animation.paint(g);
		} else {
			title_merge_animation.paint(g);
		}
		super.paint(g);
	}

	@Override
	public void update() {
		if(title_shown) {
			title_glow_animation.update();
		} else {
			title_merge_animation.update();
		}
	}

}
