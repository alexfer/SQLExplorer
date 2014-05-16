package SQLExplorer.ui;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class Tables extends DefaultTableCellRenderer {

	private static final long serialVersionUID = -6118025811358030095L;
	private JTable table;

	Tables() {
		super();
		setOpaque(true);
	}

	public JTable render(List<ArrayList<String>> data) {

		String[] columns = { "Table", "Rows", "Type", "Collation", "Size",
				"Overhead", "" };
		table = new JTable() {
			private static final long serialVersionUID = -2336506336532963484L;

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

		model.setColumnIdentifiers(columns);
		table.setIntercellSpacing(new Dimension(0, -3));
		table.setRowHeight(20);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

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
		table.getColumnModel().getColumn(6).setPreferredWidth(0);		
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