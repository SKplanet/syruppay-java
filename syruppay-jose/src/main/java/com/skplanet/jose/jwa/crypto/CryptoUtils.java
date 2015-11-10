package com.skplanet.jose.jwa.crypto;

import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.*;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.AESWrapEngine;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class CryptoUtils {
	public static byte[] aesEncrypt(Transformation transformation, byte[] raw, byte[] secret, byte[] iv) {
		Cipher cipher = null;
		byte[] encryptedData = null;

		try {
			cipher = Cipher.getInstance(transformation.getValue());
			cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(secret, transformation.getAlgorithm()),
					new IvParameterSpec(iv));
			encryptedData = cipher.doFinal(raw);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encryptedData;
	}

	public static byte[] aesDecrypt(Transformation transformation, byte[] encryptedData, byte[] secret, byte[] iv) {
		Cipher cipher = null;
		byte[] decryptedData = null;

		try {
			cipher = Cipher.getInstance(transformation.getValue());
			cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(secret, transformation.getAlgorithm()),
					new IvParameterSpec(iv));
			decryptedData = cipher.doFinal(encryptedData);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return decryptedData;
	}

	public static byte[] rsaEncrypt(Transformation transformation, byte[] raw, PublicKey key) {
		byte[] encryptedData = null;

		try {
			Cipher cipher = Cipher.getInstance(transformation.getValue());
			cipher.init(Cipher.ENCRYPT_MODE, key);
			encryptedData = cipher.doFinal(raw);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return encryptedData;
	}

	public static byte[] rsaDecrypt(Transformation transformation, byte[] encrypted, PrivateKey key) {
		byte[] decryptedData = null;

		try {
			Cipher cipher = Cipher.getInstance(transformation.getValue());
			cipher.init(Cipher.DECRYPT_MODE, key);
			decryptedData = cipher.doFinal(encrypted);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return decryptedData;
	}

	public static RSAPublicKey generateRsaPublicKey(BigInteger modulus, BigInteger publicExponent) {
		RSAPublicKey publicKey = null;
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			publicKey = (RSAPublicKey) keyFactory.generatePublic(new RSAPublicKeySpec(modulus, publicExponent));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return publicKey;
	}

	public static PublicKey generatePublicKey(KeyAlgorithm algorithms, byte[] pKey) {
		PublicKey publicKey = null;

		try {
			X509EncodedKeySpec x509Spec = new X509EncodedKeySpec(pKey);
			KeyFactory keyFactory = KeyFactory.getInstance(algorithms.getValue());
			publicKey = keyFactory.generatePublic(x509Spec);
		} catch (NoSuchAlgorithmException e) {
			if (algorithms.equals(KeyAlgorithm.EC)) {

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return publicKey;
	}

	public static RSAPrivateKey generateRsaPrivateKey(BigInteger modulus, BigInteger privateExponent) {
		RSAPrivateKey privateKey = null;

		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			privateKey = (RSAPrivateKey) keyFactory.generatePrivate(new RSAPrivateKeySpec(modulus, privateExponent));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return privateKey;
	}

	public static PrivateKey generatePrivateKey(KeyAlgorithm algorithms, byte[] pKey) {
		PrivateKey privateKey = null;

		try {
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pKey);
			KeyFactory keyFactory = KeyFactory.getInstance(algorithms.getValue());
			privateKey = keyFactory.generatePrivate(keySpec);
		} catch (NoSuchAlgorithmException e) {
			if (algorithms.equals(KeyAlgorithm.EC)) {

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return privateKey;
	}

	public static SecretKeySpec generateSymmetricKey(byte[] key, Algorithm algorithm) {
		return new SecretKeySpec(key, algorithm.getValue());
	}

	public static KeyPair generateRsaKeyPair(int keySize, BigInteger publicExponent) {
		KeyPair keys = null;
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(keySize, publicExponent);
			keyGen.initialize(spec);
			keys = keyGen.generateKeyPair();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return keys;
	}

	public static byte[] hmac(Transformation transformation, byte[] raw, byte[] macKey) {
		Mac hmac = null;
		byte[] signature = null;

		try {
			hmac = Mac.getInstance(transformation.getValue());
			hmac.init(new SecretKeySpec(macKey, transformation.getAlgorithm()));
			signature = hmac.doFinal(raw);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return signature;
	}

	public static byte[] asymmetricSignature(Transformation transformation, Key key, byte[] bytes) {
		Signature signature = null;
		byte[] signedBytes = null;

		try {
			signature = Signature.getInstance(transformation.getValue());
			signature.initSign((PrivateKey) key);
			signature.update(bytes);

			signedBytes = signature.sign();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return signedBytes;
	}

	public static boolean asymmetricSignatureVerify(Transformation transformation, Key key, byte[] signingBytes, byte[] expectedBytes) {
		Signature verifier = null;
		try {
			verifier = Signature.getInstance(transformation.getValue());
			verifier.initVerify((PublicKey) key);
			verifier.update(signingBytes);
			return verifier.verify(expectedBytes);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public static byte[] KeyWrap(Transformation transformation, byte[] symmetricKey, byte[] cek) {
		AESWrapEngine engine = new AESWrapEngine();
		CipherParameters param = new KeyParameter(
				new SecretKeySpec(symmetricKey, transformation.getAlgorithm()).getEncoded());
		engine.init(true, param);
		return engine.wrap(cek, 0, cek.length);
	}

	public static byte[] keyUnwrap(Transformation transformation, byte[] symmetricKey, byte[] cek) {
		AESWrapEngine engine = new AESWrapEngine();
		CipherParameters param = new KeyParameter(
				new SecretKeySpec(symmetricKey, transformation.getAlgorithm()).getEncoded());
		engine.init(false, param);
		byte[] keyBytes = null;
		;
		try {
			keyBytes = engine.unwrap(cek, 0, cek.length);
		} catch (InvalidCipherTextException e) {
			e.printStackTrace();
		}
		return keyBytes;
	}

	public static byte[] generatorKey(Transformation transformation, int size) {
		SecureRandom secureRandom = null;
		KeyGenerator keyGenerator = null;
		SecretKey key = null;
		byte[] rawKey = null;

		try {
			secureRandom = SecureRandom.getInstance("SHA1PRNG");
			keyGenerator = KeyGenerator.getInstance(transformation.getAlgorithm());
			keyGenerator.init(size, secureRandom);

			key = keyGenerator.generateKey();
			rawKey = key.getEncoded();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rawKey;
	}

	private static ECParameterSpec P256 = new ECParameterSpec(new EllipticCurve(
			new ECFieldFp(new BigInteger("FFFFFFFF00000001000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFF", 16)),
			new BigInteger("FFFFFFFF00000001000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFC", 16),
			new BigInteger("5AC635D8AA3A93E7B3EBBD55769886BC651D06B0CC53B0F63BCE3C3E27D2604B", 16)),
			new ECPoint(new BigInteger("6B17D1F2E12C4247F8BCE6E563A440F277037D812DEB33A0F4A13945D898C296", 16),
					new BigInteger("4FE342E2FE1A7F9B8EE7EB4A7C0F9E162BCE33576B315ECECBB6406837BF51F5", 16)),
			new BigInteger("FFFFFFFF00000000FFFFFFFFFFFFFFFFBCE6FAADA7179E84F3B9CAC2FC632551", 16), 1);

	public static ECPrivateKey generateEcPrivateKey(byte[] d) {
		ECPrivateKey privateKey = null;

		try {
			KeyFactory keyFactory = KeyFactory.getInstance("EC");
			privateKey = (ECPrivateKey) keyFactory.generatePrivate(new ECPrivateKeySpec(new BigInteger(d), P256));
		} catch (NoSuchAlgorithmException e) {
			if (Security.getProvider("BC") == null) {
				Security.addProvider(new BouncyCastleProvider());
			}

			KeyFactory keyFactory = null;
			try {
				keyFactory = KeyFactory.getInstance("EC", "BC");
				privateKey = (ECPrivateKey) keyFactory.generatePrivate(new ECPrivateKeySpec(new BigInteger(d), P256));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return privateKey;
	}

	public static ECPublicKey generateEcPublicKey(byte[] x, byte[] y) {
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

			KeyFactory keyFactory = null;
			try {
				keyFactory = KeyFactory.getInstance("EC", "BC");
				publicKey = (ECPublicKey) keyFactory.generatePublic(ecPublicKeySpec);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return publicKey;
	}
}
