
package com.toyknight.aeii.gui.animation;

/**
 *
 * @author toyknight
 */
public abstract class Animation {
	
	private AnimationListener listener = null;
	
	public final void setAnimationListener(AnimationListener listener) {
		this.listener = listener;
	}
	
	protected final void complete() {
		if(listener != null) {
			listener.animationCompleted(this);
		}
	}
	
	abstract public boolean isCompleted();
	
	abstract public void update();
	
}
