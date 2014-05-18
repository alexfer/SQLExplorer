package SQLExplorer.db;

import java.sql.ResultSet;
import java.util.ArrayList;

import SQLExplorer.ui.UI;

public class Handler {
	private UI ui;
	private ResultSet result = null;

	public Handler(final UI ui) {
		this.ui = ui;
	}

	public void action(ArrayList<String> names, String action) {
		System.out.println(String.format("%s TABLE", action.toUpperCase()));
		/*
		try {			
			result = ui.statement.executeQuery(String.format("%s TABLE %s", action.toUpperCase()));
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(ui, e.getMessage().toString(),
					"Error", JOptionPane.ERROR_MESSAGE);
		}
		*/		
	}
}
