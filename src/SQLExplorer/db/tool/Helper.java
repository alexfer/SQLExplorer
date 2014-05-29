package SQLExplorer.db.tool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Helper {
	private static Helper instance = new Helper();

	private Helper() {
	}

	public static synchronized Helper getInstance() {
		return instance;
	}

	public static String[] removeArgument(String[] array, String value) {
		List<String> args = new ArrayList<String>(Arrays.asList(array));
		args.remove(value);
		return args.toArray(new String[0]);
	}
	
	public static String[] appendArgument(String[] array, String value) {
		List<String> args = new ArrayList<String>(Arrays.asList(array));
		args.add(value);
		return args.toArray(new String[0]);
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
