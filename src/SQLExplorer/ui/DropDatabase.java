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
		String name = ui.database.getSelectedItem().toString();
		boolean confirm = super.dialog(ui, "Do you have destroy database '"
				+ name + "'?");
		if (index >= 0 && confirm) {
			new Query(ui).dropDatabase(name);
			ui.database.removeItemAt(index);
			ui.database.updateUI();
		}
	}

}
