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
import com.skplanet.jose.jwa.crypto.*;
import com.skplanet.jose.jwa.enc.ContentEncryptKeyGenerator;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * Created by byeongchan.park@sk.com(1000808) on 2015-11-26.
 */
public class RSAEncryptionAlgorithm {
	public static JweAlgResult encryption(Transformation transformation, byte[] key, ContentEncryptKeyGenerator cekGenerator) {
		byte[] cek = cekGenerator.generateRandomKey();
		RSAPublicKey publicKey = null;
		try {
			publicKey = (RSAPublicKey) CryptoUtils.generatePublicKey(KeyAlgorithm.RSA, key);
		} catch (Exception e) {
			throw new KeyGenerateException("RSAPublicKeyGenerateException", e);
		}
		try {
			return new JweAlgResult(cek, CryptoUtils.rsaEncrypt(transformation, cek, publicKey));
		} catch (Exception e) {
			throw new KeyGenerateException(transformation.getValue()+"EncryptionException", e);
		}
	}

	public static byte[] decryption(Transformation transformation, byte[] key, byte[] cek) {
		RSAPrivateKey privateKey = null;
		try {
			privateKey = (RSAPrivateKey) CryptoUtils.generatePrivateKey(KeyAlgorithm.RSA, key);
		} catch (Exception e) {
			throw new KeyGenerateException("RSAPrivateKeyGenerateException", e);
		}
		try {
			return CryptoUtils.rsaDecrypt(transformation, cek, privateKey);
		} catch (Exception e) {
			throw new KeyGenerateException(transformation.getValue()+"DecryptionException", e);
		}
	}
}
