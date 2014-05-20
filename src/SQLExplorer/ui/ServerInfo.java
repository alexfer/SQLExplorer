package SQLExplorer.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class ServerInfo extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;

	ServerInfo() {
		super();
	}
	
	public JTable display(List<ArrayList<String>> data) {
		final JTable table = new JTable();
		//DefaultTableModel model = (DefaultTableModel) table.getModel();

		//model.setColumnIdentifiers(columns);

		table.setRowHeight(23);
		return table;
	}
}
