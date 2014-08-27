
package com.toyknight.aeii.gui.animation;

import com.toyknight.aeii.gui.screen.MapCanvas;
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
	
	public Animation(int x, int y) {
		this.x = x;
		this.y = y;
		listeners = new ArrayList();
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
	
	public void update() {
	}
	
	public void paint(Graphics g, MapCanvas canvas) {
	}
	
}
