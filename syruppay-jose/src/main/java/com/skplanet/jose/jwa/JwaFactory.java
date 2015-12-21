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

package com.skplanet.jose.jwa;

import com.skplanet.jose.jwa.alg.*;
import com.skplanet.jose.jwa.crypto.Algorithm;
import com.skplanet.jose.jwa.enc.AesEncryptionWithHmacSha;
import com.skplanet.jose.jwa.enc.AesGcmEncryption;

import java.util.HashMap;

public class JwaFactory {
	private static HashMap<Jwa, JweAlgorithm> supportedJweAlgorithm = new HashMap<Jwa, JweAlgorithm>();
	private static HashMap<Jwa, JwsAlgorithm> supportedJwsAlgorithm = new HashMap<Jwa, JwsAlgorithm>();
	private static HashMap<Jwa, JweEncryption> supportedJweEncryption = new HashMap<Jwa, JweEncryption>();

	static {
		supportedJweAlgorithm.put(Jwa.A128KW, new AesKeyWrapAlgorithm());
		supportedJweAlgorithm.put(Jwa.A256KW, new AesKeyWrapAlgorithm(32));
		supportedJweAlgorithm.put(Jwa.RSA1_5, new RSA15Algorithm());
		supportedJweAlgorithm.put(Jwa.RSA_OAEP, new RSAOAEPAlgorithm());
		supportedJweAlgorithm.put(Jwa.DIR, new DirectKeyAlgorithm());

		supportedJwsAlgorithm.put(Jwa.HS256, new HmacSha256Algorithm());
		supportedJwsAlgorithm.put(Jwa.RS256, new Sha256WithRsaAlgorithm());
		supportedJwsAlgorithm.put(Jwa.ES256, new Sha256WithECDSAUsingP256Algorithm());

		supportedJweEncryption.put(Jwa.A128CBC_HS256, new AesEncryptionWithHmacSha(32, 16, Algorithm.HS256));
		supportedJweEncryption.put(Jwa.A256CBC_HS512, new AesEncryptionWithHmacSha(64, 16, Algorithm.HS512));
		supportedJweEncryption.put(Jwa.A128GCM, new AesGcmEncryption(16, 12, Algorithm.NONE));
		supportedJweEncryption.put(Jwa.A256GCM, new AesGcmEncryption(32, 12, Algorithm.NONE));
	}

	public static JweAlgorithm getJweAlgorithm(Jwa jwa) {
		return supportedJweAlgorithm.get(jwa);
	}

	public static JwsAlgorithm getJwsAlgorithm(Jwa jwa) {
		return supportedJwsAlgorithm.get(jwa);
	}
	
	public static JweEncryption getJweEncryption(Jwa jwa) {
		return supportedJweEncryption.get(jwa);
	}
}
