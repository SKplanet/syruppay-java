/*
 * Copyright (c) 2015 SK PLANET. ALL Rights Reserved.
 *
 * Syrup Pay Jose Library
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.skplanet.jose.jwa.alg;

import com.skplanet.jose.exception.KeyGenerateException;
import com.skplanet.jose.jwa.JwsAlgorithm;
import com.skplanet.jose.jwa.crypto.Algorithm;
import com.skplanet.jose.jwa.crypto.CryptoUtils;
import com.skplanet.jose.jwa.crypto.KeyAlgorithm;
import com.skplanet.jose.jwa.crypto.Transformation;

import java.security.Key;

/**
 * Created by 박병찬 on 2015-07-14.
 */
public class Sha256WithRsaAlgorithm implements JwsAlgorithm {
	public boolean verify(byte[] key, byte[] actual, byte[] expected) {
		Transformation transformation = new Transformation(Algorithm.RS256, null, null);
		Key publicKey = null;
		try {
			publicKey = CryptoUtils.generatePublicKey(KeyAlgorithm.RSA, key);
		} catch (Exception e) {
			throw new KeyGenerateException("RSAPublicKeyGenerateException", e);
		}
		return AsymmetricShaAlgorithm.verify(transformation, publicKey, actual, expected);
	}

	public byte[] sign(byte[] key, byte[] bytes) {
		Transformation transformation = new Transformation(Algorithm.RS256, null, null);
		Key privateKey = null;
		try {
			privateKey = CryptoUtils.generatePrivateKey(KeyAlgorithm.RSA, key);
		} catch (Exception e) {
			throw new KeyGenerateException("RSAPrivateKeyGenerateException", e);
		}
		return AsymmetricShaAlgorithm.sign(transformation, privateKey, bytes);
	}
}
