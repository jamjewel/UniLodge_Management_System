package model.exception;

public class InvalidInputException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2295712877029057665L;

	public InvalidInputException(String msg) {
		super("Invalid Input: " + msg);
	}

}
