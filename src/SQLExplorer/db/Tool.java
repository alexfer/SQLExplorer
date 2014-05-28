package SQLExplorer.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.SwingUtilities;

import SQLExplorer.ui.UI;
import SQLExplorer.ui.tool.Export;
import SQLExplorer.ui.tool.Import;

public class Tool extends Thread {
	private Preferences prefs;
	private Task task;
	private Export exp = null;
	private Import imp = null;
	private int proc = 0;

	Tool() {
		super();
	}

	/**
	 * 
	 * @param exp
	 */
	public Tool(Export exp) {
		prefs = Preferences.userNodeForPackage(UI.class);
		this.exp = exp;
	}

	/**
	 * 
	 * @param imp
	 */
	public Tool(Import imp) {
		prefs = Preferences.userNodeForPackage(UI.class);
		this.imp = imp;
	}

	/**
	 * 
	 * @param input
	 *            List arguments
	 * @param index
	 *            Element that needs to removing
	 * @return Returns new list arguments
	 */
	public static String[] removeArgument(String[] array, String value) {
		List<String> args = new ArrayList<String>(Arrays.asList(array));
		args.remove(value);
		return args.toArray(new String[0]);
	}

	/**
	 * 
	 * @throws UISQLException
	 */
	public void export() throws UISQLException {
		String name = exp.ui.database.getSelectedItem().toString(), file = exp.file
				.getText();
		task = new Task(exp.ui);
		task.start();
		if (file.equals("")) {
			file = String.format("%s.sql", name);
		}

		String[] args = { prefs.get("mysqldump", ""), "--force", "--quick",
				String.format("--user=%s", exp.ui.user),
				String.format("--password=%s", exp.ui.password), "--databases",
				name, "--add-drop-database" };

		if (!exp.quick.isSelected()) {
			args = removeArgument(args, "--quick");
		}

		if (!exp.force.isSelected()) {
			args = removeArgument(args, "--force");
		}

		if (!exp.dropDb.isSelected()) {
			args = removeArgument(args, "--add-drop-database");
			args = removeArgument(args, "--databases");
		}
		exp.dialog.dispose();
		try {
			execute(args, String.format("%s/%s", exp.path.getText(), file));
		} catch (UISQLException e) {
			throw new UISQLException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param im
	 * @return
	 * @throws UISQLException
	 */
	public void restore() throws UISQLException {
		String name = imp.ui.database.getSelectedItem().toString(), path = imp.path
				.getText();
		task = new Task(imp.ui);
		task.start();
		String[] args = { prefs.get("mysql", ""), "--force", name,
				String.format("--user=%s", imp.ui.user),
				String.format("--password=%s", imp.ui.password), "-e",
				String.format(" source %s", path) };

		if (!imp.force.isSelected()) {
			args = removeArgument(args, "--force");
		}
		imp.dialog.dispose();
		try {
			execute(args, null);
		} catch (UISQLException e) {
			throw new UISQLException(e.getMessage());
		}
	}

	public void run() {
		// TODO Change this block
		final int state = getProc();
		try {
			if (imp != null) {
				restore();
				if (state == 0 || state == 2) {
					imp.finished();
				}
			}
			if (exp != null) {
				export();
				if (state == 0 || state == 2) {
					exp.finished();
				}
			}
		} catch (UISQLException ex) {
			try {
				throw new UISQLException(ex.getMessage());
			} catch (UISQLException e) {
				e.printStackTrace();
			}
		}
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			UI.logger.info(e.getMessage());
		}
	}

	/**
	 * 
	 * @param in
	 * @param file
	 * @throws UISQLException
	 */
	private void copy(InputStream in, File file) throws UISQLException {
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
			throw new UISQLException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param cmd
	 * @param file
	 * @return
	 * @throws UISQLException
	 */

	private synchronized void execute(String[] cmd, String file)
			throws UISQLException {

		try {
			Process exec = Runtime.getRuntime().exec(cmd);
			try {
				if (!cmd[0].equals(prefs.get("mysql", ""))) {
					InputStream in = exec.getInputStream();
					copy(in, new File(file));
				}
				ArrayList<String> errors = new ArrayList<String>();
				try {
					BufferedReader buffer = new BufferedReader(
							new InputStreamReader(exec.getErrorStream()));
					String line = null;

					while ((line = buffer.readLine()) != null) {
						errors.add(line);
					}
					buffer.close();
				} catch (IOException ex) {
					UI.logger.info(ex.toString());
				}

				proc = exec.waitFor();
				if (errors.size() > 0) {
					throw new UISQLException(exp.ui.join(errors, "\n"));
				}

			} catch (InterruptedException e) {
				throw new UISQLException(e.getMessage());
			} finally {
				exec.destroy();
			}
		} catch (IOException e) {
			throw new UISQLException(e.getMessage());
		}
	}

	public int getProc() {
		return this.proc;
	}
}

class Task extends Thread {
	UI ui;

	Task(UI ui) {
		super();
		this.ui = ui;
	}

	public void run() {
		for (int i = 0; i <= 100; i += 10) {
			final int progress = i;
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					ui.progressBar.setValue(progress);
				}
			});
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				UI.logger.info(e.getMessage());
			}
		}
	}
}
