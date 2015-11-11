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

package com.skplanet.jose.jws;

import com.skplanet.jose.commons.codec.binary.Base64;
import com.skplanet.jose.jwa.crypto.CryptoUtils;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by 박병찬 on 2015-07-15.
 */
public class JwsVerifySha256WithECDSATest {
	/*
{"kty":"EC",
"crv":"P-256",
"x":"f83OJ3D2xF1Bg8vub9tLe1gHMzV76e8Tus9uPHvRVEU",
"y":"x_FEzRu9m36HLN_tue659LNpXW6pCyStikYjKIWI5a0",
"d":"jpsQnnGQmL-YBIffH1136cspYG6-0iY7X1fCE9-E9LI"
}
*/

	ECPublicKey publicKey;
	ECPrivateKey privateKey;

	@Before
	public void setUp() throws Exception {
		String x = "f83OJ3D2xF1Bg8vub9tLe1gHMzV76e8Tus9uPHvRVEU";
		String y = "x_FEzRu9m36HLN_tue659LNpXW6pCyStikYjKIWI5a0";
		String d = "jpsQnnGQmL-YBIffH1136cspYG6-0iY7X1fCE9-E9LI";

		publicKey = CryptoUtils.generateEcPublicKey(Base64.decodeBase64(x), Base64.decodeBase64(y));
		privateKey = CryptoUtils.generateEcPrivateKey(Base64.decodeBase64(d));
	}

	@Test
	public void testCompactDeserialization() {
		String expected = "{\"iss\":\"joe\",\n" + "   \"exp\":1300819380,\n" + "   \"http://example.com/is_root\":true}";

		String signing = "eyJhbGciOiJFUzI1NiJ9.eyJpc3MiOiJqb2UiLAogICAiZXhwIjoxMzAwODE5MzgwLAogICAiaHR0cDovL2V4YW1wbGUuY29tL2lzX3Jvb3QiOnRydWV9.HvqgOJII0U4cvAOmxj4zwcinsKAQy2FWuQicmoT0PJFs4TkhPC-TNFxr818-3Uqo-WsQSxspvKrasmFNuirJUg";

		JwsSerializer jwsSerializer = new JwsSerializer(signing, publicKey.getEncoded());
		String actual = jwsSerializer.compactDeserialization();

		assertThat(actual, is(expected));
	}
}
