package com.skplanet.jose.exception;

/**
 * Created by byeongchan.park@sk.com(1000808) on 2015-11-26.
 */
public class EncryptionException extends RuntimeException {
	public EncryptionException() {}
	public EncryptionException(String message) {
		super(message);
	}
	public EncryptionException(String message, Throwable cause) {
		super(message, cause);
	}

}
