
package com.toyknight.aeii.gui;

import com.toyknight.aeii.gui.animation.Animation;
import com.toyknight.aeii.gui.animation.AnimationListener;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.JDesktopPane;

/**
 *
 * @author toyknight
 */
public class Screen extends JDesktopPane {
	
	private final AEIIApplet context;
	
	private final HashMap<Point, Animation> animations;

	public Screen(Dimension size, AEIIApplet context) {
		this.setPreferredSize(size);
		this.context = context;
		this.setOpaque(false);
		animations = new HashMap();
	}
	
	protected final void submitAnimation(Animation animation) {
		Point location = animation.getLocation();
		animation.addAnimationListener(new AnimationListener() {
			@Override
			public void animationCompleted(Animation animation) {
				animations.remove(animation.getLocation());
			}
		});
		animations.put(location, animation);
	}
	
	protected final Animation getAnimation(int x, int y) {
		return animations.get(new Point(x, y));
	}
	
	protected final boolean isAnimating() {
		return !animations.isEmpty();
	}
	
	protected final void updateAnimation() {
		for (Map.Entry entry : animations.entrySet()) {
			((Animation)entry.getValue()).update();
		}
	}
	
	protected final AEIIApplet getContext() {
		return context;
	}
	
	public void onKeyPress(KeyEvent e) {
		
	}
	
	public void onKeyRelease(KeyEvent e) {
		
	}
	
	public void update() {
	}

}
