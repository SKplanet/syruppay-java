package com.skplanet.jose.jwa.crypto;

public enum Algorithm {
	AES("AES"), HS256("HmacSHA256"), RSA("RSA"), RS256("SHA256WithRSA"), ES256("SHA256withECDSA");

	private String value;

	private Algorithm(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
