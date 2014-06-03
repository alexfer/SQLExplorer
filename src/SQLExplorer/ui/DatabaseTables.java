package SQLExplorer.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import SQLExplorer.ui.tool.TUitl;

//import javax.swing.table.TableColumn;

public class DatabaseTables extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;

	public DatabaseTables() {
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
							TUitl.convert(Integer.parseInt(data.get(i).get(4))
									+ Integer.parseInt(data.get(i).get(5)),
									false),
									TUitl.convert(data.get(i).get(2).equals("MyISAM") ? Integer
									.parseInt(data.get(i).get(6)) : 0, false),
							false });
		}

		int j = table.getColumnCount() - 1;

		table.getColumnModel().getColumn(j).setPreferredWidth(0);

		table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {

				if (SwingUtilities.isLeftMouseButton(e)) {

				}

				if (SwingUtilities.isRightMouseButton(e)) {
					int row = table.rowAtPoint(e.getPoint());

					ListSelectionModel model = table.getSelectionModel();
					model.setSelectionInterval(row, row);
					if (e.isPopupTrigger()) {

						if (table.isRowSelected(row)) {
							popup().show(e.getComponent(), e.getX(), e.getY());
						}
					}
				}
			}
		});

		/*
		 * TableColumn tc = table.getColumnModel().getColumn(j);
		 * tc.setHeaderRenderer(new SelectAll(table, j));
		 */		
		return table;
	}

	private JPopupMenu popup() {
		final JPopupMenu popup = new JPopupMenu();

		final JMenuItem structure = new JMenuItem("Structure");
		structure.setIcon(new ImageIcon(getClass().getResource(
				"/resources/icons/table_relationship.png")));
		popup.add(structure);
		popup.addSeparator();
		final JMenuItem browse = new JMenuItem("Browse");
		browse.setIcon(new ImageIcon(getClass().getResource(
				"/resources/icons/table.png")));
		popup.add(browse);

		return popup;
	}
}
