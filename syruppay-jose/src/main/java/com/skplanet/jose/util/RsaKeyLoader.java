package com.skplanet.jose.util;

import java.io.FileInputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * java keystore의 key를 로딩하는 클래스
 * 
 * @author 1000808 (byeongchan.park@sk.com)
 *
 */
public class RsaKeyLoader {
	/**
	 * key store type : pkcs12
	 */
	public final static String STORE_TYPE_PKCS12 = "pkcs12";
	
	/**
	 * key store type : jks
	 */
	public final static String STORE_TYPE_JKS = "jks";
	
	private KeyStore keystore = null;
	private char[] storepass = null;
	
	/**
	 * keystore를 로딩하는 생성자
	 * 
	 * @param keystoreFileName : keystore의 파일 경로
	 * @param storeType	: STORE_TYPE_PKCS12 또는 STORE_TYPE_JKS
	 * @param storepass : keystore의 패스워드
	 * @throws IllegalAccessException 
	 */
	public RsaKeyLoader(String keystoreFileName, String storeType, char[] storepass) throws IllegalAccessException {
		if (keystore != null)
			return;
		
		FileInputStream fis = null;
		
		try {
			fis = new FileInputStream(keystoreFileName);
			keystore = KeyStore.getInstance(storeType);
			keystore.load(fis, storepass);
			
			this.storepass = storepass;
		} catch (Exception e) {
			throw new IllegalAccessException("keystore access exception.");
		} finally {
			if (fis != null)
				try { fis.close(); } catch (Exception e) { }
		}	
	}
	
	/**
	 * privatekey 반환하는 메소드.
	 * 
	 * @param alias : keystore에 등록되어 있는 alias
	 * @return
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyStoreException 
	 * @throws UnrecoverableKeyException 
	 * @throws InvalidKeySpecException 
	 */
	public byte[] getPrivateKey(String alias) throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, InvalidKeySpecException {
		Key key = keystore.getKey(alias, storepass);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key.getEncoded());
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

		return privateKey.getEncoded();
	}
	
	/**
	 * publickey 반환하는 메소드
	 * 
	 * @param alias : keystore에 등록되어 있는 alias
	 * @return
	 * @throws KeyStoreException 
	 */
	public byte[] getPublicKey(String alias) throws KeyStoreException {
		Certificate cert = keystore.getCertificate(alias);

		return cert.getPublicKey().getEncoded();
	}
}
