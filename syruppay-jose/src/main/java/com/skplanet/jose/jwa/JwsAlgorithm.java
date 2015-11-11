package com.skplanet.jose.jwa;

/**
 * Created by 박병찬 on 2015-07-14.
 */
public interface JwsAlgorithm {
	boolean verify(byte[] key, byte[] actual, byte[] expected);
	byte[] sign(byte[] key, byte[] bytes);
}
