package com.skplanet.jose.jwa.crypto;

public enum Mode {
	ECB("ECB"), CBC("CBC"), CFB("CFB"), OFB("OFB");

	private String value;

	private Mode(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
