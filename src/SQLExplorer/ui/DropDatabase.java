package SQLExplorer.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import SQLExplorer.db.Query;
import SQLExplorer.db.UISQLException;

public class DropDatabase extends Confirm implements ActionListener {

	private static final long serialVersionUID = 1L;
	private UI ui;

	public DropDatabase(UI ui) {
		this.ui = ui;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		int index = ui.database.getSelectedIndex();
		String name = ui.database.getSelectedItem().toString();
		boolean confirm = super.dialog(ui, "Are you sure you want to delete database '"
				+ name + "'?");
		if (index >= 0 && confirm) {
			try {
				new Query(ui).dropDatabase(name);
			} catch (UISQLException e) {
				JOptionPane.showMessageDialog(ui, e.getMessage().toString(),
						"Error", JOptionPane.ERROR_MESSAGE);
			}
			ui.database.removeItemAt(index);
			ui.database.updateUI();
		}
	}

}
