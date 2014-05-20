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
			if (action.equals("empty")) {
				for (int i = 0; i < names.size(); i++) {
					ui.statement.executeUpdate(String.format(
							"TRUNCATE TABLE %s", names.get(i).toString()));
				}
			} else if (!action.equals("drop")) {
				ResultSet result = ui.statement.executeQuery(String.format(
						"%s TABLE %s", action.toUpperCase(),
						ui.join(names, ",")));
				while (result.next()) {

				}
			} else {
				ui.statement.executeUpdate(String.format(
						"DROP TABLE IF EXISTS %s", ui.join(names, ",")));
			}
		} catch (SQLException e) {			
			throw new UISQLException(e.getMessage());
		}
	}
}
