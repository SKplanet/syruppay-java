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

package com.skplanet.jose;

import com.skplanet.jose.support.JoseSupport;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.spec.PKCS8EncodedKeySpec;

public class TestJweLibrary {
	@Test
	public void testAes128() throws Exception {
		String data = "apple";
		String symmetricKey = "1234567890123456";
		
		System.out.println("SRC DATA: " + data);
		String encData = JoseSupport.build_a128kw("test", symmetricKey.getBytes(), data);
		System.out.println("ENC DATA: " + encData);
		String decData = JoseSupport.verify_a128kw(symmetricKey.getBytes(), encData);
		System.out.println("DEC DATA: " + decData);
		
		/**
		 * 결과
		 * SRC DATA: apple
		 * ENC DATA: eyJhbGciOiJBMTI4S1ciLCJlbmMiOiJBMTI4Q0JDLUhTMjU2In0.vvIRHwiN2i0TOHOevL4dc8eStQJ607ugkZz1HZrqV99-Froki_POIQ.q-qLAjHHzJXLJhcnDCvS0A.4ihs9xMj3zt6sXrSszU3WA.sdVuNn6AZL77y2rZS_cyyw
		 * DEC DATA: apple
		 */
	}

	@Ignore
	@Test
	public void testJweRsa() throws Exception{
		String userHome = System.getProperty("user.home");
		String keystoreFilename = userHome + File.separator + ".keystore";

		char[] password = "dja0221".toCharArray();
		String kid      = "jwe";

        /* 암호화 */
		FileInputStream fIn = new FileInputStream(keystoreFilename);
		KeyStore keystore = KeyStore.getInstance("pkcs12");
		keystore.load(fIn, password);
		Certificate cert = keystore.getCertificate(kid);
		byte[] pubKey = cert.getPublicKey().getEncoded();

		String src = "apple"; /* 함호화 할 데이터 */
		String enc = JoseSupport.build_rsa15(kid, pubKey, src);

		/* 복호화 */
		kid = JoseSupport.verify_ras15Header(enc);
		Key key = keystore.getKey(kid, password);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key.getEncoded());
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		byte[] priKey = privateKey.getEncoded();

		String target = JoseSupport.verify_rsa15(priKey, enc);

		System.out.println("enc=" + src + ", dec=" + target);
	}

	@Ignore
	@Test
	public void testJwsRsa() throws Exception {
		String userHome = System.getProperty("user.home");
		String keystoreFilename = userHome + File.separator + ".keystore";

		char[] password = "dja0221".toCharArray();
		String kid      = "jwe";

		          /* 암호화 */
		FileInputStream fIn = new FileInputStream(keystoreFilename);
		KeyStore keystore = KeyStore.getInstance("pkcs12");
		keystore.load(fIn, password);
		Certificate cert = keystore.getCertificate(kid);
		PublicKey publicKey = cert.getPublicKey();
		
		Key key = keystore.getKey(kid, password);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key.getEncoded());
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

		String src = "apple"; /* 함호화 할 데이터 */
		String enc = JoseSupport.Jws_RsaSign(src, privateKey);
		String target = JoseSupport.Jws_RsaVerify(enc, publicKey);

		System.out.println("enc=" + src + ", dec=" + target);
	}

	@Test
	public void testJwsHmac() throws Exception {
		String symmetricKey = "1234567890123456";

		String src = "apple"; /* 함호화 할 데이터 */

		String enc = JoseSupport.Jws_HmacSign(src, symmetricKey);
		String target = JoseSupport.Jws_HmacVerify(enc, symmetricKey);
		System.out.println("enc=" + enc + ", dec=" + target);

		Assert.assertThat(target, CoreMatchers.is(src));

	}
	
}
