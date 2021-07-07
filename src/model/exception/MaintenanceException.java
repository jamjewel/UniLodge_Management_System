package model.exception;

public class MaintenanceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5299498312913522472L;

	public MaintenanceException(String msg) {
		super("Maintenance Error: " + msg);
	}

}
