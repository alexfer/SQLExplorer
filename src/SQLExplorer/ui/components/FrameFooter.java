package SQLExplorer.ui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import SQLExplorer.db.Handler;
import SQLExplorer.ui.DatabaseTables;
import SQLExplorer.ui.ErrorException;
import SQLExplorer.ui.UI;
import SQLExplorer.ui.tool.TUitl;

public class FrameFooter extends JPanel {

	private static final long serialVersionUID = 1L;
	private UI ui;
	public static JComboBox<Object> handle;	
	public static JLabel progress;

	public FrameFooter(UI ui) {
		super(new FlowLayout(FlowLayout.LEFT));
		this.ui = ui;
	}

	public void render() {
		add(new JLabel("", new ImageIcon(getClass().getResource(
				"/resources/icons/database_go.png")), SwingConstants.LEFT));
		handle = new JComboBox<Object>(new String[] { "--------", "Check",
				"Optimize", "Repair", "Empty", "Drop" });
		add(handle);

		progress = new JLabel("In Progerss");
		add(progress);
		progress.setVisible(false);

		if (TUitl.inArray(UI.excludeDbs, ui.database.getSelectedItem().toString())) {
			FrameHeader.drop.setEnabled(false);
			handle.setEnabled(false);
		}
		handle.addActionListener(handler);
		ui.add(this, BorderLayout.SOUTH);
		setBackground(Color.lightGray);
	}

	private Action handler = new AbstractAction() {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {

			ArrayList<String> selected = new ArrayList<String>();

			for (int i = 0; i < ui.table.getModel().getRowCount(); i++) {
				if ((Boolean) ui.table.getValueAt(i, 6)) {
					selected.add(ui.table.getValueAt(i, 0).toString());
				}
			}

			if (selected.size() > 0 && handle.getSelectedIndex() > 0) {
				Handler handler = new Handler(ui);
				try {
					handler.action(selected, handle.getSelectedItem()
							.toString().toLowerCase());
				} catch (ErrorException ex) {
					JOptionPane.showMessageDialog(ui, ex.getMessage()
							.toString(), "Error", JOptionPane.ERROR_MESSAGE);
				}
				handle.setSelectedIndex(0);
				ui.getContentPane().remove(ui.pane);
				ui.renderTableList(new DatabaseTables(), ui.database
						.getSelectedItem().toString());
				ui.pane.updateUI();
				ui.validate();
				ui.repaint();
			}
		}
	};
}
