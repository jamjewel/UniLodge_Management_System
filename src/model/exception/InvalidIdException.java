package model.exception;

public class InvalidIdException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5299498312913522472L;

	public InvalidIdException(String msg) {
		super("Invalid ID: " + msg);
	}

}
