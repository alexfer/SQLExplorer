package SQLExplorer.ui;

import javax.swing.JOptionPane;

public class Confirm extends JOptionPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7612011113250947897L;

	final boolean dialog(final UI ui, final String message) {
		Object[] options = { "OK", "Cancel" };

		int confirm = super.showOptionDialog(ui, message, "Confirmation",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				options, options[0]);
		if(confirm == 1) {
			return false;
		}
		return true;
	}
}
