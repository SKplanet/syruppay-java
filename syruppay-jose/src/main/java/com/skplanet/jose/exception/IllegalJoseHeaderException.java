package com.skplanet.jose.exception;

@SuppressWarnings("serial")
public class IllegalJoseHeaderException extends RuntimeException {
	public IllegalJoseHeaderException() {
		super();
	}

	public IllegalJoseHeaderException(String message) {
		super(message);
	}
}
