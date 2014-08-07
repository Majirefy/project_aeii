
package com.toyknight.aeii.gui.animation;

import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author toyknight
 */
public abstract class Animation {
	
	private final int x;
	private final int y;
	private final ArrayList<AnimationListener> listeners;
	
	public Animation(int x, int y) {
		this.x = x;
		this.y = y;
		listeners = new ArrayList();
	}
	
	public final Point getLocation() {
		return new Point(x, y);
	}
	
	public final void addAnimationListener(AnimationListener listener) {
		listeners.add(listener);
	}
	
	protected final void complete() {
		for(AnimationListener listener: listeners) {
			listener.animationCompleted(this);
		}
	}
	
	abstract public void update();
	
}
