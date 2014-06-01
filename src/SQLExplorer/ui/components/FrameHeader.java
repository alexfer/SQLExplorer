package SQLExplorer.ui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import SQLExplorer.db.Query;
import SQLExplorer.db.UISQLException;
import SQLExplorer.ui.UI;
import SQLExplorer.ui.DropDatabase;
import SQLExplorer.ui.tool.Export;
import SQLExplorer.ui.tool.Import;

public class FrameHeader extends JPanel {

	private static final long serialVersionUID = 1L;
	public static JButton drop, imp, export;
	private UI ui;

	public FrameHeader(UI ui) {
		super(new FlowLayout(FlowLayout.LEFT));
		this.ui = ui;
	}

	public void render() {

		add(new JLabel("", new ImageIcon(getClass().getResource(
				"/resources/icons/database.png")), SwingConstants.LEFT));

		// Render database list
		try {
			List<Object> dbs = new Query(ui).listDatabases();
			ui.database = new JComboBox<Object>(
					new DefaultComboBoxModel<Object>(dbs.toArray()));
		} catch (UISQLException e) {
			JOptionPane.showMessageDialog(ui, e.getMessage().toString(),
					"Error", JOptionPane.ERROR_MESSAGE);
		}
		add(ui.database);
		ui.database.setPreferredSize(new Dimension(300, 25));

		// Adding delete database button
		drop = new JButton("Drop", new ImageIcon(getClass().getResource(
				"/resources/icons/database_delete.png")));
		add(drop);
		drop.addActionListener(new DropDatabase(ui));

		// Export database
		export = new JButton("Export", new ImageIcon(getClass().getResource(
				"/resources/icons/table_row_insert.png")));
		add(export);
		export.addActionListener(new Export(ui));

		// Import database
		imp = new JButton("Import", new ImageIcon(getClass().getResource(
				"/resources/icons/table_row_delete.png")));
		add(imp);
		imp.addActionListener(new Import(ui));

		// Add header panel to layout
		ui.add(this, BorderLayout.NORTH);
		setBackground(Color.lightGray);

		// Set frame title
		ui.setTitle(String.format("%s - %s", UI.title, ui.database
				.getSelectedItem().toString()));
	}
}
