package SQLExplorer.db;

import java.sql.SQLException;

public class UISQLException extends SQLException {

	private static final long serialVersionUID = 1L;
	
	private String message;
	
	public UISQLException(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}

}
