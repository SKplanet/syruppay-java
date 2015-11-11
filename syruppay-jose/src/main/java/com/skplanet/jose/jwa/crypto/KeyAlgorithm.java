package com.skplanet.jose.jwa.crypto;

/**
 * Created by 박병찬 on 2015-07-22.
 */
public enum KeyAlgorithm {
	RSA("RSA"), EC("EC");

	private String value;

	private KeyAlgorithm(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
