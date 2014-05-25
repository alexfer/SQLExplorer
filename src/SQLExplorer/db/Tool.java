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

import SQLExplorer.ui.UI;
import SQLExplorer.ui.tool.Export;
import SQLExplorer.ui.tool.Import;

public class Tool {
	private UI ui;

	/**
	 * 
	 * @param ui
	 */
	public Tool(UI ui) {
		this.ui = ui;
	}
	
	/**
	 * 
	 * @param input List arguments
	 * @param index Element that needs to removing
	 * @return Returns new list arguments
	 */
	public static String[] removeArgument(String[] array, String value) {
		List<String> args = new ArrayList<String>(Arrays.asList(array));
		args.remove(value);
		return args.toArray(new String[0]);
	}

	/**
	 * 
	 * @param im
	 * @return
	 * @throws UISQLException
	 */
	public int backup(Import im) throws UISQLException {
		String name = ui.database.getSelectedItem().toString(), file = im.file
				.getText();

		if (file.equals("")) {
			file = String.format("%s.sql", name);
		}

		String[] args = { "mysqldump", "--force", "--quick",
				String.format("--user=%s", ui.user),
				String.format("--password=%s", ui.password), "--databases",
				name, "--add-drop-database" };

		if (!im.quick.isSelected()) {			
			args = removeArgument(args, "--quick");
		}

		if (!im.force.isSelected()) {			
			args = removeArgument(args, "--force");
		}
				
		if (!im.dropDb.isSelected()) {			
			args = removeArgument(args, "--add-drop-database");
			args = removeArgument(args, "--databases");
		}				

		return execute(args, String.format("%s/%s", im.path.getText(), file));
	}
	
	/**
	 * 
	 * @param export
	 * @return
	 * @throws UISQLException
	 */
	public int restore(Export export) throws UISQLException {
		String name = ui.database.getSelectedItem().toString(), path = export.path
				.getText();

		return execute(
				new String[] { "mysql", "-f", name,
						String.format("--user=%s", ui.user),
						String.format("--password=%s", ui.password), "-e",
						String.format(" source %s", path) }, null);
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
	private int execute(String[] cmd, String file) throws UISQLException {
		int proc = 0;
		try {
			Process exec = Runtime.getRuntime().exec(cmd);
			try {
				if (!cmd[0].equals("mysql")) {
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
