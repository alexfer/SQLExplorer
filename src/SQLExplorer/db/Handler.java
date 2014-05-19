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
		try {
			ResultSet result = ui.statement.executeQuery(String.format(
					"%s TABLE %s", action.toUpperCase(), ui.join(names, ",")));

			while (result.next()) {

			}
		} catch (SQLException e) {
			throw new UISQLException(e.getMessage());
		}
	}
}
