package com.kendy.excel.excel4j.exceptions;

/**
 * author : Crab2Died
 * date : 2017/5/24  14:29
 */
public class Excel4jReadException extends RuntimeException {

	private static final long serialVersionUID = 8735084330744657672L;

	public Excel4jReadException() {
	    super();
    }

    public Excel4jReadException(String message) {
        super(message);
    }

    public Excel4jReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
