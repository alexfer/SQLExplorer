package SQLExplorer.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import SQLExplorer.ui.ErrorException;
import SQLExplorer.ui.UI;
import SQLExplorer.ui.tool.TUitl;

public class Handler implements Runnable {
	private UI ui;

	public Handler(final UI ui) {
		this.ui = ui;
	}

	public void action(ArrayList<String> names, String action)
			throws ErrorException {
		try {
			if (action.equals("empty")) {
				for (int i = 0; i < names.size(); i++) {
					ui.statement.executeUpdate(String.format(
							"TRUNCATE TABLE %s", names.get(i).toString()));					
				}
			} else if (!action.equals("drop")) {
				ResultSet result = ui.statement.executeQuery(String.format(
						"%s TABLE %s", action.toUpperCase(),
						TUitl.join(names, ",")));
				while (result.next()) {

				}
				result.close();
			} else {
				ui.statement.executeUpdate(String.format(
						"DROP TABLE IF EXISTS %s", TUitl.join(names, ",")));
			}
		} catch (SQLException e) {			
			throw new ErrorException(e.getMessage());
		}
	}

	@Override
	public void run() {
				
	}
}
