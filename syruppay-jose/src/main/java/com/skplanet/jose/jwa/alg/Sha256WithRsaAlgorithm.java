package com.skplanet.jose.jwa.alg;

import com.skplanet.jose.jwa.JwsAlgorithm;
import com.skplanet.jose.jwa.crypto.Algorithm;
import com.skplanet.jose.jwa.crypto.CryptoUtils;
import com.skplanet.jose.jwa.crypto.KeyAlgorithm;
import com.skplanet.jose.jwa.crypto.Transformation;

/**
 * Created by 박병찬 on 2015-07-14.
 */
public class Sha256WithRsaAlgorithm implements JwsAlgorithm {
	public boolean verify(byte[] key, byte[] actual, byte[] expected) {
		Transformation transformation = new Transformation(Algorithm.RS256, null, null);
		return CryptoUtils.asymmetricSignatureVerify(
				transformation,
				CryptoUtils.generatePublicKey(KeyAlgorithm.RSA, key),
				actual,
				expected);
	}

	public byte[] sign(byte[] key, byte[] bytes) {
		Transformation transformation = new Transformation(Algorithm.RS256, null, null);
		return CryptoUtils.asymmetricSignature(
				transformation,
				CryptoUtils.generatePrivateKey(KeyAlgorithm.RSA, key),
				bytes);
	}
}
