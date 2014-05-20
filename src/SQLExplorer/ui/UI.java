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
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;


import SQLExplorer.db.Handler;
import SQLExplorer.db.Query;
import SQLExplorer.db.UISQLException;
import SQLExplorer.ui.tool.Backup;
import SQLExplorer.ui.tool.Restore;

public class UI extends JFrame {

	private static final long serialVersionUID = 6371680321859454577L;
	public Statement statement = null;
	public JComboBox<Object> database;
	protected JPanel header, footer;
	protected static JComboBox<Object> handle;
	private JPanel layout;
	private JTable table;
	private JScrollPane pane;
	private JButton drop, backup, restore;
	private JMenuBar menu;
	private JMenu server, help;
	private JMenuItem newDatabase, info, diconnect, contents, about;
	final private static String title = "SQL Explorer";
	final private String manualUrl = "http://dev.mysql.com/doc/#manual";
	final private String[] excludeDbs = { "mysql", "information_schema",
			"performance_schema" };
	public String user, password;

	public UI(Statement stmt, String user, String password) {
		super(title);
		this.user = user;
		this.password = password;
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

		info = new JMenuItem("Database Server");
		info.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				// new NewDatabase(UI.this);
			}
		});
		server.add(info);

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
		footer.add(new JLabel("", new ImageIcon(getClass().getResource(
				"/resources/icons/database_go.png")), SwingConstants.LEFT));
		handle = new JComboBox<Object>(new String[] { "--------", "Check",
				"Optimize", "Repair", "Truncate", "Drop" });
		footer.add(handle);

		if (in_array(excludeDbs, database.getSelectedItem().toString())) {
			drop.setEnabled(false);
			handle.setEnabled(false);
		}
		handle.addActionListener(actionTable);
		add(footer, BorderLayout.SOUTH);
		footer.setBackground(Color.lightGray);
	}

	private void header() {
		// Create header panel
		header = new JPanel(new FlowLayout(FlowLayout.LEFT));
		header.add(new JLabel("", new ImageIcon(getClass().getResource(
				"/resources/icons/database.png")), SwingConstants.LEFT));

		// Render database list
		try {
			List<Object> dbs = new Query(this).listDatabases();
			database = new JComboBox<Object>(new DefaultComboBoxModel<Object>(
					dbs.toArray()));
		} catch (UISQLException e) {
			JOptionPane.showMessageDialog(this, e.getMessage().toString(),
					"Error", JOptionPane.ERROR_MESSAGE);
		}
		header.add(database);
		database.setPreferredSize(new Dimension(300, 25));

		// Adding delete database button
		drop = new JButton("Drop", new ImageIcon(getClass().getResource(
				"/resources/icons/database_delete.png")));
		header.add(drop);
		drop.addActionListener(new DropDatabase(this));

		// Backup database
		backup = new JButton("Backup", new ImageIcon(getClass().getResource(
				"/resources/icons/database_save.png")));
		header.add(backup);
		backup.addActionListener(new Backup(this));

		// Restore database
		restore = new JButton("Restore", new ImageIcon(getClass().getResource(
				"/resources/icons/database_save.png")));
		header.add(restore);
		restore.addActionListener(new Restore(this));

		// Add header panel to layout
		add(header, BorderLayout.NORTH);
		header.setBackground(Color.lightGray);

		// Set frame title
		setTitle(String.format("%s - %s", title, database.getSelectedItem()
				.toString()));
	}

	private void content() {
		// Append header to panel
		header();

		// Create panel
		layout = new JPanel();
		layout.setLayout(new BorderLayout());
		renderTableList(new DatabaseTables(), database.getSelectedItem()
				.toString());
		database.addActionListener(changeDb);

		// Append footer to panel
		footer();
	}

	private void renderTableList(DatabaseTables tables, String db) {
		try {
			table = tables.render(new Query(this).listTables(db));
		} catch (UISQLException e) {
			JOptionPane.showMessageDialog(this, e.getMessage().toString(),
					"Error", JOptionPane.ERROR_MESSAGE);
		}
		layout.add(table.getTableHeader(), BorderLayout.NORTH);
		layout.add(table, BorderLayout.CENTER);
		table.setFillsViewportHeight(true);
		table.updateUI();
		pane = new JScrollPane(table);
		add(pane, BorderLayout.CENTER);
	}

	private Action actionTable = new AbstractAction() {

		private static final long serialVersionUID = 4034610359758014095L;

		@Override
		public void actionPerformed(ActionEvent e) {

			ArrayList<String> selected = new ArrayList<String>();

			for (int i = 0; i < table.getModel().getRowCount(); i++) {
				if ((Boolean) table.getValueAt(i, 6)) {
					selected.add(table.getValueAt(i, 0).toString());
				}
			}

			if (selected.size() > 0 && handle.getSelectedIndex() > 0) {
				Handler handler = new Handler(UI.this);
				try {
					handler.action(selected, handle.getSelectedItem()
							.toString().toLowerCase());
				} catch (UISQLException ex) {
					JOptionPane.showMessageDialog(UI.this, ex.getMessage()
							.toString(), "Error", JOptionPane.ERROR_MESSAGE);
				}
				getContentPane().remove(pane);
				renderTableList(new DatabaseTables(), database
						.getSelectedItem().toString());
				pane.updateUI();
				validate();
				repaint();
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
				renderTableList(new DatabaseTables(), name);
				handle.setSelectedIndex(0);
				if (in_array(excludeDbs, name)) {
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

	public String join(ArrayList<?> parts, String separator) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < parts.size(); i++) {
			builder.append(parts.get(i));
			builder.append(separator.equals("") ? " " : separator);
		}
		builder.delete(builder.length() - 2, builder.length());
		return builder.toString();
	}
}