package SQLExplorer.ui;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.Statement;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import SQLExplorer.db.Connect;
import SQLExplorer.db.UISQLException;

public class Init extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextField host, port, username, password;
	private JLabel lhost, lport, lusername, lpassword;
	private JButton conn;
	private Statement statement = null;

	public Init() {
		super("Connect to Server");
		setSize(350, 210);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		JPanel panel = new JPanel();
		add(panel);
		placeComponents(panel);
		conn.getActionMap().put("Enter", action);
		conn.addActionListener(action);
		setLocationRelativeTo(null);
	}

	private void placeComponents(JPanel panel) {

		panel.setLayout(null);

		lhost = new JLabel("Host");
		lhost.setBounds(10, 10, 80, 25);
		panel.add(lhost);

		host = new JTextField("localhost", 20);
		host.setBounds(100, 10, 230, 25);
		panel.add(host);

		lport = new JLabel("Port");
		lport.setBounds(10, 40, 80, 25);
		panel.add(lport);

		port = new JTextField("3306", 20);
		port.setBounds(100, 40, 230, 25);
		panel.add(port);

		lusername = new JLabel("User");
		lusername.setBounds(10, 70, 80, 25);
		panel.add(lusername);

		username = new JTextField("root", 20);
		username.setBounds(100, 70, 230, 25);
		panel.add(username);

		lpassword = new JLabel("Password");
		lpassword.setBounds(10, 100, 80, 25);
		panel.add(lpassword);

		password = new JPasswordField(20);
		password.setBounds(100, 100, 230, 25);
		panel.add(password);

		conn = new JButton("Connect");
		conn.setBounds(130, 140, 120, 25);
		conn.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
		panel.add(conn);
	}

	final private Action action = new AbstractAction() {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			final Connect connect = new Connect(host.getText(),
					Integer.valueOf(port.getText()), username.getText(),
					password.getText());
			try {
				statement = connect.createStatement();
			} catch (UISQLException ex) {
				JOptionPane.showMessageDialog(Init.this, ex.getMessage()
						.toString(), "Error", JOptionPane.ERROR_MESSAGE);
			}
			if (statement == null) {
				password.setText("");
			} else {
				getContentPane().removeAll();
				dispose();				
				new UI(statement, username.getText(), password.getText());
			}
		}
	};
}
