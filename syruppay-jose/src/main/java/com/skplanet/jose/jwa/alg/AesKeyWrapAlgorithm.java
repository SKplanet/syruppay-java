package com.skplanet.jose.jwa.alg;

import com.skplanet.jose.exception.IllegalEncryptionKey;
import com.skplanet.jose.jwa.JweAlgorithm;
import com.skplanet.jose.jwa.crypto.Algorithm;
import com.skplanet.jose.jwa.crypto.CryptoUtils;
import com.skplanet.jose.jwa.crypto.Transformation;
import com.skplanet.jose.jwa.enc.ContentEncryptKeyGenerator;

/**
 * Created by 박병찬 on 2015-07-29.
 */
public class AesKeyWrapAlgorithm implements JweAlgorithm {
	private int fixedkeyLength;

	//0.3.2 이전 버전에서는 key length를 체크하지 않았음.
	public AesKeyWrapAlgorithm() {
		this.fixedkeyLength = 0;
	}

	public AesKeyWrapAlgorithm(int keyLength) {
		this.fixedkeyLength = keyLength;
	}

	private void isValidKey(byte[] key) {
		if (fixedkeyLength != 0 && key.length != fixedkeyLength) {
			throw new IllegalEncryptionKey("JWE key must be "+fixedkeyLength+" bytes. yours key "+key.length+" bytes.");
		}
	}

	public JweAlgResult encryption(byte[] key, ContentEncryptKeyGenerator cekGenerator) {
		isValidKey(key);

		byte[] cek = cekGenerator.generateRandomKey();
		return new JweAlgResult(cek, CryptoUtils.KeyWrap(new Transformation(Algorithm.AES), key, cek));
	}

	public byte[] decryption(byte[] key, byte[] cek) {
		isValidKey(key);

		Transformation transformation = new Transformation(Algorithm.AES);
		return CryptoUtils.keyUnwrap(transformation, key, cek);
	}
}
