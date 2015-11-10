package com.skplanet.jose.util;

import com.skplanet.jose.support.JoseSupport;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.spec.InvalidKeySpecException;

public class RsaKeyLoaderTest {
	String keystoreFileName = "C:/Users/skplanet/.keystore";
	String storeType = RsaKeyLoader.STORE_TYPE_PKCS12;
	char[] storepass = "0123456".toCharArray();
	String alias = "sap_dev";

	@Before
	public void setUp() throws Exception {
	}

	@Ignore
	@Test
	public void test() throws IllegalAccessException, KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, InvalidKeySpecException {
		RsaKeyLoader loader = new RsaKeyLoader(keystoreFileName, storeType, storepass);
		byte[] publicKey = loader.getPublicKey(alias);
		
		String src = "deviceKey=DE2014102816201425515044607;expire=;20120913T195630+0900;";
		String enc= JoseSupport.build_rsa15(alias, publicKey, src);
		
		System.out.println(enc);
		
		String kid = JoseSupport.verify_ras15Header(enc);
		byte[] privateKey = loader.getPrivateKey(kid);
		
		String result = JoseSupport.verify_rsa15(privateKey, enc);
		
		Assert.assertEquals(src, result);
	}
}
