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
import com.skplanet.jose.exception.KeyGenerateException;
import com.skplanet.jose.jwa.JwsAlgorithm;
import com.skplanet.jose.jwa.crypto.Algorithm;
import com.skplanet.jose.jwa.crypto.CryptoUtils;
import com.skplanet.jose.jwa.crypto.KeyAlgorithm;
import com.skplanet.jose.jwa.crypto.Transformation;

import java.security.Key;

/**
 * Created by 박병찬 on 2015-07-22.
 */
public class Sha256WithECDSAUsingP256Algorithm implements JwsAlgorithm {
	public boolean verify(byte[] key, byte[] actual, byte[] expected) {
		Transformation transformation = new Transformation(Algorithm.ES256, null, null);
		Key publicKey = null;
		try {
			publicKey = CryptoUtils.generatePublicKey(KeyAlgorithm.EC, key);
		} catch (Exception e) {
			throw new KeyGenerateException("ECPublicKeyGenerateException", e);
		}
		return AsymmetricShaAlgoritm.verify(transformation, publicKey, actual, convertRandStoDer(expected));
	}

	public byte[] sign(byte[] key, byte[] bytes) {
		Transformation transformation = new Transformation(Algorithm.ES256, null, null);
		Key privateKey = null;
		try {
			privateKey = CryptoUtils.generatePrivateKey(KeyAlgorithm.EC, key);
		} catch (Exception e) {
			throw new KeyGenerateException("ECPrivateKeyGenerateException", e);
		}
		return convertDertoRandS(AsymmetricShaAlgoritm.sign(transformation, privateKey, bytes), 32);
	}

	public byte[] convertDertoRandS(byte derBytes[], int outputLength) {
		if (derBytes.length < 8 || derBytes[0] != 48) {
			throw new IllegalSignatureToken("Invalid format der encoding");
		}

		int offset;
		if (derBytes[1] > 0) {
			offset = 2;
		} else if (derBytes[1] == (byte) 0x81) {
			offset = 3;
		} else {
			throw new IllegalSignatureToken("Invalid format der encoding");
		}

		byte rLength = derBytes[offset + 1];

		int i;
		for (i = rLength; (i > 0) && (derBytes[(offset + 2 + rLength) - i] == 0); i--);

		byte sLength = derBytes[offset + 2 + rLength + 1];

		int j;
		for (j = sLength; (j > 0) && (derBytes[(offset + 2 + rLength + 2 + sLength) - j] == 0); j--);

		int rawLen = Math.max(i, j);
		rawLen = Math.max(rawLen, outputLength / 2);

		if ((derBytes[offset - 1] & 0xff) != derBytes.length - offset
				|| (derBytes[offset - 1] & 0xff) != 2 + rLength + 2 + sLength || derBytes[offset] != 2
				|| derBytes[offset + 2 + rLength] != 2) {
			throw new IllegalSignatureToken("Invalid format der encoding");
		}

		byte rands [] = new byte[2 * rawLen];

		System.arraycopy(derBytes, (offset + 2 + rLength) - i, rands, rawLen - i, i);
		System.arraycopy(derBytes, (offset + 2 + rLength + 2 + sLength) - j, rands, 2 * rawLen - j, j);

		return rands;
	}

	public byte[] convertRandStoDer(byte[] rands) {
		int rawLen = rands.length / 2;

		int i;
		for (i = rawLen; (i > 0) && (rands[rawLen - i] == 0); i--);

		int j = i;
		if (rands[rawLen - i] < 0) {
			j += 1;
		}

		int k;
		for (k = rawLen; (k > 0) && (rands[2 * rawLen - k] == 0); k--);

		int l = k;
		if (rands[2 * rawLen - k] < 0) {
			l += 1;
		}

		int len = 2 + j + 2 + l;
		if (len > 255) {
			throw new IllegalSignatureToken("Invalid format der encoding");
		}

		int offset;
		byte derBytes[];
		if (len < 128) {
			derBytes = new byte[2 + 2 + j + 2 + l];
			offset = 1;
		} else {
			derBytes = new byte[3 + 2 + j + 2 + l];
			derBytes[1] = (byte) 0x81;
			offset = 2;
		}

		derBytes[0] = 48;
		derBytes[offset++] = (byte) len;
		derBytes[offset++] = 2;
		derBytes[offset++] = (byte) j;

		System.arraycopy(rands, rawLen - i, derBytes, (offset + j) - i, i);

		offset += j;

		derBytes[offset++] = 2;
		derBytes[offset++] = (byte) l;

		System.arraycopy(rands, 2 * rawLen - k, derBytes, (offset + l) - k, k);

		return derBytes;
	}
}
