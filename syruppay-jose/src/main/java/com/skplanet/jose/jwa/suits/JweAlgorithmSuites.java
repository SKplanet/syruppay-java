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

package com.skplanet.jose.jwa.suits;

import com.skplanet.jose.jwa.Jwa;

/**
 * Created by byeongchan.park@sk.com(1000808) on 2015-12-22.
 */
public class JweAlgorithmSuites {
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

	public static class A128KWAndA128CBC_HS256 extends JweAlgorithmSuites {
		public A128KWAndA128CBC_HS256() {
			super(Jwa.A128KW, Jwa.A128CBC_HS256);
		}
	}

	public static class A256KWAndA128CBC_HS256 extends JweAlgorithmSuites {
		public A256KWAndA128CBC_HS256() {
			super(Jwa.A256KW, Jwa.A128CBC_HS256);
		}
	}

	public static class A256KWAndA256CBC_HS512 extends JweAlgorithmSuites {
		public A256KWAndA256CBC_HS512() {
			super(Jwa.A256KW, Jwa.A256CBC_HS512);
		}
	}


	public static class A256KWAndA128GCM extends JweAlgorithmSuites {
		public A256KWAndA128GCM() {
			super(Jwa.A256KW, Jwa.A128GCM);
		}
	}

	public static class A256KWAndA256GCM extends JweAlgorithmSuites {
		public A256KWAndA256GCM() {
			super(Jwa.A256KW, Jwa.A256GCM);
		}
	}
}
