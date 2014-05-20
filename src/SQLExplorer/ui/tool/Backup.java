package SQLExplorer.ui.tool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import SQLExplorer.db.Tool;
import SQLExplorer.db.UISQLException;
import SQLExplorer.ui.Confirm;
import SQLExplorer.ui.UI;

public class Backup extends Tool implements ActionListener {

	private UI ui;

	public Backup(UI ui) {
		super(ui);
		this.ui = ui;		
	}

	@Override
	public void actionPerformed(ActionEvent event) {

		DirChooser.getInstance();
		JFileChooser dialog = DirChooser.dialog();
		String destantion = "/";

		Confirm c = new Confirm();
		boolean confirm = c.dialog(ui,
				"Do you want to create backup database '"
						+ ui.database.getSelectedItem().toString() + "'?");

		if (!confirm) {
			return;
		}
		ui.setEnabled(false);
		int jfch = dialog.showOpenDialog(ui);
		if (jfch == JFileChooser.APPROVE_OPTION) {
			destantion = dialog.getSelectedFile().toString();
			try {
				int completed = super.backup(destantion);

				if (completed == 0) {
					ui.setEnabled(true);
					JOptionPane
							.showMessageDialog(
									ui,
									"Database backup operation has been finished successfully.",
									"Backup Completed",
									JOptionPane.INFORMATION_MESSAGE);
				}
			} catch (UISQLException e) {
				JOptionPane.showMessageDialog(ui, e.getMessage().toString(),
						"Error", JOptionPane.ERROR_MESSAGE);
			}
		} else if (jfch == JFileChooser.CANCEL_OPTION) {
			dialog.setVisible(false);
		}

	}
}
