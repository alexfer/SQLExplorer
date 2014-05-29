package SQLExplorer.db.tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.prefs.Preferences;

import SQLExplorer.db.UISQLException;
import SQLExplorer.ui.UI;
import SQLExplorer.ui.tool.Import;

public class Restore extends Thread {
	private Preferences prefs;
	private Task task;
	private Import imp = null;
	private int proc = 0;

	Restore() {
		super();
	}

	public Restore(Import imp) {
		prefs = Preferences.userNodeForPackage(UI.class);
		this.imp = imp;
	}
	
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
			args = Helper.removeArgument(args, "--force");
		}
		imp.dialog.dispose();
		try {
			execute(args);
		} catch (UISQLException e) {
			throw new UISQLException(e.getMessage());
		}
	}

	public void run() {
		// TODO Change this block
		final int state = getProc();
		try {
			restore();
			if(state == 2) {
				imp.finished();
			}
			if (state == 0) {
				imp.finished();
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
	
	private synchronized void execute(String[] cmd)
			throws UISQLException {

		try {
			Process exec = Runtime.getRuntime().exec(cmd);
			try {
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
					throw new UISQLException(imp.ui.join(errors, "\n"));
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
