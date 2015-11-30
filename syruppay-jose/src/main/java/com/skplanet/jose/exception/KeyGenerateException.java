package com.skplanet.jose.exception;

/**
 * Created by byeongchan.park@sk.com(1000808) on 2015-11-26.
 */
public class KeyGenerateException extends RuntimeException {
	public KeyGenerateException() {
		super();
	}

	public KeyGenerateException(String message) {
		super(message);
	}

	public KeyGenerateException(String message, Throwable cause) {
		super(message, cause);
	}
}
