package model.exception;

public class RentException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5299498312913522472L;

	public RentException(String msg) {
		super("Rent Error: " + msg);
	}

}
