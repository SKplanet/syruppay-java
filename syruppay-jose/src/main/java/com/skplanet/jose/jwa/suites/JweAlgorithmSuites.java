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

package com.skplanet.jose.jwa.suites;

import com.skplanet.jose.jwa.Jwa;

/**
 * Created by byeongchan.park@sk.com(1000808) on 2015-12-29.
 */
public enum JweAlgorithmSuites {
	A128KWAndA128CBC_HS256(Jwa.A128KW, Jwa.A128CBC_HS256),
	A256KWAndA128CBC_HS256(Jwa.A256KW, Jwa.A128CBC_HS256),
	RSA1_5AndA128CBC_HS256(Jwa.RSA1_5, Jwa.A128CBC_HS256),
	RSA_OAEPAndA128CBC_HS256(Jwa.RSA_OAEP, Jwa.A128CBC_HS256);

	private Jwa keyWrapAlgorithm;
	private Jwa contentEncryptionAlgorithm;

	private JweAlgorithmSuites(Jwa keyWrapAlgorithm, Jwa contentEncryptionAlgorithm) {
		this.keyWrapAlgorithm = keyWrapAlgorithm;
		this.contentEncryptionAlgorithm = contentEncryptionAlgorithm;
	}

	public Jwa getKeyWrapAlgorithm() {
		return keyWrapAlgorithm;
	}

	public Jwa getContentEncryptionAlgorithm() {
		return contentEncryptionAlgorithm;
	}

	public boolean equals(JweAlgorithmSuites jweAlgorithmSuites) {
		if (jweAlgorithmSuites != null &&
				jweAlgorithmSuites.getKeyWrapAlgorithm().equals(keyWrapAlgorithm) &&
				jweAlgorithmSuites.getContentEncryptionAlgorithm().equals(contentEncryptionAlgorithm)) {
			return true;
		}

		return false;
	}
}
