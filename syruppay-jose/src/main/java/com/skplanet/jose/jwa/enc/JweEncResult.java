package com.skplanet.jose.jwa.enc;

/**
 * Created by 박병찬 on 2015-08-12.
 */
public class JweEncResult {
	private byte[] cipherText = null;
	private byte[] at = null;
	private byte[] iv = null;

	public JweEncResult(byte[] cipherText, byte[] at, byte[] iv) {
		this.cipherText = cipherText;
		this.at = at;
		this.iv = iv;
	}

	public byte[] getCipherText() {
		return cipherText;
	}

	public byte[] getAt() {
		return at;
	}

	public byte[] getIv() {
		return iv;
	}
}
