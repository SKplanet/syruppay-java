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

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import com.skplanet.jose.jwa.JweAlgorithm;
import com.skplanet.jose.jwa.crypto.*;
import com.skplanet.jose.jwa.enc.ContentEncryptKeyGenerator;

public class RSAOAEPAlgorithm implements JweAlgorithm {
	public JweAlgResult encryption(byte[] key, ContentEncryptKeyGenerator cekGenerator) {
		byte[] cek = cekGenerator.generateRandomKey();

		RSAPublicKey publicKey = (RSAPublicKey) CryptoUtils.generatePublicKey(KeyAlgorithm.RSA, key);
		return new JweAlgResult(cek, CryptoUtils.rsaEncrypt(
				new Transformation(Algorithm.RSA, Mode.ECB, Padding.OAEPPadding),
				cek,
				publicKey));
	}

	public byte[] decryption(byte[] key, byte[] cek) {
		Transformation transformation = new Transformation(Algorithm.RSA, Mode.ECB, Padding.OAEPPadding);
		RSAPrivateKey privateKey = (RSAPrivateKey) CryptoUtils.generatePrivateKey(KeyAlgorithm.RSA, key);
		return CryptoUtils.rsaDecrypt(transformation, cek, privateKey);
	}
}
