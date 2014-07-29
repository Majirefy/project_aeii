package com.toyknight.aeii.gui;

import com.toyknight.aeii.Launcher;
import com.toyknight.aeii.core.BasicGame;
import com.toyknight.aeii.core.GameFactory;
import com.toyknight.aeii.core.map.Map;
import com.toyknight.aeii.core.map.MapFactory;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

/**
 *
 * @author toyknight
 */
public class CommandLineDialog extends JDialog {

	private final AEIIApplet context;
	private JTextField tf_command;
	private CommandWrapper command_wrapper;

	public CommandLineDialog(AEIIApplet context) {
		super(Launcher.getWindow(), "Command Line");
		this.context = context;
		this.initComponents();
		this.setResizable(false);
		this.pack();
		this.setModal(true);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	private void initComponents() {
		this.getContentPane().setPreferredSize(new Dimension(500, 20));
		this.getContentPane().setLayout(null);
		tf_command = new JTextField();
		tf_command.setBounds(0, 0, 430, 20);
		this.add(tf_command);
		JButton btn_submit = new JButton("Submit");
		btn_submit.setBounds(430, 0, 70, 20);
		btn_submit.setFocusable(false);
		ActionListener btn_submit_listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				excute(tf_command.getText());
			}
		};
		btn_submit.addActionListener(btn_submit_listener);
		btn_submit.registerKeyboardAction(
				btn_submit_listener,
				KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		this.add(btn_submit);
		command_wrapper = new CommandWrapper();
	}

	public void display() {
		this.setLocationRelativeTo(getOwner());
		tf_command.setText("");
		this.setVisible(true);
	}

	public void excute(String input) {
		this.dispose();
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

	private Object[] convertArgs(Method method, Object[] args) {
		Class<?>[] param_types = method.getParameterTypes();
		Object[] converted_args = new Object[param_types.length];
		for (int i = 0; i < param_types.length; i++) {
			if (param_types[i].equals(int.class)) {
				converted_args[i] = Integer.parseInt((String) args[i]);
			}
			if(param_types[i].equals(boolean.class)) {
				converted_args[i] = Boolean.parseBoolean((String) args[i]);
			}
			if(param_types[i].equals(String.class)) {
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
			System.err.println(ex.getClass().toString() + ": \n" + ex.getMessage());
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
				File map_file = new File("map\\" + map_name);
				Map map = MapFactory.createMap(map_file);
				GameFactory game_factory = new GameFactory(map);
				BasicGame game = game_factory.createBasicGame();
				context.getGameScreen().setGame(game);
				context.setCurrentScreen(AEIIApplet.ID_GAME_SCREEN);
			} catch (IOException ex) {
				
			}
		}

	}

}
