package SQLExplorer.ui;

import java.sql.SQLException;

public class ErrorException extends SQLException {

	private static final long serialVersionUID = 1L;
	
	private String message;
	
	public ErrorException(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}

}
