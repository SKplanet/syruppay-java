package com.skplanet.jose.jwa.enc;

import com.skplanet.jose.jwa.JweEncryption;
import com.skplanet.jose.jwa.crypto.Algorithm;
import com.skplanet.jose.jwa.crypto.CryptoUtils;
import com.skplanet.jose.jwa.crypto.Transformation;

/**
 * Created by 박병찬 on 2015-08-13.
 */
public abstract class ContentEncryption implements JweEncryption {
	protected int keyLength = 0;
	protected int ivLength = 0;

	public ContentEncryption(int keyLength, int ivLength) {
		this.keyLength = keyLength;
		this.ivLength = ivLength;
	}

	protected int getIvBitLength() {
		return ivLength * 8;
	}

	protected byte[] generateRandomIv() {
		Transformation transformation = new Transformation(Algorithm.AES);
		return CryptoUtils.generatorKey(transformation, getIvBitLength());
	}
}
