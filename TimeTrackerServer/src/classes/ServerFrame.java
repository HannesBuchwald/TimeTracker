package classes;

import java.awt.*;
import java.awt.event.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.*;

public class ServerFrame extends JFrame {

	private JTextField textField;
	public JLabel label;
	private JButton button;
	private boolean serverRunning = false;
	private TimeTrackerServer server;
	private String ip;
	private int defaultport = 4460;

	private ServerFrame() {
		createWindow();
	}

	public static void main(String[] args) {
		new ServerFrame();
	}

	public void createWindow() {
		JFrame frame = new JFrame("TimeTrackerServer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		InetAddress IP;
		try {
			IP = InetAddress.getLocalHost();
			ip = IP.getHostAddress();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}

		textField = new JTextField();
		textField.setHorizontalAlignment(JTextField.CENTER);
		textField.setText(String.valueOf(defaultport));

		label = new JLabel("label", SwingConstants.CENTER);
		label.setText("<html>Server NOT running!<br>Port: "
				+ textField.getText() + "<br>IP: " + ip + "</html>");
		label.setOpaque(true);
		label.setBackground(Color.red);

		button = new JButton();
		button.setText("Start");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!serverRunning) {
					serverRunning = true;
					label.setText("<html>Server running!<br>Port: "
							+ textField.getText() + "<br>IP: " + ip + "</html>");
					label.setBackground(Color.green);
					button.setText("Stop");
					startServer();
				} else {
					serverRunning = false;
					label.setText("<html>Server NOT running!<br>Port: "
							+ textField.getText() + "<br>sIP: " + ip
							+ "</html>");
					label.setBackground(Color.red);
					button.setText("Start");
					server.stop();
				}
			}
		});

		frame.setLayout(new GridLayout(0, 1));

		frame.add(textField);
		frame.add(label);
		frame.add(button);

		frame.setSize(300, 200);
		frame.setResizable(false);

		frame.setVisible(true);
	}

	public void startServer() {
		try {
			server = new TimeTrackerServer(
					Integer.parseInt(textField.getText()),label);
		} catch (NumberFormatException e) {
			System.out.println("Not a number in textfield!");
			server = new TimeTrackerServer(defaultport,label);
			label.setText("<html>Server running!<br>Port: " + defaultport + "<br>IP: "
					+ ip + "</html>");
			textField.setText(String.valueOf(defaultport));
		}
		new Thread(server).start();
	}

}
