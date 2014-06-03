package SQLExplorer.ui.components;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import SQLExplorer.ui.UI;
import SQLExplorer.ui.tool.NewDatabase;
import SQLExplorer.ui.tool.Prefs;

public class FrameMenu extends JMenuBar {

	private static final long serialVersionUID = 1L;
	private UI ui;

	public FrameMenu(final UI ui) {
		this.ui = ui;
	}

	public void render() {
		final JMenu server = new JMenu("Server");
		add(server);
		server.add(addItem("New Database", "database_add.png"))
				.addActionListener(createDb);
		server.add(addItem("Database Server", "database.png"));
		server.addSeparator();
		server.add(addItem("Disconnect", "disconnect.png")).addActionListener(
				disconnect);

		final JMenu window = new JMenu("Window");
		add(window);
		window.add(addItem("Preferences", "database_gear.png"))
				.addActionListener(new Prefs(ui));

		final JMenu help = new JMenu("Help");
		add(help);
		help.add(addItem("Help Contents", "")).addActionListener(redirect);
		help.addSeparator();
		help.add(addItem("About", "")).addActionListener(about);
		ui.setJMenuBar(this);
	}

	private JMenuItem addItem(final String title, final String icon) {
		final JMenuItem item = new JMenuItem(title);
		if (!item.equals("")) {
			item.setIcon(new ImageIcon(getClass().getResource(
					"/resources/icons/" + icon)));
		}
		return item;
	}

	private Action disconnect = new AbstractAction() {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			ui.dispose();
		}
	};

	private Action createDb = new AbstractAction() {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			new NewDatabase(ui);
		}
	};

	private Action redirect = new AbstractAction() {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				Desktop.getDesktop().browse(
						new URL("http://dev.mysql.com/doc/#manual").toURI());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	};

	private Action about = new AbstractAction() {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(ui, "MySQL Explorer v1.0", "About",
					JOptionPane.PLAIN_MESSAGE);
		}
	};
}
