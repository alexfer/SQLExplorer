package SQLExplorer.ui.tool;

import java.io.File;

import javax.swing.JFileChooser;

public final class FileChooser {
	private static FileChooser instance = new FileChooser();
	
	private FileChooser() {
	}

	public static synchronized FileChooser getInstance() {
		return instance;
	}

	public static JFileChooser dialog(boolean mode) {
		final JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		chooser.setDialogTitle(mode ? "Select Destination Folder" : "Select Destination File");
		chooser.setFileSelectionMode(mode ? JFileChooser.DIRECTORIES_ONLY : JFileChooser.FILES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		return chooser;
	}
}
