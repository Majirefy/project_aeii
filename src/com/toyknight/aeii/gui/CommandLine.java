package com.toyknight.aeii.gui;

import com.toyknight.aeii.Launcher;
import java.io.BufferedReader;
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
	
	public AEIIApplet getContext() {
		return context;
	}

	@Override
	public void run() {
		while (getContext().isRunning()) {
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
			getContext().setCurrentFpsDelay(1000 / speed);
		}

	}

}
