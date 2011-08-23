package edu.yale.its.cas.test;

public class CasFailException extends Exception {

	/**
     * 
     */
    private static final long serialVersionUID = -204679685940252966L;

    public CasFailException(String message) {
		super(message);
	}

	public CasFailException(String message, Throwable cause) {
		super(message, cause);
	}

}
