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

import javax.swing.DefaultComboBoxModel;
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
import javax.swing.table.DefaultTableModel;

import SQLExplorer.db.Query;

public class MakeUI extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6371680321859454577L;
	public Statement statement = null;
	protected JComboBox database;
	protected JPanel header;
	private JPanel layout;
	private Tables tables;
	private JTable table;	
	private JScrollPane pane;
	private JButton drop;
	private JMenuBar menu;
	private JMenu server, help;
	private JMenuItem newDatabase, diconnect, contents, about;
	private static String title = "MySQL Explorer";
	private String manualUrl = "http://dev.mysql.com/doc/#manual";

	public MakeUI(Statement stmt) {
		super(title);
		statement = stmt;
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		menuBar();
		build();
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
			public void actionPerformed(ActionEvent event) {
				new NewDatabase(MakeUI.this);
			}
		});
		server.add(newDatabase);
		
		server.addSeparator();
		
		diconnect = new JMenuItem("Diconnect");
		server.add(diconnect);

		diconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				dispose();
			}
		});

		help = new JMenu("Help");
		menu.add(help);
		help.addSeparator();
		contents = new JMenuItem("Help Contents");
		help.add(contents);

		contents.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					Desktop.getDesktop().browse(new URL(manualUrl).toURI());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		about = new JMenuItem("About");
		help.add(about);

		about.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				JOptionPane.showMessageDialog(MakeUI.this,
						"MySQL Explorer v1.0", "About",
						JOptionPane.PLAIN_MESSAGE);
			}
		});

		setJMenuBar(menu);
	}

	private void header() {
		header = new JPanel(new FlowLayout(FlowLayout.LEFT));
		header.add(new JLabel("Database"));
		
		database = new JComboBox(new DefaultComboBoxModel(new Query(this)
				.listDatabases().toArray()));

		setTitle(title + " - " + (String) database.getSelectedItem());

		header.add(database);
		database.setPreferredSize(new Dimension(300, 25));

		drop = new JButton("Drop");
		header.add(drop);

		drop.addActionListener(new DropDatabase(this));

		add(header, BorderLayout.NORTH);
		header.setBackground(Color.lightGray);
	}

	private void build() {
		header();
		layout = new JPanel();
		layout.setLayout(new BorderLayout());

		tables = new Tables();
		table = tables.render(new Query(this).listTables(database
				.getSelectedItem().toString()));
		layout.add(table.getTableHeader(), BorderLayout.NORTH);
		layout.add(table, BorderLayout.CENTER);
		pane = new JScrollPane(table);
		add(pane, BorderLayout.CENTER);
		database.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		getContentPane().remove(pane);
		if (database.getSelectedIndex() >= 0) {
			String name = database.getSelectedItem().toString();
			setTitle(title + " - " + name);
			table.setModel(new DefaultTableModel());
			table = tables.render(new Query(this).listTables(name));
			table.setFillsViewportHeight(true);
			layout.add(table.getTableHeader(), BorderLayout.NORTH);
			layout.add(table, BorderLayout.CENTER);
			pane = new JScrollPane(table);
			add(pane, BorderLayout.CENTER);
		} else {
			setTitle(title + " - Undefined");
			drop.setEnabled(false);
			database.setEnabled(false);
		}
		pane.updateUI();
		validate();
		repaint();
	}
}
