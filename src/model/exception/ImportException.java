package model.exception;

public class ImportException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4294288925579477137L;

	public ImportException(String msg) {
		super("Import Error: " + msg);
	}
}
