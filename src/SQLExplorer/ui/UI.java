package SQLExplorer.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import SQLExplorer.db.Handler;
import SQLExplorer.db.Query;
import SQLExplorer.db.UISQLException;

public class UI extends JFrame {

	private static final long serialVersionUID = 6371680321859454577L;
	public Statement statement = null;
	protected JComboBox database;
	protected JPanel header, footer;
	protected static JComboBox<Object> handle;
	private JPanel layout;
	private Tables tables;
	private JTable table;
	private JScrollPane pane;
	private JButton drop;
	private JMenuBar menu;
	private JMenu server, help;
	private JMenuItem newDatabase, diconnect, contents, about;
	private static String title = "SQL Explorer";
	private String manualUrl = "http://dev.mysql.com/doc/#manual";
	private String[] excludeTable = { "mysql", "information_schema",
			"performance_schema" };

	public UI(Statement stmt) {
		super(title);

		statement = stmt;
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		menuBar();
		content();
		setSize(800, 600);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void menuBar() {
		menu = new JMenuBar();
		server = new JMenu("Server");
		menu.add(server);

		newDatabase = new JMenuItem("New Database");
		newDatabase.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				new NewDatabase(UI.this);
			}
		});
		server.add(newDatabase);

		server.addSeparator();

		diconnect = new JMenuItem("Diconnect");
		server.add(diconnect);

		diconnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				dispose();
			}
		});

		help = new JMenu("Help");
		menu.add(help);

		contents = new JMenuItem("Help Contents");
		help.add(contents);

		contents.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					Desktop.getDesktop().browse(new URL(manualUrl).toURI());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		help.addSeparator();
		about = new JMenuItem("About");
		help.add(about);

		about.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JOptionPane.showMessageDialog(UI.this, "MySQL Explorer v1.0",
						"About", JOptionPane.PLAIN_MESSAGE);
			}
		});

		setJMenuBar(menu);
	}

	private void footer() {
		footer = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel action = new JLabel("Action: ");
		footer.add(action);
		handle = new JComboBox<Object>(new String[] { "--------", "Check",
				"Optimize", "Repair", "Truncate", "Drop" });
		footer.add(handle);

		if (in_array(excludeTable, database.getSelectedItem().toString())) {
			drop.setEnabled(false);
			handle.setEnabled(false);
		}
		handle.addActionListener(actionTable);
		add(footer, BorderLayout.SOUTH);
		footer.setBackground(Color.lightGray);
	}

	private void header() {
		header = new JPanel(new FlowLayout(FlowLayout.LEFT));
		header.add(new JLabel("", new ImageIcon(getClass().getResource(
				"/resources/icons/database.png")), SwingConstants.LEFT));

		try {
			List<Object> dbs = new Query(this).listDatabases();
			database = new JComboBox(new DefaultComboBoxModel(dbs.toArray()));
		} catch (UISQLException e) {
			JOptionPane.showMessageDialog(this, e.getMessage().toString(),
					"Error", JOptionPane.ERROR_MESSAGE);
		}

		header.add(database);
		database.setPreferredSize(new Dimension(300, 25));

		drop = new JButton("Drop");
		header.add(drop);

		drop.addActionListener(new DropDatabase(this));

		add(header, BorderLayout.NORTH);
		header.setBackground(Color.lightGray);
		setTitle(title + " - " + database.getSelectedItem().toString());
	}

	private void content() {
		header();
		layout = new JPanel();
		layout.setLayout(new BorderLayout());

		tables = new Tables();
		try {
			table = tables.render(new Query(this).listTables(database
					.getSelectedItem().toString()));
		} catch (UISQLException e) {
			JOptionPane.showMessageDialog(this, e.getMessage().toString(),
					"Error", JOptionPane.ERROR_MESSAGE);
		}
		layout.add(table.getTableHeader(), BorderLayout.NORTH);
		layout.add(table, BorderLayout.CENTER);
		pane = new JScrollPane(table);
		add(pane, BorderLayout.CENTER);
		database.addActionListener(changeDb);
		footer();
	}

	private Action actionTable = new AbstractAction() {

		private static final long serialVersionUID = 4034610359758014095L;

		@Override
		public void actionPerformed(ActionEvent e) {

			ArrayList<String> tables = new ArrayList<String>();

			for (int i = 0; i < table.getModel().getRowCount(); i++) {
				if ((Boolean) table.getValueAt(i, 6)) {
					tables.add(table.getValueAt(i, 0).toString());
				}
			}

			if (tables.size() > 0 && handle.getSelectedIndex() > 0) {
				Handler handler = new Handler(UI.this);
				try {
					handler.action(tables, handle.getSelectedItem().toString()
							.toLowerCase());
					pane.updateUI();
					validate();
					repaint();
				} catch (UISQLException ex) {
					JOptionPane.showMessageDialog(UI.this, ex.getMessage()
							.toString(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	};

	private Action changeDb = new AbstractAction() {

		private static final long serialVersionUID = 4034610359758014095L;

		@Override
		public void actionPerformed(ActionEvent e) {
			getContentPane().remove(pane);
			if (database.getSelectedIndex() >= 0) {
				drop.setEnabled(true);
				handle.setEnabled(true);
				String name = database.getSelectedItem().toString();
				setTitle(title + " - " + name);
				table.setModel(new DefaultTableModel());
				try {
					table = tables.render(new Query(UI.this).listTables(name));
				} catch (UISQLException ex) {
					JOptionPane.showMessageDialog(UI.this, ex.getMessage()
							.toString(), "Error", JOptionPane.ERROR_MESSAGE);
				}
				table.setFillsViewportHeight(true);
				layout.add(table.getTableHeader(), BorderLayout.NORTH);
				layout.add(table, BorderLayout.CENTER);
				pane = new JScrollPane(table);
				add(pane, BorderLayout.CENTER);
				handle.setSelectedIndex(0);
				if (in_array(excludeTable, name)) {
					drop.setEnabled(false);
					handle.setEnabled(false);
				}
			} else {
				setTitle(title + " - Undefined");
				drop.setEnabled(false);
				database.setEnabled(false);
				footer.setEnabled(false);
			}
			pane.updateUI();
			validate();
			repaint();
		}
	};

	private boolean in_array(String[] haystack, String needle) {
		for (int i = 0; i < haystack.length; i++) {
			if (haystack[i].equals(needle)) {
				return true;
			}
		}
		return false;
	}
}