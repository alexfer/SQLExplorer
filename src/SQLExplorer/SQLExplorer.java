package SQLExplorer;

import javax.swing.SwingUtilities;

import SQLExplorer.ui.Login;
import SQLExplorer.ui.Theme;

public class SQLExplorer {
	public static void main(String[] args) {				
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				Theme.setup();
				Login login = new Login();
				login.setVisible(true);
			}
		});
	}	
}
