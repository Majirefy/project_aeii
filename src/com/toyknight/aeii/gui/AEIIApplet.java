package com.toyknight.aeii.gui;

import com.toyknight.aeii.Configuration;
import com.toyknight.aeii.Launcher;
import com.toyknight.aeii.audio.AudioManager;
import com.toyknight.aeii.core.AEIIException;
import com.toyknight.aeii.core.Game;
import com.toyknight.aeii.core.LocalGameManager;
import com.toyknight.aeii.core.creator.GameCreator;
import com.toyknight.aeii.core.map.LocalMapProvider;
import com.toyknight.aeii.core.map.MapProvider;
import com.toyknight.aeii.core.map.TileRepository;
import com.toyknight.aeii.core.unit.UnitFactory;
import com.toyknight.aeii.gui.effect.ImageWaveEffect;
import com.toyknight.aeii.gui.screen.GameCreateScreen;
import com.toyknight.aeii.gui.screen.GameScreen;
import com.toyknight.aeii.gui.screen.LogoScreen;
import com.toyknight.aeii.gui.screen.MainMenuScreen;
import com.toyknight.aeii.gui.screen.internal.MainMenu;
import com.toyknight.aeii.gui.util.CharPainter;
import com.toyknight.aeii.gui.util.DialogUtil;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.KeyEventPostProcessor;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author toyknight
 */
public class AEIIApplet {

    public static final String ID_LOGO_SCREEN = "logo";
    public static final String ID_MAIN_MENU_SCREEN = "main_menu";
    public static final String ID_GAME_CREATE_SCREEN = "game_create";
    public static final String ID_GAME_SCREEN = "game";

    public static final int MODE_LOCAL = 0x1;
    public static final int MODE_NET = 0x2;

    private final LocalMapProvider local_map_provider;

    private CommandLine command_line;

    private final Object FPS_LOCK = new Object();

    private boolean isRunning;
    private final boolean isDebugMode = true;

    private long fpsDelay;

    private Container content_pane;

    private final int TILE_SIZE;
    private final Dimension SCREEN_SIZE;

    private Screen current_screen;
    private LogoScreen logo_screen;
    private MainMenuScreen main_menu_screen;
    private GameCreateScreen game_create_screen;
    private GameScreen game_screen;

    private CardLayout screen_container;

    public AEIIApplet(int ts, int width, int height) {
        this.TILE_SIZE = ts;
        SCREEN_SIZE = new Dimension(width, height);
        fpsDelay = 1000 / Configuration.getGameSpeed();

        File map_dir = new File("map/");
        local_map_provider = new LocalMapProvider(map_dir);
    }

    public void init() {
        AudioManager.init();
        ImageWaveEffect.createSinTab();

        content_pane = new Container();

        screen_container = new CardLayout();
        this.getContentPane().setLayout(screen_container);
        logo_screen = new LogoScreen(SCREEN_SIZE, this);
        this.getContentPane().add(logo_screen, ID_LOGO_SCREEN);
        main_menu_screen = new MainMenuScreen(SCREEN_SIZE, this);
        main_menu_screen.initComponents();
        this.getContentPane().add(main_menu_screen, ID_MAIN_MENU_SCREEN);
        game_create_screen = new GameCreateScreen(SCREEN_SIZE, this);
        game_create_screen.initComponents();
        this.getContentPane().add(game_create_screen, ID_GAME_CREATE_SCREEN);
        game_screen = new GameScreen(SCREEN_SIZE, this);
        game_screen.initComponents();
        this.getContentPane().add(game_screen, ID_GAME_SCREEN);
        setCurrentScreen(ID_LOGO_SCREEN);

        CharPainter.init(TILE_SIZE);

        command_line = new CommandLine(this);

        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventPostProcessor(new GlobalKeyListener());
    }

    public boolean isRunning() {
        return isRunning;
    }

    public boolean isDebugMode() {
        return isDebugMode;
    }

    public void start() {
        isRunning = true;
        new Thread(new Animator(), "animation thread").start();
        new Thread(new Updater(), "update thread").start();
        //current_screen.repaint();
        loadResources();
    }

    public void stop() {
        isRunning = false;
    }

    private void loadResources() {
        new Thread("resource loading thread") {
            @Override
            public void run() {
                try {
                    File tile_data_dir = new File("data/tiles");
                    File unit_data_dir = new File("data/units");
                    TileRepository.init(tile_data_dir);
                    UnitFactory.init(unit_data_dir);
                    ResourceManager.init(getTileSize());
                    logo_screen.setResourceLoaded(true);
                    if (isDebugMode()) {
                        command_line.start();
                    }
                } catch (IOException | AEIIException ex) {
                    DialogUtil.showError(
                            Launcher.getWindow(),
                            ex.getClass().toString() + ": " + ex.getMessage());
                    Launcher.exit();
                }
            }
        }.start();
    }

    public Container getContentPane() {
        return content_pane;
    }

    public CommandLine getCommandLine() {
        return command_line;
    }

    public void setCurrentScreen(String screen_id) {
        screen_container.show(getContentPane(), screen_id);
        switch (screen_id) {
            case ID_LOGO_SCREEN:
                current_screen = logo_screen;
                break;
            case ID_MAIN_MENU_SCREEN:
                current_screen = main_menu_screen;
                break;
            case ID_GAME_CREATE_SCREEN:
                current_screen = game_create_screen;
                break;
            case ID_GAME_SCREEN:
                current_screen = game_screen;
                break;
            default:
                break;
        }
        current_screen.revalidate();
    }

    public void gotoMainMenuScreen() {
        setCurrentScreen(ID_MAIN_MENU_SCREEN);
        main_menu_screen.getMenu().showMenu(MainMenu.ID_WELCOME_MENU);
        AudioManager.stopBgm();
        AudioManager.playBgm("res/bgm/main_theme.ogg", true);
    }

    public void gotoGameCreateScreen(GameCreator creator, MapProvider provider) {
        setCurrentScreen(ID_GAME_CREATE_SCREEN);
        game_create_screen.setGameCreator(creator);
        game_create_screen.setMapProvider(provider);
        game_create_screen.reloadMaps();
    }

    public void gotoGameScreen(Game game, int mode) {
        switch (mode) {
            case MODE_LOCAL:
                LocalGameManager manager
                        = new LocalGameManager(game, game_screen.getAnimationProvicer());
                game_screen.setGameManager(manager);
                setCurrentScreen(ID_GAME_SCREEN);
                break;
            case MODE_NET:
                break;
            default:
            //do nothing;
        }
    }

    public Screen getCurrentScreen() {
        return current_screen;
    }

    public LocalMapProvider getLocalMapProvider() {
        return local_map_provider;
    }

//	public GameScreen getGameScreen() {
//		return game_screen;
//	}
    public int getTileSize() {
        return TILE_SIZE;
    }

    public void setCurrentFpsDelay(long delay) {
        synchronized (FPS_LOCK) {
            fpsDelay = delay;
        }
    }

    public long getCurrentFpsDelay() {
        synchronized (FPS_LOCK) {
            return fpsDelay;
        }
    }

    public Object getUpdateLock() {
        return content_pane.getTreeLock();
    }

    private void updateCurrentScreen() {
        synchronized (getUpdateLock()) {
            getCurrentScreen().update();
        }
    }

    private class Animator implements Runnable {

        @Override
        public void run() {
            while (isRunning) {
                getCurrentScreen().repaint();
                waitDelay(getCurrentFpsDelay());
            }
        }

        private void waitDelay(long time) {
            try {
                Thread.sleep(time);
            } catch (InterruptedException ex) {
            }
        }

    }

    private class Updater implements Runnable {

        @Override
        public void run() {
            while (isRunning) {
                long start_time = System.currentTimeMillis();
                //System.out.println("update");
                updateCurrentScreen();
                long end_time = System.currentTimeMillis();
                long current_fps_delay = getCurrentFpsDelay();
                if (end_time - start_time < current_fps_delay) {
                    waitDelay(current_fps_delay - (end_time - start_time));
                } else {
                    waitDelay(0);
                }
            }
        }

        private void waitDelay(long time) {
            try {
                Thread.sleep(time);
            } catch (InterruptedException ex) {
            }
        }

    }

    private class GlobalKeyListener implements KeyEventPostProcessor {

        @Override
        public boolean postProcessKeyEvent(KeyEvent e) {
            switch (e.getID()) {
                case KeyEvent.KEY_PRESSED:
                    current_screen.onKeyPress(e);
                    break;
                case KeyEvent.KEY_RELEASED:
                    current_screen.onKeyRelease(e);
                    break;
            }
            return false;
        }

    }

}
