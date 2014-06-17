package SQLExplorer.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import SQLExplorer.ui.ErrorException;
import SQLExplorer.ui.UI;

public class Query {
	private ResultSet result = null;
	private UI ui;
	private List<Object> list = new ArrayList<Object>();

	public Query(UI ui) {
		this.ui = ui;
	}

	private String getCollationAt(String charset) throws ErrorException {
		String collation = null;
		try {
			result = ui.statement.executeQuery("SHOW CHARACTER SET LIKE '"
					+ charset + "'");
			if (result.next()) {
				collation = result.getString("Default collation");
			}
			result.close();
		} catch (SQLException e) {
			throw new ErrorException(e.getMessage());
		}
		return collation;
	}

	public List<Object> getCharactes() throws ErrorException {
		try {
			result = ui.statement.executeQuery("SHOW CHARACTER SET");
			while (result.next()) {
				list.add(result.getString("Charset"));
			}
			result.close();
		} catch (SQLException e) {
			throw new ErrorException(e.getMessage());
		}
		return list;
	}

	public List<Object> listDatabases() throws ErrorException {
		try {
			result = ui.statement.executeQuery("SHOW DATABASES");
			while (result.next()) {
				list.add(result.getString("Database"));
			}
			result.close();
		} catch (SQLException e) {
			throw new ErrorException(e.getMessage());
		}
		return list;
	}

	public List<ArrayList<String>> listTables(String db) throws ErrorException {
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
			result.close();
		} catch (SQLException e) {
			throw new ErrorException(e.getMessage());
		}
		return rows;
	}

	public void dropDatabase(String name) throws ErrorException {
		try {
			ui.statement.executeUpdate(String
					.format("DROP DATABASE `%s`", name));			
		} catch (SQLException e) {
			throw new ErrorException(e.getMessage());
		}
	}

	public void addDatabase(String name, String charset) throws ErrorException {
		try {
			ui.statement.executeUpdate(String.format(
					"CREATE DATABASE `%s` CHARACTER SET %s COLLATE %s", name,
					charset, getCollationAt(charset)));
		} catch (SQLException e) {
			throw new ErrorException(e.getMessage());
		}
	}

	public List<Object> getMeta(ResultSet result) throws ErrorException {
		try {
			for (int i = 1; i <= result.getMetaData().getColumnCount(); i++) {
				list.add(result.getMetaData().getColumnName(i));
			}
			result.close();
		} catch (SQLException e) {
			throw new ErrorException(e.getMessage());
		}
		return list;
	}
}
