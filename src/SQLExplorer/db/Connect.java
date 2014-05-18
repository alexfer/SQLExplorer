package SQLExplorer.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Connect {
	private String host, user, password;
	private int port;
	private Connection connect = null;
	private Statement statement;

	public Connect(String host, Integer port, String user, String password) {
		this.host = host;
		this.port = port;		
		this.user = user;
		this.password = password;
	}

	public Statement createStatement(JFrame frame) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection("jdbc:mysql://" + this.host
					+ ":" + this.port + "/?user=" + this.user
					+ "&password=" + this.password);
			statement = connect.createStatement();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, e.getMessage().toString(),
					"Error", JOptionPane.ERROR_MESSAGE);
			statement = null;
		}
		return statement;
	}
}
