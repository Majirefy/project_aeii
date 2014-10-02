
package com.toyknight.aeii.gui.control;

import com.toyknight.aeii.gui.ResourceManager;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import javax.swing.JButton;

/**
 *
 * @author toyknight
 */
public class ActionButton extends JButton {
	
	private final int index;
	
	public ActionButton(int index) {
		super();
		this.index = index;
		this.setFocusable(false);
		this.setContentAreaFilled(false);
		this.setBorder(BorderFactory.createEmptyBorder());
	}
	
	@Override
	public void paint(Graphics g) {
		if(getModel().isArmed()) {
			g.drawImage(ResourceManager.getActionButtonImage(index, 1), 0, 0, this);
		} else {
			g.drawImage(ResourceManager.getActionButtonImage(index, 0), 0, 0, this);
		}
		super.paint(g);
	}
	
}
