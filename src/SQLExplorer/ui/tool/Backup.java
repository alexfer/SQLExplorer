package SQLExplorer.ui.tool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import SQLExplorer.db.Tool;
import SQLExplorer.db.UISQLException;
import SQLExplorer.ui.Confirm;
import SQLExplorer.ui.UI;

public class Backup extends Tool implements ActionListener, Runnable {

	private UI ui;
	private JProgressBar progress = null;
	private Integer value = null;

	public Backup(UI ui, JProgressBar progress) {
		super(ui);		
		this.ui = ui;
		this.progress = progress;
		new Thread(this).start();
	}

	public void setValue(java.lang.Integer value) {
		this.value = value;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		JFileChooser chooser = new JFileChooser();
		String destantion = "/";

		chooser.setCurrentDirectory(new File("."));
		chooser.setDialogTitle("Select Destination Folder");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		Confirm c = new Confirm();
		boolean confirm = c.dialog(ui,
				"Do you want to create backup database '"
						+ ui.database.getSelectedItem().toString() + "'?");

		if (!confirm) {
			return;
		}

		int jfch = chooser.showOpenDialog(ui);
		if (jfch == JFileChooser.APPROVE_OPTION) {
			// chooser.getCurrentDirectory()
			destantion = chooser.getSelectedFile().toString();
			try {
				int completed = super.backup(destantion);

				if (completed == 0) {
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
			chooser.setVisible(false);
		}

	}

	@Override
	public void run() {
		do {
			if (value != null) {
				progress.setValue((int) Math.round(Math.floor(value.intValue() * 100 / 100)));
			}
			try {
				Thread.sleep(100L);
			} catch (java.lang.InterruptedException ex) {
				ex.printStackTrace();
			}
		} while (value == null || value.intValue() < progress.getMaximum());
	}
}
