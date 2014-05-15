package SQLExplorer;

import javax.swing.SwingUtilities;
import SQLExplorer.ui.Login;

public class SQLExplorer {	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Login();				
			}
		});
	}
}
