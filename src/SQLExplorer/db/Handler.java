package SQLExplorer.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import SQLExplorer.ui.UI;

public class Handler {
	private UI ui;	

	public Handler(final UI ui) {
		this.ui = ui;
	}

	public void action(ArrayList<String> names, String action)
			throws UISQLException {
		StringBuilder tables = new StringBuilder();
		for (int i = 0; i < names.size(); i++) {
			tables.append(names.get(i));
			tables.append(", ");
		}
		tables.delete(tables.length() - 2, tables.length());		

		try {
			ResultSet result = ui.statement.executeQuery(String.format("%s TABLE %s",
					action.toUpperCase(), tables.toString()));
		} catch (SQLException e) {
			throw new UISQLException(e.getMessage());
		}
	}
}
