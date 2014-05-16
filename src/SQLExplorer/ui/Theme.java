package SQLExplorer.ui;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class Theme {
	public static void setup() {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
