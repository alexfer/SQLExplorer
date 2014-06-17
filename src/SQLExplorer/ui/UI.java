package SQLExplorer.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.sql.Statement;
import java.util.logging.*;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import SQLExplorer.db.Query;
import SQLExplorer.ui.components.FrameFooter;
import SQLExplorer.ui.components.FrameHeader;
import SQLExplorer.ui.components.FrameMenu;
import SQLExplorer.ui.tool.TUitl;

public class UI extends JFrame {

	private static final long serialVersionUID = 1L;
	public static Logger logger = Logger.getLogger(UI.class.getName());
	public Statement statement = null;
	public JComboBox<Object> database;	
	public JPanel layout;
	public JTable table;
	public JScrollPane pane;	
	public static String title = "SQL Explorer";
	public String user, password;
	public final static String[] excludeDbs = { "mysql", "information_schema",
			"performance_schema" };

	public UI(Statement stmt, String user, String password) {
		super(title);
		this.user = user;
		this.password = password;
		statement = stmt;
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		new FrameMenu(this).render();
		content();
		setSize(800, 600);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public FrameFooter footer() {
		return new FrameFooter(this);
	}

	private FrameHeader header() {
		return new FrameHeader(this);
	}

	private void content() {
		// Append header to panel
		header().render();

		// Create panel
		layout = new JPanel();
		layout.setLayout(new BorderLayout());
		renderTableList(new DatabaseTables(), database.getSelectedItem()
				.toString());
		database.addActionListener(changeDb);

		// Append footer to panel
		footer().render();
	}

	public void renderTableList(DatabaseTables tables, String db) {
		
		try {
			table = tables.render(new Query(this).listTables(db));
		} catch (ErrorException e) {
			JOptionPane.showMessageDialog(this, e.getMessage().toString(),
					"Error", JOptionPane.ERROR_MESSAGE);
		} finally {
			tables.removeAll();
		}
		layout.add(table.getTableHeader(), BorderLayout.NORTH);
		layout.add(table, BorderLayout.CENTER);
		table.setFillsViewportHeight(true);
		table.updateUI();
		pane = new JScrollPane(table);
		add(pane, BorderLayout.CENTER);
	}

	private Action changeDb = new AbstractAction() {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			getContentPane().remove(pane);
			if (database.getSelectedIndex() >= 0) {
				FrameHeader.drop.setEnabled(true);
				FrameFooter.handle.setEnabled(true);
				String name = database.getSelectedItem().toString();
				setTitle(title + " - " + name);
				renderTableList(new DatabaseTables(), name);
				FrameFooter.handle.setSelectedIndex(0);
				if (TUitl.inArray(excludeDbs, name)) {
					FrameHeader.drop.setEnabled(false);
					FrameFooter.handle.setEnabled(false);
				}
			} else {
				setTitle(title + " - Undefined");
				FrameHeader.drop.setEnabled(false);
				database.setEnabled(false);
				footer().setEnabled(false);
			}
			pane.updateUI();
			validate();
			repaint();
		}
	};
}