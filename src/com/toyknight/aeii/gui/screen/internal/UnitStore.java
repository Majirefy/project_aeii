package com.toyknight.aeii.gui.screen.internal;

import com.toyknight.aeii.core.GameManager;
import com.toyknight.aeii.gui.AEIIPanel;
import com.toyknight.aeii.gui.screen.MapCanvas;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.KeyStroke;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

/**
 *
 * @author toyknight
 */
public class UnitStore extends JInternalFrame {

	private final MapCanvas canvas;
	private GameManager manager;

	public UnitStore(MapCanvas canvas) {
		super("Buy Unit", false, true, false);
		this.canvas = canvas;
		this.setBorder(BorderFactory.createEmptyBorder());
		this.getContentPane().setPreferredSize(new Dimension(400, 300));
		this.getContentPane().add(new AEIIPanel());
		this.pack();
		this.setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		this.getRootPane().registerKeyboardAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				close();
			}
		}, stroke, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		this.addInternalFrameListener(new FrameListener());
	}

	public void setGameManager(GameManager manager) {
		this.manager = manager;
	}

	public void close() {
		canvas.setInternalFrameShown(false);
		setVisible(false);
	}

	private class FrameListener extends InternalFrameAdapter {

		@Override
		public void internalFrameClosing(InternalFrameEvent e) {
			close();
		}

	}

}
