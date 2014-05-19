package tool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import SQLExplorer.db.Tool;
import SQLExplorer.ui.Confirm;
import SQLExplorer.ui.UI;

public class Restore extends Tool implements ActionListener {

	private UI ui;
	private String name;

	public Restore(UI ui, String name) {
		super(ui);
		this.ui = ui;
		this.name = name;
	}

	@Override
	public void actionPerformed(ActionEvent event) {

	}
}
