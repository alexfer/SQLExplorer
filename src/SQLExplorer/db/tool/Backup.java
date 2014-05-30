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

public class Backup {

	private Preferences prefs;
	private Export exp = null;
	private int proc = 0;
	private long start;

	public Backup(Export exp, long start) {
		prefs = Preferences.userNodeForPackage(UI.class);
		this.exp = exp;
		this.start = start;
	}

	public long[] start() throws ToolException {
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
			args = Helper.removeArgument(args, "--quick");
		}

		if (!exp.force.isSelected()) {
			args = Helper.removeArgument(args, "--force");
		}

		if (!exp.dropDb.isSelected()) {
			args = Helper.removeArgument(args, "--add-drop-database");
			args = Helper.removeArgument(args, "--databases");
		}

		if (exp.tblSelected.size() > 0) {
			for (String table : exp.tblSelected) {
				args = Helper.appendArgument(args, table);
			}
			args = Helper.removeArgument(args, "--add-drop-database");
			args = Helper.removeArgument(args, "--databases");
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
				Helper.copy(in, new File(file));

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
					throw new ToolException(exp.ui.join(errors, "\n"));
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
}
