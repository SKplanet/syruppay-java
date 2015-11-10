package com.skplanet.jose.exception;

public class UnsupportedJOSEAlgorithm extends RuntimeException {
	public UnsupportedJOSEAlgorithm() {
		super();
	}
	public UnsupportedJOSEAlgorithm(String message) {
		super(message);
	}
}
