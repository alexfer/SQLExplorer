package SQLExplorer.ui.tool;

import javax.swing.JDialog;

public class CloseDialog {
	protected static void close(JDialog d) {
		d.getContentPane().removeAll();
		d.dispose();
		d = null;
	}
}
