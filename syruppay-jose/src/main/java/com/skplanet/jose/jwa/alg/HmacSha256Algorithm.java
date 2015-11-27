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

import com.skplanet.jose.exception.IllegalSignatureToken;
import com.skplanet.jose.jwa.JwsAlgorithm;
import com.skplanet.jose.jwa.crypto.Algorithm;
import com.skplanet.jose.jwa.crypto.CryptoUtils;
import com.skplanet.jose.jwa.crypto.Transformation;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Created by 박병찬 on 2015-07-14.
 */
public class HmacSha256Algorithm implements JwsAlgorithm {
	@Override
	public boolean verify(byte[] key, byte[] actual, byte[] expected) {
		Transformation transformation = new Transformation(Algorithm.HS256);
		byte[] signedBytes = new byte[0];
		try {
			signedBytes = CryptoUtils.hmac(transformation, actual, key);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalSignatureToken(transformation.getAlgorithm()+"AlgorithmException", e);
		} catch (InvalidKeyException e) {
			throw new IllegalSignatureToken("InvalidVerifykeyException", e);
		}
		return Arrays.equals(signedBytes, expected);
	}

	@Override
	public byte[] sign(byte[] key, byte[] bytes) {
		Transformation transformation = new Transformation(Algorithm.HS256);
		try {
			return CryptoUtils.hmac(transformation, bytes, key);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalSignatureToken(transformation.getAlgorithm()+"AlgorithmException", e);
		} catch (InvalidKeyException e) {
			throw new IllegalSignatureToken("InvalidSignkeyException", e);
		}
	}
}
