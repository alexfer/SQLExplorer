package SQLExplorer.db.tool;

public class ToolException extends Exception {
private static final long serialVersionUID = 1L;
	
	private String message;
	
	public ToolException(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}
}
