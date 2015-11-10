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
