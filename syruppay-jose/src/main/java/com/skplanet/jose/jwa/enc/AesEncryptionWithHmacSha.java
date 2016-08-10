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

package com.skplanet.jose.jwa.enc;

import com.skplanet.jose.commons.codec.binary.Base64;
import com.skplanet.jose.exception.EncryptionException;
import com.skplanet.jose.exception.IllegalAuthenticationTag;
import com.skplanet.jose.exception.IllegalSignatureToken;
import com.skplanet.jose.jwa.crypto.*;
import com.skplanet.jose.util.ByteUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class AesEncryptionWithHmacSha extends ContentEncryption {
	private Transformation transformation;

	public AesEncryptionWithHmacSha(int keyLength, int ivLength, Transformation transformation, Algorithm hmacAlgorithm) {
		super(keyLength, ivLength, hmacAlgorithm);
		this.transformation = transformation;
	}

	public JweEncResult encryptAndSign(byte[] cek, byte[] iv, byte[] src, byte[] aad) {
		iv = iv != null ? iv : generateRandomIv();

		byte[] dKey = ByteUtils.firstHalf(cek);
		byte[] eKey = ByteUtils.restHalf(cek);

		byte[] cipherText = encryption(eKey, iv, src);
		byte[] at = sign(dKey, iv, cipherText, aad);

		return new JweEncResult(cipherText, at, iv);
	}

	private byte[] encryption(byte[] key, byte[] iv, byte[] src) {
		try {
			return CryptoUtils.aesEncrypt(transformation, src, key, iv);
		} catch (Exception e) {
			throw new EncryptionException(transformation.getValue()+"EncryptionException", e);
		}
	}

	public byte[] verifyAndDecrypt(byte[] cek, byte[] iv, byte[] cipherText, byte[] aad, byte[] expected) {
		byte[] dKey = ByteUtils.firstHalf(cek);
		byte[] eKey = ByteUtils.restHalf(cek);

		verifyAuthenticationTag(dKey, iv, cipherText, aad, expected);

		return decryption(eKey, iv, cipherText);
	}

	private void verifyAuthenticationTag(byte[] key, byte[] iv, byte[] cipherText, byte[] aad, byte[] expected) {
		byte[] actual = sign(key, iv, cipherText, aad);

		if (!Arrays.equals(actual, expected)) {
			throw new IllegalAuthenticationTag("actual: " + Base64.encodeBase64URLSafeString(actual) +
				"expected: "+Base64.encodeBase64URLSafeString(expected) +
				"is not matched");
		}
	}

	private byte[] decryption(byte[] key, byte[] iv, byte[] cipherText) {
		try {
			return CryptoUtils.aesDecrypt(transformation, cipherText, key, iv);
		} catch (Exception e) {
			throw new EncryptionException(transformation.getValue()+"DecryptionException", e);
		}
	}

	private byte[] sign(byte[] key, byte[] iv, byte[] cipherText, byte[] aad) {
		byte[] signPart = getSignPart(iv, cipherText, aad);
		byte[] digest = new byte[0];
		try {
			digest = CryptoUtils.hmac(new Transformation(hmacAlgorithm), signPart, key);
		} catch (Exception e) {
			throw new IllegalSignatureToken("invalid algorithm/key", e);
		}

		byte[] at = new byte[keyLength/2];
		System.arraycopy(digest, 0, at, 0, keyLength/2);

		return at;
	}

	private byte[] getSignPart(byte[] iv, final byte[] cipherText, byte[] aad) {
		byte[] al = getAl(aad);

		int size = aad.length + iv.length + cipherText.length + al.length;
		ByteBuffer buffer = ByteBuffer.allocate(size);
		buffer.put(aad, 0, aad.length);
		buffer.put(iv, 0, iv.length);
		buffer.put(cipherText, 0, cipherText.length);
		buffer.put(al, 0, al.length);

		return buffer.array();
	}

	private byte[] getAl(byte[] aad) {
		int len = ByteUtils.getBitLength(aad);

		return ByteBuffer.allocate(8)
				.order(ByteOrder.BIG_ENDIAN)
				.putLong(len)
				.array();
	}

	public ContentEncryptKeyGenerator getContentEncryptionKeyGenerator() {
		return new ContentEncryptKeyGenerator(keyLength);
	}

	public static class AesWithPaddingAndHmac extends AesEncryptionWithHmacSha {
		public AesWithPaddingAndHmac(int keyLength, int ivLength, Algorithm hmacAlgorithm) {
			super(keyLength, ivLength, new Transformation(Algorithm.AES, Mode.CBC, Padding.PKCS5Padding), hmacAlgorithm);
		}
	}

	public static class AesNoPaddingAndHmac extends AesEncryptionWithHmacSha {
		public AesNoPaddingAndHmac(int keyLength, int ivLength, Algorithm hmacAlgorithm) {
			super(keyLength, ivLength, new Transformation(Algorithm.AES, Mode.CBC, Padding.NoPadding), hmacAlgorithm);
		}
	}
}
