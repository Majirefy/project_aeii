package com.toyknight.aeii.gui;

import com.toyknight.aeii.Configuration;
import com.toyknight.aeii.gui.effect.ImageWaveEffect;
import com.toyknight.aeii.gui.screen.LogoScreen;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 *
 * @author toyknight
 */
public class AEIIMainFrame extends JFrame {
	
	public static final String ID_LOGO_SCREEN = "logo";

	private final int ROWS = 10;
	private final int COLUMNS = 15;
	private final int SCREEN_SCALE;
	private final int BASE_TILE_SIZE;
	private final Dimension SCREEN_SIZE;

	private GameCanvas canvas;

	private Screen current_screen;
	private LogoScreen logo_screen;

	private CardLayout screen_container;

	public AEIIMainFrame(String title) {
		super(title);
		SCREEN_SCALE = Configuration.getCanvasScale();
		BASE_TILE_SIZE = Configuration.getBaseTileSize();
		SCREEN_SIZE = new Dimension(
				BASE_TILE_SIZE * COLUMNS * SCREEN_SCALE,
				BASE_TILE_SIZE * ROWS * SCREEN_SCALE);
	}

	public void init() throws IOException {
		this.setIconImage(ImageIO.read(getClass().getResource("gameicon.png")));
		ImageWaveEffect.createSinTab();
		
		screen_container = new CardLayout();
		this.getContentPane().setLayout(screen_container);
		logo_screen = new LogoScreen(SCREEN_SIZE, this);
		this.getContentPane().add(logo_screen, ID_LOGO_SCREEN);
		setCurrentScreen(ID_LOGO_SCREEN);

		this.setResizable(false);
		this.pack();
		this.setLocationRelativeTo(null);

	}

	public void setCurrentScreen(String screen_id) {
		screen_container.show(getContentPane(), screen_id);
		switch(screen_id) {
			case ID_LOGO_SCREEN:
				current_screen = logo_screen;
				break;
			default:
				break;
		}
	}

	public Screen getCurrentScreen() {
		return current_screen;
	}

	public GameCanvas getCanvas() {
		return canvas;
	}

	public int getTileSize() {
		return BASE_TILE_SIZE * SCREEN_SCALE;
	}
	
	public void setResourceLoaded() {
		logo_screen.setResourceLoaded(true);
	}

}
