
package com.toyknight.aeii.gui.animation;

import com.toyknight.aeii.gui.screen.MapCanvas;
import com.toyknight.aeii.gui.sprite.Sprite;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 *
 * @author toyknight
 */
public class Animation {
	
	private final int x;
	private final int y;
	private final ArrayList<AnimationListener> listeners;
	
	private int interval;
	private int current_delay;
	
	public Animation(int x, int y) {
		this.x = x;
		this.y = y;
		listeners = new ArrayList();
		setInterval(0);
	}
	
	public final int getX() {
		return x;
	}
	
	public final int getY() {
		return y;
	}
	
	public final void addAnimationListener(AnimationListener listener) {
		listeners.add(listener);
	}
	
	protected final void complete() {
		for(AnimationListener listener: listeners) {
			listener.animationCompleted(this);
		}
	}
	
	public final void setInterval(int interval) {
		this.interval = interval;
		current_delay = 0;
	}
	
	protected void doUpdate() {
		
	}
	
	public void update() {
		if (current_delay < interval) {
			current_delay++;
		} else {
			current_delay = 0;
			doUpdate();
		}
	}
	
	public void paint(Graphics g, MapCanvas canvas) {
	}
	
}
