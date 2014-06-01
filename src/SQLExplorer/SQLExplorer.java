package SQLExplorer;

import javax.swing.SwingUtilities;

import SQLExplorer.ui.Init;
import SQLExplorer.ui.Theme;

public class SQLExplorer {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				Theme.setup();
				Init init = new Init();
				init.setVisible(true);
			}
		});
	}
}
