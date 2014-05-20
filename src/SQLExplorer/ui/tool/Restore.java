package SQLExplorer.ui.tool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import SQLExplorer.db.Tool;
import SQLExplorer.ui.Confirm;
import SQLExplorer.ui.UI;

public class Restore extends Tool implements ActionListener {

	private UI ui;	

	public Restore(UI ui) {
		super(ui);
		this.ui = ui;		
	}

	@Override
	public void actionPerformed(ActionEvent event) {

	}
}
