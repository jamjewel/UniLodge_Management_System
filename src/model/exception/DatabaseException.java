package model.exception;

public class DatabaseException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1502469550397662586L;

	public DatabaseException(String msg) {
		super("DB Error: " + msg);
	}

}
