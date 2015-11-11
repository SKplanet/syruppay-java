package com.skplanet.jose.jwa.enc;

import com.skplanet.jose.exception.IllegalAuthenticationTag;
import com.skplanet.jose.jwa.crypto.*;
import com.skplanet.jose.util.ByteUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class AesEncryptionWithHmacSha extends ContentEncryption {
	public AesEncryptionWithHmacSha(int keyLength, int ivLength) {
		super(keyLength, ivLength);
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
		Transformation transformation = new Transformation(Algorithm.AES, Mode.CBC, Padding.PKCS5Padding);
		return CryptoUtils.aesEncrypt(transformation, src, key, iv);
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
			throw new IllegalAuthenticationTag();
		}
	}

	private byte[] decryption(byte[] key, byte[] iv, byte[] cipherText) {
		return CryptoUtils.aesDecrypt(new Transformation(Algorithm.AES, Mode.CBC, Padding.PKCS5Padding), cipherText,
				key, iv);
	}

	private byte[] sign(byte[] key, byte[] iv, byte[] cipherText, byte[] aad) {
		byte[] signPart = getSignPart(iv, cipherText, aad);
		byte[] digest = CryptoUtils.hmac(new Transformation(Algorithm.HS256), signPart, key);

		byte[] at = new byte[16];
		System.arraycopy(digest, 0, at, 0, 16);

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
}
