package com.toyknight.aeii.gui;

import com.toyknight.aeii.Launcher;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
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
public class CommandLineWindow extends JDialog {

	private final AEIIApplet context;
	private JTextField tf_command;
	private CommandWrapper command_wrapper;

	public CommandLineWindow(AEIIApplet context) {
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
			ArrayList<String> arg_generator = new ArrayList();
			while (in.hasNext()) {
				arg_generator.add(in.next());
			}
			int arg_count = arg_generator.size();
			String args[];
			if (arg_count > 0) {
				args = new String[arg_count];
				for (int i = 0; i < args.length; i++) {
					args[i] = arg_generator.get(i);
				}
			} else {
				args = null;
			}
			invoke(cmd, args);
		}
	}

	private void invoke(String cmd, Object[] args) {
		Class commands = CommandWrapper.class;
		try {
			if (args != null) {
				Method method = commands.getMethod(cmd, String[].class);
				method.invoke(command_wrapper, args);
			} else {
				Method method = commands.getMethod(cmd);
				method.invoke(command_wrapper);
			}
		} catch (NoSuchMethodException | 
				SecurityException |
				IllegalAccessException |
				IllegalArgumentException |
				InvocationTargetException ex) {
		}
	}

	private class CommandWrapper {

		public void exit() {
			Launcher.exit();
		}

	}

}
