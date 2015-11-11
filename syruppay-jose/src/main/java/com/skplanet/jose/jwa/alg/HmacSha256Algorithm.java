package com.skplanet.jose.jwa.alg;

import com.skplanet.jose.jwa.JwsAlgorithm;
import com.skplanet.jose.jwa.crypto.Algorithm;
import com.skplanet.jose.jwa.crypto.CryptoUtils;
import com.skplanet.jose.jwa.crypto.Transformation;

import java.util.Arrays;

/**
 * Created by 박병찬 on 2015-07-14.
 */
public class HmacSha256Algorithm implements JwsAlgorithm {
	@Override
	public boolean verify(byte[] key, byte[] actual, byte[] expected) {
		Transformation transformation = new Transformation(Algorithm.HS256);
		byte[] signedBytes = CryptoUtils.hmac(transformation, actual, key);
		return Arrays.equals(signedBytes, expected);
	}

	@Override
	public byte[] sign(byte[] key, byte[] bytes) {
		Transformation transformation = new Transformation(Algorithm.HS256);
		return CryptoUtils.hmac(transformation, bytes, key);
	}
}
