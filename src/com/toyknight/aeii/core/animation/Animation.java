
package com.toyknight.aeii.core.animation;

import com.toyknight.aeii.core.Point;
import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author toyknight
 */
public class Animation {
	
	private final HashSet<Point> locations = new HashSet();
	private final ArrayList<AnimationListener> listeners = new ArrayList();
	
	public final void addLocation(int x, int y) {
		this.locations.add(new Point(x, y));
	}
	
	public final boolean hasLocation(int x, int y) {
		return locations.contains(new Point(x, y));
	}
	
	public final void addAnimationListener(AnimationListener listener) {
		listeners.add(0, listener);
	}
	
	protected final void complete() {
		for(AnimationListener listener: listeners) {
			listener.animationCompleted(this);
		}
	}
	
	public void update() {
	}
	
}
