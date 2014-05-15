package SQLExplorer.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import SQLExplorer.db.Query;

class DropDatabase extends Confirm implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1891497792293858626L;
	private MakeUI ui;

	DropDatabase(MakeUI ui) {
		this.ui = ui;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		int index = ui.database.getSelectedIndex();
		boolean confirm = super.dialog(ui, "You are about to DESTROY a complete database!");

		if (index >= 0 && confirm) {
			new Query(ui).dropDatabase(ui.database.getSelectedItem().toString());
			ui.database.removeItemAt(index);
			ui.database.updateUI();
		}
	}

}
