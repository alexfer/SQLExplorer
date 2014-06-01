package SQLExplorer.db.tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.prefs.Preferences;

import SQLExplorer.ui.UI;
import SQLExplorer.ui.tool.Import;

public class Restore implements Runnable {
	private Preferences prefs;
	private Import imp = null;
	private int proc = 0;
	private long start;

	public Restore(Import imp, long start) {
		prefs = Preferences.userNodeForPackage(UI.class);
		this.imp = imp;
		this.start = start;
	}

	private long[] handle() throws ToolException {
		String name = imp.ui.database.getSelectedItem().toString(), path = imp.path
				.getText();

		String[] args = { prefs.get("mysql", ""), "--force", name,
				String.format("--user=%s", imp.ui.user),
				String.format("--password=%s", imp.ui.password), "-e",
				String.format(" source %s", path) };

		if (!imp.force.isSelected()) {
			args = Helper.removeArgument(args, "--force");
		}

		try {
			return new long[] { execute(args),
					System.currentTimeMillis() - start };
		} catch (ToolException e) {
			throw new ToolException(e.getMessage());
		}
	}

	private int execute(String[] cmd) throws ToolException {

		try {
			Process exec = Runtime.getRuntime().exec(cmd);
			try {
				ArrayList<String> errors = new ArrayList<String>();
				BufferedReader buffer = new BufferedReader(
						new InputStreamReader(exec.getErrorStream()));
				try {					
					String line = null;
					while ((line = buffer.readLine()) != null) {
						errors.add(line);
					}					
				} catch (IOException ex) {
					UI.logger.info(ex.toString());
				} finally {
					buffer.close();
				}

				proc = exec.waitFor();
				if (errors.size() > 0) {
					throw new ToolException(imp.ui.join(errors, "\n"));
				}

			} catch (InterruptedException e) {
				throw new ToolException(e.getMessage());
			} finally {
				exec.destroy();
			}
		} catch (IOException e) {
			throw new ToolException(e.getMessage());
		}
		return proc;
	}

	@Override
	public void run() {
		try {
			final long[] finished = handle();
			if (finished[0] == 0) {
				imp.finished(finished[1]);
			}
		} catch (ToolException e) {
			e.printStackTrace();
		}
	}
}
