package SQLExplorer.ui.tool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TUitl {
	private static TUitl instance = new TUitl();

	private TUitl() {
	}

	public static synchronized TUitl getInstance() {
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
	
	public static boolean inArray(String[] haystack, String needle) {
		for (int i = 0; i < haystack.length; i++) {
			if (haystack[i].equals(needle)) {
				return true;
			}
		}
		return false;
	}

	public static String join(ArrayList<?> parts, String separator) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < parts.size(); i++) {
			builder.append(parts.get(i));
			builder.append(separator.equals("") ? " " : separator);
		}
		builder.delete(builder.length() - 1, builder.length());
		return builder.toString();
	}
	
	public static String convert(int bytes, boolean si) {
		int unit = si ? 1000 : 1024;
		if (bytes < unit)
			return bytes + " B";
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = (si ? "kMGTPEZY" : "KMGTPEZY").charAt(exp - 1)
				+ (si ? "" : "i");
		return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}
}
