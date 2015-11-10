package com.skplanet.jose.jwa.alg;

/**
 * Created by 박병찬 on 2015-08-17.
 */
public class JweAlgResult {
	private byte[] cek;
	private byte[] encryptedCek;

	public JweAlgResult(byte[] cek, byte[] encryptedCek) {
		this.cek = cek;
		this.encryptedCek = encryptedCek;
	}

	public byte[] getEncryptedCek() {
		return encryptedCek;
	}

	public byte[] getCek() {
		return cek;
	}
}
