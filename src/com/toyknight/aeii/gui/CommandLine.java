package com.toyknight.aeii.gui;

import com.toyknight.aeii.Launcher;
import com.toyknight.aeii.core.Game;
import com.toyknight.aeii.core.GameFactory;
import com.toyknight.aeii.core.map.Map;
import com.toyknight.aeii.core.map.MapFactory;
import com.toyknight.aeii.core.player.LocalPlayer;
import com.toyknight.aeii.core.player.Player;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author toyknight
 */
public class CommandLine extends Thread {

	private final AEIIApplet context;
	private final CommandWrapper command_wrapper;
	private final BufferedReader cin;

	public CommandLine(AEIIApplet context) {
		super("command line thread");
		this.context = context;
		this.command_wrapper = new CommandWrapper();
		this.cin = new BufferedReader(new InputStreamReader(System.in));
	}

	@Override
	public void run() {
		while (context.isRunning()) {
			System.out.print(">");
			try {
				excute(cin.readLine());
			} catch (IOException ex) {
				//do nothing
			}
		}
	}

	public void excute(String input) {
		if (input != null) {
			Scanner in = new Scanner(input);
			if (in.hasNext()) {
				String cmd = in.next();
				ArrayList<Object> args = new ArrayList();
				while (in.hasNext()) {
					args.add(in.next());
				}
				invoke(cmd, args.toArray());
			}
		}
	}

	private Object[] convertArgs(Method method, Object[] args) {
		Class<?>[] param_types = method.getParameterTypes();
		Object[] converted_args = new Object[param_types.length];
		for (int i = 0; i < param_types.length; i++) {
			if (param_types[i].equals(int.class)) {
				converted_args[i] = Integer.parseInt((String) args[i]);
			}
			if (param_types[i].equals(boolean.class)) {
				converted_args[i] = Boolean.parseBoolean((String) args[i]);
			}
			if (param_types[i].equals(String.class)) {
				converted_args[i] = args[i];
			}
		}
		return converted_args;
	}

	private Method getMethod(String name, int param_count) {
		Class cmd_wrapper = CommandWrapper.class;
		for (Method method : cmd_wrapper.getDeclaredMethods()) {
			if (method.getName().equals(name)
					&& method.getParameterTypes().length == param_count) {
				return method;
			}
		}
		return null;
	}

	private void invoke(String cmd, Object... args) {
		try {
			Method method = getMethod(cmd, args.length);
			if (method != null) {
				method.invoke(command_wrapper, convertArgs(method, args));
			}
		} catch (SecurityException |
				IllegalAccessException |
				IllegalArgumentException |
				InvocationTargetException ex) {
			System.err.println(ex.getClass().toString() + ": " + ex.getMessage());
		}
	}

	private class CommandWrapper {

		public void exit() {
			Launcher.exit();
		}

		public void setspeed(int speed) {
			context.setCurrentFpsDelay(1000 / speed);
		}

		public void cmdtest(String msg) {
			System.out.println(msg);
		}

		public void maptest(String map_name) {
			try {
				File map_file = new File("map/" + map_name);
				Map map = MapFactory.createMap(map_file);
				GameFactory game_factory = new GameFactory(map);
				Player[] players = new Player[4];
				for (int team = 0; team < 4; team++) {
					players[team] = new LocalPlayer();
					players[team].setAlliance(team);
					players[team].setGold(1000);
				}
				Game game = game_factory.createBasicGame(players, 10);
				context.getGameScreen().setGame(game);
				context.setCurrentScreen(AEIIApplet.ID_GAME_SCREEN);
			} catch (IOException ex) {
				System.err.println(ex.getClass().toString() + ": " + ex.getMessage());
			}
		}

	}

}
