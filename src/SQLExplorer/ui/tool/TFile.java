package SQLExplorer.ui.tool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JFileChooser;

import SQLExplorer.db.tool.ToolException;

public class TFile {
	private static TFile instance = new TFile();

	private TFile() {
	}

	public static synchronized TFile getInstance() {
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
	
	public static void copy(InputStream in, File file) throws ToolException {
		try {
			OutputStream out = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			out.close();
			in.close();
		} catch (Exception e) {
			throw new ToolException(e.getMessage());
		}
	}
}
