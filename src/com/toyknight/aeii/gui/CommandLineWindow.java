package com.toyknight.aeii.gui;

import com.toyknight.aeii.Launcher;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

/**
 *
 * @author toyknight
 */
public class CommandLineWindow extends JDialog {

	private final AEIIMainFrame context;
	private JTextField tf_command;
	private CommandWrapper command_wrapper;

	public CommandLineWindow(AEIIMainFrame context) {
		super(context, "Command Line");
		this.context = context;
		this.initComponents();
		this.pack();
		this.setModal(true);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	private void initComponents() {
		this.getContentPane().setPreferredSize(new Dimension(300, 20));
		this.getContentPane().setLayout(null);
		tf_command = new JTextField();
		tf_command.setBounds(0, 0, 230, 20);
		this.add(tf_command);
		JButton btn_submit = new JButton("Submit");
		btn_submit.setBounds(230, 0, 70, 20);
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
		this.setLocationRelativeTo(context);
		this.setVisible(true);
	}

	public void excute(String input) {
		this.dispose();
		Scanner in = new Scanner(input);
		if (in.hasNext()) {
			try {
				String cmd = in.next();
				Class command = CommandWrapper.class;
				Method method = command.getMethod(cmd);
				method.invoke(command_wrapper);
			} catch (NoSuchMethodException |
					SecurityException |
					IllegalAccessException |
					IllegalArgumentException |
					InvocationTargetException ex) {
				JOptionPane.showMessageDialog(
						context, 
						ex.getMessage(), "Error", 
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private class CommandWrapper {

		public void exit() {
			Launcher.exit();
		}

	}

}
