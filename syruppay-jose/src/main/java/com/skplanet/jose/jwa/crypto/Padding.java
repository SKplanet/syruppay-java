package com.skplanet.jose.jwa.crypto;

public enum Padding {
	PKCS1Padding ("PKCS1Padding"), PKCS5Padding("PKCS5Padding"), NoPadding("NoPadding"), OAEPPadding("OAEPWithSHA-1AndMGF1Padding");

	private String value;

	private Padding(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
