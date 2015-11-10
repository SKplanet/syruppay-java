package com.skplanet.jose.jwa.alg;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import com.skplanet.jose.jwa.crypto.*;

import com.skplanet.jose.jwa.JweAlgorithm;
import com.skplanet.jose.jwa.enc.ContentEncryptKeyGenerator;

public class RSA15Algorithm implements JweAlgorithm {
	public JweAlgResult encryption(byte[] key, ContentEncryptKeyGenerator cekGenerator) {
		byte[] cek = cekGenerator.generateRandomKey();

		RSAPublicKey publicKey = (RSAPublicKey) CryptoUtils.generatePublicKey(KeyAlgorithm.RSA, key);

		return new JweAlgResult(cek, CryptoUtils.rsaEncrypt(
				new Transformation(Algorithm.RSA, Mode.ECB, Padding.PKCS1Padding),
				cek,
				publicKey));
	}

	public byte[] decryption(byte[] key, byte[] cek) {
		Transformation transformation = new Transformation(Algorithm.RSA, Mode.ECB, Padding.PKCS1Padding);
		RSAPrivateKey privateKey = (RSAPrivateKey) CryptoUtils.generatePrivateKey(KeyAlgorithm.RSA, key);
		return CryptoUtils.rsaDecrypt(transformation, cek, privateKey);
	}
}
