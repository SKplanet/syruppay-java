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

package com.skplanet.jose.jwa.crypto;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.engines.AESWrapEngine;
import org.bouncycastle.crypto.modes.GCMBlockCipher;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.*;

public class CryptoUtils {
	public static byte[] aesEncrypt(Transformation transformation, byte[] raw, byte[] secret, byte[] iv) throws Exception {
		Cipher cipher = Cipher.getInstance(transformation.getValue());
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(secret, transformation.getAlgorithm()),
				new IvParameterSpec(iv));
		return cipher.doFinal(raw);
	}

	public static byte[] aesDecrypt(Transformation transformation, byte[] encryptedData, byte[] secret, byte[] iv) throws Exception {
		Cipher cipher = Cipher.getInstance(transformation.getValue());
		cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(secret, transformation.getAlgorithm()),
				new IvParameterSpec(iv));
		return cipher.doFinal(encryptedData);
	}

	public static byte[] aesGcmEncrypt(Transformation transformation, byte[] raw, byte[] secret, int atLength, byte[] iv, byte[] aad) throws Exception {
		BlockCipher blockCipher = new AESEngine();
		blockCipher.init(true, new KeyParameter(new SecretKeySpec(secret, "AES").getEncoded()));

		GCMBlockCipher aGCMBlockCipher = new GCMBlockCipher(blockCipher);
		aGCMBlockCipher.init(true, new AEADParameters(new KeyParameter(secret), atLength, iv, aad));

		int len = aGCMBlockCipher.getOutputSize(raw.length);
		byte[] out = new byte[len];
		int outOff = aGCMBlockCipher.processBytes(raw, 0, raw.length, out, 0);
		aGCMBlockCipher.doFinal(out, outOff);

		return out;
	}

//	public static byte[] aesGcmEncrypt(Transformation transformation, byte[] raw, byte[] secret, int atLength, byte[] iv, byte[] aad) throws Exception {
//		Cipher cipher = Cipher.getInstance(transformation.getValue());
//		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(secret, transformation.getAlgorithm()),
//				new GCMParameterSpec(atLength, iv));
//		cipher.updateAAD(aad);
//		return cipher.doFinal(raw);
//	}

	public static byte[] aesGcmDecrypt(Transformation transformation, byte[] encryptedData, byte[] secret, int atLength, byte[] iv, byte[] aad) throws Exception {
		BlockCipher blockCipher = new AESEngine();
		blockCipher.init(false, new KeyParameter(new SecretKeySpec(secret, "AES").getEncoded()));

		GCMBlockCipher aGCMBlockCipher = new GCMBlockCipher(blockCipher);
		aGCMBlockCipher.init(false, new AEADParameters(new KeyParameter(secret), atLength, iv, aad));

		int len = aGCMBlockCipher.getOutputSize(encryptedData.length);
		byte[] out = new byte[len];
		int outOff = aGCMBlockCipher.processBytes(encryptedData, 0, encryptedData.length, out, 0);
		aGCMBlockCipher.doFinal(out, outOff);

		return out;
	}

//	public static byte[] aesGcmDecrypt(Transformation transformation, byte[] encryptedData, byte[] secret, int atLength, byte[] iv, byte[] aad) throws Exception {
//		Cipher cipher = Cipher.getInstance(transformation.getValue());
//		cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(secret, transformation.getAlgorithm()),
//				new GCMParameterSpec(atLength, iv));
//		cipher.updateAAD(aad);
//		return cipher.doFinal(encryptedData);
//	}

	public static byte[] rsaEncrypt(Transformation transformation, byte[] raw, PublicKey key) throws Exception {
		Cipher cipher = Cipher.getInstance(transformation.getValue());
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return cipher.doFinal(raw);
	}

	public static byte[] rsaDecrypt(Transformation transformation, byte[] encrypted, PrivateKey key) throws Exception {
		Cipher cipher = null;

		try {
			cipher = Cipher.getInstance(transformation.getValue());
		} catch (NoSuchAlgorithmException e) {
			if (Security.getProvider("BC") == null) {
				Security.addProvider(new BouncyCastleProvider());
			}

			cipher = Cipher.getInstance(transformation.getValue());
		}

		cipher.init(Cipher.DECRYPT_MODE, key);
		return cipher.doFinal(encrypted);
	}

	public static RSAPublicKey generateRsaPublicKey(BigInteger modulus, BigInteger publicExponent) throws Exception {
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return (RSAPublicKey) keyFactory.generatePublic(new RSAPublicKeySpec(modulus, publicExponent));
	}

	public static PublicKey generatePublicKey(KeyAlgorithm algorithms, byte[] pKey) throws Exception {
		X509EncodedKeySpec x509Spec = new X509EncodedKeySpec(pKey);
		KeyFactory keyFactory = KeyFactory.getInstance(algorithms.getValue());
		return keyFactory.generatePublic(x509Spec);
	}

	public static RSAPrivateKey generateRsaPrivateKey(BigInteger modulus, BigInteger privateExponent) throws Exception {
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return (RSAPrivateKey) keyFactory.generatePrivate(new RSAPrivateKeySpec(modulus, privateExponent));
	}

	public static PrivateKey generatePrivateKey(KeyAlgorithm algorithms, byte[] pKey) throws Exception {
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pKey);
		KeyFactory keyFactory = KeyFactory.getInstance(algorithms.getValue());
		return keyFactory.generatePrivate(keySpec);
	}

	public static SecretKeySpec generateSymmetricKey(byte[] key, Algorithm algorithm) {
		return new SecretKeySpec(key, algorithm.getValue());
	}

	public static KeyPair generateRsaKeyPair(int keySize, BigInteger publicExponent) throws Exception {
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(keySize, publicExponent);
		keyGen.initialize(spec);
		return keyGen.generateKeyPair();
	}

	public static byte[] hmac(Transformation transformation, byte[] raw, byte[] macKey)
			throws NoSuchAlgorithmException, InvalidKeyException {
		Mac hmac = Mac.getInstance(transformation.getValue());
		hmac.init(new SecretKeySpec(macKey, transformation.getAlgorithm()));
		return hmac.doFinal(raw);
	}

	public static byte[] asymmetricSignature(Transformation transformation, Key key, byte[] bytes)
			throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		Signature signature = Signature.getInstance(transformation.getValue());
		signature.initSign((PrivateKey) key);
		signature.update(bytes);

		return signature.sign();
	}

	public static boolean asymmetricSignatureVerify(Transformation transformation, Key key, byte[] signingBytes, byte[] expectedBytes)
			throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		Signature verifier = Signature.getInstance(transformation.getValue());
		verifier.initVerify((PublicKey) key);
		verifier.update(signingBytes);
		return verifier.verify(expectedBytes);
	}

	public static byte[] KeyWrap(Transformation transformation, byte[] symmetricKey, byte[] cek) {
		AESWrapEngine engine = new AESWrapEngine();
		CipherParameters param = new KeyParameter(
				new SecretKeySpec(symmetricKey, transformation.getAlgorithm()).getEncoded());
		engine.init(true, param);
		return engine.wrap(cek, 0, cek.length);
	}

	/*
	public static byte[] KeyWrap(Transformation transformation, byte[] symmetricKey, byte[] cek)
			throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException,
			NoSuchProviderException {
		Cipher cipher = Cipher.getInstance("AESWrap");
		cipher.init(Cipher.WRAP_MODE, new SecretKeySpec(symmetricKey, transformation.getAlgorithm()));
		return cipher.wrap(new SecretKeySpec(cek, transformation.getAlgorithm()));
	}
	*/

	public static byte[] keyUnwrap(Transformation transformation, byte[] symmetricKey, byte[] cek) throws Exception {
		AESWrapEngine engine = new AESWrapEngine();
		CipherParameters param = new KeyParameter(
				new SecretKeySpec(symmetricKey, transformation.getAlgorithm()).getEncoded());
		engine.init(false, param);
		return engine.unwrap(cek, 0, cek.length);
	}

	/*
	public static byte[] keyUnwrap(Transformation transformation, byte[] symmetricKey, byte[] cek) throws Exception {
		Cipher cipher = Cipher.getInstance("AESWrap");
		cipher.init(Cipher.UNWRAP_MODE, new SecretKeySpec(symmetricKey, transformation.getAlgorithm()));
		return cipher.unwrap(cek, transformation.getAlgorithm(), Cipher.SECRET_KEY).getEncoded();
	}
	*/

	public static byte[] generatorKey(Transformation transformation, int size) throws NoSuchAlgorithmException {
		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
		KeyGenerator keyGenerator = KeyGenerator.getInstance(transformation.getAlgorithm());
		keyGenerator.init(size, secureRandom);

		SecretKey key = keyGenerator.generateKey();
		return key.getEncoded();
	}

	private static ECParameterSpec P256 = new ECParameterSpec(new EllipticCurve(
			new ECFieldFp(new BigInteger("FFFFFFFF00000001000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFF", 16)),
			new BigInteger("FFFFFFFF00000001000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFC", 16),
			new BigInteger("5AC635D8AA3A93E7B3EBBD55769886BC651D06B0CC53B0F63BCE3C3E27D2604B", 16)),
			new ECPoint(new BigInteger("6B17D1F2E12C4247F8BCE6E563A440F277037D812DEB33A0F4A13945D898C296", 16),
					new BigInteger("4FE342E2FE1A7F9B8EE7EB4A7C0F9E162BCE33576B315ECECBB6406837BF51F5", 16)),
			new BigInteger("FFFFFFFF00000000FFFFFFFFFFFFFFFFBCE6FAADA7179E84F3B9CAC2FC632551", 16), 1);

	public static ECPrivateKey generateEcPrivateKey(byte[] d) throws Exception {
		ECPrivateKey privateKey = null;

		try {
			KeyFactory keyFactory = KeyFactory.getInstance("EC");
			privateKey = (ECPrivateKey) keyFactory.generatePrivate(new ECPrivateKeySpec(new BigInteger(d), P256));
		} catch (NoSuchAlgorithmException e) {
			if (Security.getProvider("BC") == null) {
				Security.addProvider(new BouncyCastleProvider());
			}

			KeyFactory keyFactory = KeyFactory.getInstance("EC", "BC");
			privateKey = (ECPrivateKey) keyFactory.generatePrivate(new ECPrivateKeySpec(new BigInteger(d), P256));
		}

		return privateKey;
	}

	public static ECPublicKey generateEcPublicKey(byte[] x, byte[] y) throws Exception {
		ECPublicKey publicKey = null;

		ECPublicKeySpec ecPublicKeySpec = new ECPublicKeySpec(
				new ECPoint(new BigInteger(x), new BigInteger(y)),
				P256);
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("EC");
			publicKey = (ECPublicKey) keyFactory.generatePublic(ecPublicKeySpec);
		} catch (NoSuchAlgorithmException e) {
			if (Security.getProvider("BC") == null) {
				Security.addProvider(new BouncyCastleProvider());
			}

			KeyFactory keyFactory = KeyFactory.getInstance("EC", "BC");
			publicKey = (ECPublicKey) keyFactory.generatePublic(ecPublicKeySpec);
		}

		return publicKey;
	}
}
