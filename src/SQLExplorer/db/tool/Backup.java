package SQLExplorer.db.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.prefs.Preferences;

import SQLExplorer.ui.UI;
import SQLExplorer.ui.tool.Export;
import SQLExplorer.ui.tool.TFile;
import SQLExplorer.ui.tool.TUitl;

public class Backup implements Runnable {
	private Preferences prefs;
	private Export exp = null;
	private int proc = 0;
	private long start;

	public Backup(Export exp, long start) {
		prefs = Preferences.userNodeForPackage(UI.class);
		this.exp = exp;
		this.start = start;
	}

	public long[] handle() throws ToolException {
		String name = exp.ui.database.getSelectedItem().toString(), file = exp.file
				.getText();

		if (file.equals("")) {
			file = String.format("%s.sql", name);
		}

		String[] args = { prefs.get("mysqldump", ""), "--force", "--quick",
				"--add-drop-database", String.format("--user=%s", exp.ui.user),
				String.format("--password=%s", exp.ui.password), "--databases",
				name };

		if (!exp.quick.isSelected()) {
			args = TUitl.removeArgument(args, "--quick");
		}

		if (!exp.force.isSelected()) {
			args = TUitl.removeArgument(args, "--force");
		}

		if (!exp.drop.isSelected()) {
			args = TUitl.removeArgument(args, "--add-drop-database");
			args = TUitl.removeArgument(args, "--databases");
		}

		if (exp.tblSelected.size() > 0) {
			for (String table : exp.tblSelected) {
				args = TUitl.appendArgument(args, table);
			}
			args = TUitl.removeArgument(args, "--add-drop-database");
			args = TUitl.removeArgument(args, "--databases");
		}

		try {
			return new long[] {
					execute(args,
							String.format("%s/%s", exp.path.getText(), file)),
					System.currentTimeMillis() - start };
		} catch (ToolException e) {
			throw new ToolException(e.getMessage());
		}
	}

	private int execute(String[] cmd, String file) throws ToolException {

		try {
			Process exec = Runtime.getRuntime().exec(cmd);
			try {
				InputStream in = exec.getInputStream();
				TFile.copy(in, new File(file));

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
				if (proc == 2) {
					return 0;
				}
				if (errors.size() > 0) {
					throw new ToolException(TUitl.join(errors, "\n"));
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
				exp.finished(finished[1]);
			}
		} catch (ToolException e) {
			e.printStackTrace();
		} finally {
			
		}
	}

}
