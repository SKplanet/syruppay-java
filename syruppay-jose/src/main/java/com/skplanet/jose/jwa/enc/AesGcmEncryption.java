package com.skplanet.jose.jwa.enc;

import com.skplanet.jose.commons.codec.binary.Base64;
import com.skplanet.jose.exception.EncryptionException;
import com.skplanet.jose.jwa.crypto.*;
import com.skplanet.jose.util.ByteUtils;

/**
 * Created by byeongchan.park@sk.com(1000808) on 2015-12-18.
 */
public class AesGcmEncryption extends ContentEncryption {
	private final static int AT_BYTES_LENGTH = 16;

	public AesGcmEncryption(int keyLength, int ivLength, Algorithm hmacAlgorithm) {
		super(keyLength, ivLength, hmacAlgorithm);
	}

	@Override public JweEncResult encryptAndSign(byte[] cek, byte[] iv, byte[] src, byte[] aad) {
		iv = iv != null ? iv : generateRandomIv();

		byte[] e = encryption(cek, iv, src, aad);
		int idx = e.length - AT_BYTES_LENGTH;
		byte[] cipherText = ByteUtils.subBytes(e, 0, idx);
		byte[] at = ByteUtils.subBytes(e, idx, AT_BYTES_LENGTH);

		return new JweEncResult(cipherText, at, iv);
	}

	private byte[] encryption(byte[] key, byte[] iv, byte[] src, byte[] aad) {
		Transformation transformation = new Transformation(Algorithm.AES, Mode.GCM, Padding.NoPadding);
		try {
			return CryptoUtils.aesGcmEncrypt(transformation, src, key, AT_BYTES_LENGTH * 8, iv, aad);
		} catch (Exception e) {
			throw new EncryptionException(transformation.getValue()+"EncryptionException", e);
		}
	}

	@Override public byte[] verifyAndDecrypt(byte[] cek, byte[] iv, byte[] cipherText, byte[] aad, byte[] expected) {
		byte[] b = ByteUtils.concat(cipherText, expected);
		return decryption(cek, iv, b, aad);
	}

	private byte[] decryption(byte[] key, byte[] iv, byte[] cipherText, byte[] aad) {
		Transformation transformation = new Transformation(Algorithm.AES, Mode.GCM, Padding.NoPadding);
		try {
			return CryptoUtils.aesGcmDecrypt(transformation, cipherText, key, AT_BYTES_LENGTH * 8, iv, aad);
		} catch (Exception e) {
			throw new EncryptionException(transformation.getValue()+"DecryptionException", e);
		}
	}

	@Override public ContentEncryptKeyGenerator getContentEncryptionKeyGenerator() {
		return new ContentEncryptKeyGenerator(keyLength);
	}
}
