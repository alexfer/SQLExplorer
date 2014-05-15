package SQLExplorer.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import SQLExplorer.ui.MakeUI;

public class Query {
	private ResultSet result = null;
	private MakeUI ui;

	public Query(MakeUI ui) {
		this.ui = ui;
	}

	public List<Object> listDatabases() {
		List<Object> list = new ArrayList<Object>();
		try {
			result = ui.statement.executeQuery("SHOW DATABASES");
			while (result.next()) {
				list.add(result.getString("Database"));
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(ui, e.getMessage().toString(),
					"Error", JOptionPane.ERROR_MESSAGE);
		}
		return list;
	}

	public List<ArrayList<String>> listTables(String db) {
		List<ArrayList<String>> rows = new ArrayList<ArrayList<String>>();
		final String[] columns = { "Name", "Rows", "Engine", "Collation",
				"Data_length", "Index_length", "Data_free" };
		try {
			ui.statement.executeQuery(String.format("USE `%s`", db));
			result = ui.statement.executeQuery("SHOW TABLE STATUS");
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
		return rows;
	}

	public void dropDatabase(String name) {
		try {
			ui.statement.executeUpdate(String
					.format("DROP DATABASE `%s`", name));
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(ui, e.getMessage().toString(),
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void addDatabase(String name) {
		try {
			ui.statement.executeUpdate(String.format("CREATE DATABASE `%s`",
					name));
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(ui, e.getMessage().toString(),
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public List<String> getMeta(ResultSet result) {
		List<String> list = new ArrayList<String>();
		try {
			for (int i = 1; i <= result.getMetaData().getColumnCount(); i++) {
				list.add(result.getMetaData().getColumnName(i));
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(ui, e.getMessage().toString(),
					"Error", JOptionPane.ERROR_MESSAGE);
		}
		return list;
	}
}
