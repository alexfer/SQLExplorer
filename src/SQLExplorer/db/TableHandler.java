package SQLExplorer.db;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import SQLExplorer.ui.UI;

public class TableHandler {
	private UI ui;
	private ResultSet result = null;

	public TableHandler(final UI ui) {
		this.ui = ui;		
	}

	public void check(ArrayList<String> names) {		

		final List<ArrayList<String>> rows = new ArrayList<ArrayList<String>>();
		final String[] columns = { "Table", "Msg_type", "Msg_text" };
		final StringBuilder tables = new StringBuilder();

		for (String table : names) {			
			tables.append(table);
			tables.append(", ");
		}
		System.out.println(tables.toString());
	}

	public void optimize(ArrayList<String> names) {
		
		final List<ArrayList<String>> rows = new ArrayList<ArrayList<String>>();
		final String[] columns = { "Table", "Msg_type", "Msg_text" };
		final StringBuilder tables = new StringBuilder();

		for (String table : names) {
			tables.append(", ");
			tables.append(table);
		}
		
		/*
		try {
			result = ui.statement.executeQuery("OPTIMIZE TABLE");
			int j = 0;
			while (result.next()) {
				rows.add(j, new ArrayList<String>());
				for (int i = 0; i < columns.length; i++) {
					rows.get(j).add(result.getString(columns[i]));
				}
				j++;
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(ui, e.getMessage().toString(),
					"Error", JOptionPane.ERROR_MESSAGE);
		}
		*/
		//return rows;
	}

	protected void repair(ArrayList<String> names) {

	}

	protected void drop(ArrayList<String> names) {

	}

	protected void empty(ArrayList<String> names) {

	}
}
