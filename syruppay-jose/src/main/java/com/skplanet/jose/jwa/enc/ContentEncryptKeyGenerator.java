package com.skplanet.jose.jwa.enc;

import com.skplanet.jose.jwa.crypto.Algorithm;
import com.skplanet.jose.jwa.crypto.CryptoUtils;
import com.skplanet.jose.jwa.crypto.Transformation;

/**
 * Created by 박병찬 on 2015-08-17.
 */
public class ContentEncryptKeyGenerator {
	private int keyLength = 0;
	private byte[] cek = null;

	public ContentEncryptKeyGenerator(int keyLength) {
		this.keyLength = keyLength;
	}

	public void setUserEncryptionKey(byte[] cek) {
		this.cek = cek;
	}

	private int getKeyBitLength() {
		return keyLength * 8;
	}

	public byte[] generateRandomKey() {
		if (cek == null ) {
			Transformation transformation = new Transformation(Algorithm.AES);
			cek = CryptoUtils.generatorKey(transformation, getKeyBitLength());
		}

		return cek;
	}
}
