
package com.toyknight.aeii.core.animation;

import java.util.ArrayList;

/**
 *
 * @author toyknight
 */
public class Animation {
	
	private int interval = 0;
	private int current_delay = 0;
	private boolean is_complete = false;
	private final ArrayList<AnimationListener> listeners = new ArrayList();
	
	public final void addAnimationListener(AnimationListener listener) {
		listeners.add(0, listener);
	}
	
	public boolean isComplete() {
		return is_complete;
	}
	
	protected final void complete() {
		for(AnimationListener listener: listeners) {
			listener.animationCompleted(this);
		}
		this.is_complete = true;
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
	
}
