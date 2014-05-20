package SQLExplorer.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

import SQLExplorer.ui.UI;

public class Tool {
	private UI ui;

	public Tool(UI ui) {
		this.ui = ui;
	}

	public int backup(String path) throws UISQLException {
		String name = ui.database.getSelectedItem().toString();
		return execute(
				new String[] { "mysqldump", "--force", "--quick",
						String.format("--user=%s", ui.user),
						String.format("--password=%s", ui.password), name },
				String.format("%s/%s.sql", path, name));
	}

	public int restore(String path) throws UISQLException {
		String name = ui.database.getSelectedItem().toString();
		return execute(
				new String[] { "mysql", String.format("--user=%s", ui.user),
						String.format("--password=%s", ui.password), name },
				String.format("%s/%s.sql", path, name));
	}

	private void copy(InputStream in, File file) {
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
			e.printStackTrace();
		}
	}

	private int execute(String[] cmd, String file) throws UISQLException {
		int proc = 0;
		try {
			Process exec = Runtime.getRuntime().exec(cmd);
			try {
				InputStream in = exec.getInputStream();
				copy(in, new File(file));

				BufferedReader buffer = new BufferedReader(
						new InputStreamReader(exec.getErrorStream()));
				String line = null;
				ArrayList<String> errors = new ArrayList<String>();

				while ((line = buffer.readLine()) != null) {
					errors.add(line);
				}

				proc = exec.waitFor();

				if (proc == 2) {
					return 0;
				}
				if (errors.size() > 0) {
					throw new UISQLException(ui.join(errors, "\n"));
				}

			} catch (InterruptedException e) {
				throw new UISQLException(e.getMessage());
			} finally {
				exec.destroy();				
			}
		} catch (IOException e) {
			throw new UISQLException(e.getMessage());
		}
		return proc;
	}
}
