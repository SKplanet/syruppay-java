package com.skplanet.jose.exception;

@SuppressWarnings("serial")
public class IllegalSignatureToken extends RuntimeException {
	public IllegalSignatureToken() {
		super();
	}

	public IllegalSignatureToken(String message) {
		super(message);
	}
}
