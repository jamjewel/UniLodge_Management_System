package model.exception;

public class ReturnException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5299498312913522472L;

	public ReturnException(String msg) {
		super("Return Error: " + msg);
	}

}
