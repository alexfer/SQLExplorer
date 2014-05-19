package SQLExplorer.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class DatabaseTables extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;

	DatabaseTables() {
		super();
		setOpaque(true);
	}

	public JTable render(List<ArrayList<String>> data) {

		final JTable table = new JTable() {
			private static final long serialVersionUID = 1L;

			@Override
			public Class<?> getColumnClass(int column) {
				switch (column) {
				case 0:
					return String.class;
				case 1:
					return Integer.class;
				case 2:
					return String.class;
				case 3:
					return String.class;
				case 4:
					return String.class;
				case 5:
					return String.class;
				default:
					return Boolean.class;
				}
			}
		};
		DefaultTableModel model = (DefaultTableModel) table.getModel();

		model.setColumnIdentifiers(new String[] { "Table", "Rows", "Type",
				"Collation", "Size", "Overhead", "" });
		
		table.setRowHeight(23);

		for (int i = 0; i < data.size(); i++) {
			model.insertRow(
					i,
					new Object[] {
							data.get(i).get(0),
							data.get(i).get(1),
							data.get(i).get(2),
							data.get(i).get(3),
							convert(Integer.parseInt(data.get(i).get(4))
									+ Integer.parseInt(data.get(i).get(5)),
									false),
							convert(data.get(i).get(2).equals("MyISAM") ? Integer
									.parseInt(data.get(i).get(6)) : 0, false),
							false });
		}
		
		int j = table.getColumnCount() -1;
		
		table.getColumnModel().getColumn(j).setPreferredWidth(0);		
		TableColumn tc = table.getColumnModel().getColumn(j);
        tc.setHeaderRenderer(new SelectAll(table, j));
		return table;
	}

	private String convert(int bytes, boolean si) {
		int unit = si ? 1000 : 1024;
		if (bytes < unit)
			return bytes + " B";
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = (si ? "kMGTPEZY" : "KMGTPEZY").charAt(exp - 1)
				+ (si ? "" : "i");
		return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}
}
