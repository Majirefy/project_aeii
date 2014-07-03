package com.toyknight.aeii.gui;

import com.toyknight.aeii.Configuration;
import com.toyknight.aeii.gui.effect.ImageWaveEffect;
import com.toyknight.aeii.gui.screen.LogoScreen;
import java.awt.Dimension;
import java.awt.Frame;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author toyknight
 */
public class AEIIMainFrame extends Frame {

	private final int ROWS = 10;
	private final int COLUMNS = 15;
	private final int CANVAS_SCALE;
	private final int BASE_TILE_SIZE;
	private final Dimension BASE_CANVAS_SIZE;

	private GameCanvas canvas;
	
	private Screen current_screen;
	private LogoScreen logo_screen;

	public AEIIMainFrame(String title) {
		super(title);
		CANVAS_SCALE = Configuration.getCanvasScale();
		BASE_TILE_SIZE = Configuration.getBaseTileSize();
		BASE_CANVAS_SIZE = new Dimension(
				BASE_TILE_SIZE * COLUMNS,
				BASE_TILE_SIZE * ROWS);
	}

	public void init() throws IOException {
		this.setIconImage(ImageIO.read(getClass().getResource("gameicon.png")));
		canvas = new GameCanvas(BASE_CANVAS_SIZE, CANVAS_SCALE);
		this.add(canvas);

		this.setResizable(false);
		this.pack();
		this.setLocationRelativeTo(null);
		
		logo_screen = new LogoScreen(BASE_CANVAS_SIZE, this);
		
		ImageWaveEffect.createSinTab();
		
		current_screen = logo_screen;
	}
	
	public Screen getCurrentScreen() {
		return current_screen;
	}

	public GameCanvas getCanvas() {
		return canvas;
	}
	
}
