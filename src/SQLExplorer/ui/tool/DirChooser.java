package SQLExplorer.ui.tool;

import java.io.File;

import javax.swing.JFileChooser;

public final class DirChooser {
	private static DirChooser instance = new DirChooser();

	private DirChooser() {
	}

	public static synchronized DirChooser getInstance() {
		return instance;
	}

	public static JFileChooser dialog(boolean mode) {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		chooser.setDialogTitle("Select Destination Folder");
		chooser.setFileSelectionMode(mode ? JFileChooser.DIRECTORIES_ONLY : JFileChooser.FILES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		return chooser;
	}
}
