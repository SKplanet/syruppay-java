package com.skplanet.jose.exception;

@SuppressWarnings("serial")
public class IllegalEncryptionKey extends RuntimeException {
	public IllegalEncryptionKey() {
		super();
	}

	public IllegalEncryptionKey(String message) {
		super(message);
	}
}
