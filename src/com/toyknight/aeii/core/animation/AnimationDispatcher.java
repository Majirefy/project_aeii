
package com.toyknight.aeii.core.animation;

/**
 *
 * @author toyknight
 */
public interface AnimationDispatcher {
	
	public void updateAnimation();
	
	public void submitAnimation(Animation animation);
	
	public Animation getCurrentAnimation();
	
	public boolean isAnimating();
	
}
