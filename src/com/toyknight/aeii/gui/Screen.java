
package com.toyknight.aeii.gui;

import com.toyknight.aeii.gui.animation.Animation;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import javax.swing.JDesktopPane;

/**
 *
 * @author toyknight
 */
public class Screen extends JDesktopPane {
	
	private final AEIIApplet context;
	
	private Animation animation;
	private boolean is_animating;

	public Screen(Dimension size, AEIIApplet context) {
		this.setPreferredSize(size);
		this.context = context;
		this.setOpaque(false);
	}
	
	protected final void doAnimation(Animation animation) {
		this.animation = animation;
		this.is_animating = true;
	}
	
	protected final boolean isAnimating() {
		return is_animating;
	}
	
	protected final AEIIApplet getContext() {
		return context;
	}
	
	public void onKeyPress(KeyEvent e) {
		
	}
	
	public void onKeyRelease(KeyEvent e) {
		
	}
	
	public void update() {
		if(animation != null && !animation.isCompleted()) {
			animation.update();
		}
	}

}
