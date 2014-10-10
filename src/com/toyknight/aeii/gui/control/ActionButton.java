
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
	private final int icon_x;
	private final int icon_y;
	
	public ActionButton(int index, int ts) {
		super();
		this.index = index;
		int iw = ts / 24 * 16;
		int ih = ts / 24 * 16;
		int cw = ts / 24 * 20;
		int ch = ts / 24 * 21;
		this.icon_x = (cw - iw) / 2;
		this.icon_y = (ch - ih) / 2;
		this.setFocusable(false);
		this.setContentAreaFilled(false);
		this.setBorder(BorderFactory.createEmptyBorder());
	}
	
	@Override
	public void paint(Graphics g) {
		if(getModel().isArmed()) {
			g.drawImage(ResourceManager.getSmallCircleImage(1), 0, 0, this);
		} else {
			g.drawImage(ResourceManager.getSmallCircleImage(0), 0, 0, this);
		}
		g.drawImage(ResourceManager.getActionIcon(index), icon_x, icon_y, this);
		super.paint(g);
	}
	
}
