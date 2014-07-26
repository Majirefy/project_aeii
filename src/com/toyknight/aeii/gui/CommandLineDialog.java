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
import java.util.logging.Level;
import java.util.logging.Logger;
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
				if (in.hasNextInt()) {
					args.add(in.nextInt());
					continue;
				}
				if (in.hasNextBoolean()) {
					args.add(in.nextBoolean());
					continue;
				}
				if (in.hasNext()) {
					args.add(in.next());
				}
			}
			invoke(cmd, args.toArray());
		}
	}

	private Class<?>[] getParamClass(Object[] args) {
		Class<?>[] param_classes = new Class<?>[args.length];
		for (int i = 0; i < args.length; i++) {
			param_classes[i] = args[i].getClass();
		}
		return param_classes;
	}

	private void invoke(String cmd, Object... args) {
		try {
			Class cmd_wrapper = CommandWrapper.class;
			Method method = cmd_wrapper.getMethod(cmd, getParamClass(args));
			if (method != null) {
				method.invoke(command_wrapper, args);
			}
		} catch (SecurityException |
				IllegalAccessException |
				IllegalArgumentException |
				InvocationTargetException | 
				NoSuchMethodException ex) {
			System.err.println(ex.getClass().toString()+": "+ex.getMessage());
		}
	}

	private class CommandWrapper {

		public void exit() {
			Launcher.exit();
		}

		public void setspeed(Integer speed) {
			context.setCurrentFpsDelay(1000 / speed);
		}

		public void cmdtest(String msg) {
			System.out.println(msg);
		}
		
		public void testmap(String map_name) {
			try {
				File map_file = new File("map\\"+map_name);
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
