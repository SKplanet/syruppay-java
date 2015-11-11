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

package com.skplanet.jose.jwe;

import com.skplanet.jose.JoseHeader;
import com.skplanet.jose.jwa.Jwa;
import com.skplanet.jose.jwa.JweAlgorithm;
import com.skplanet.jose.jwa.alg.AesKeyWrapAlgorithm;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class JweDirectKeyTest {
	String expected = "eyJhbGciOiJkaXIiLCJlbmMiOiJBMTI4Q0JDLUhTMjU2In0..AxY8DCtDaGlsbGljb3RoZQ.1d7zsu1QRvHlo5ruft6_X6TQcoWyHrdEel4i8H9iWPc.TU6RmmPleY9xySVaODFHyQ";
	byte[] symmetricKey = "12345678901234567890123456789012".getBytes();

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testSerailize() {
		byte[] iv = { (byte) 3, (byte) 22, (byte) 60, (byte) 12, (byte) 43, (byte) 67, (byte) 104, (byte) 105,
				(byte) 108, (byte) 108, (byte) 105, (byte) 99, (byte) 111, (byte) 116, (byte) 104, (byte) 101 };

		byte[] raw = { (byte) 76, (byte) 105, (byte) 118, (byte) 101, (byte) 32, (byte) 108, (byte) 111, (byte) 110,
				(byte) 103, (byte) 32, (byte) 97, (byte) 110, (byte) 100, (byte) 32, (byte) 112, (byte) 114,
				(byte) 111, (byte) 115, (byte) 112, (byte) 101, (byte) 114, (byte) 46 };

		JweSerializer serializer = new JweSerializer(
				new JoseHeader(Jwa.DIR, Jwa.A128CBC_HS256),
				new String(raw),
				symmetricKey);
		serializer.setUserEncryptionKey(null, iv);
		String actual = serializer.compactSerialization();

		assertThat(actual, is(expected));
	}

	@Test
	public void testDeserialize() {
		byte[] symmetricKey = "12345678901234567890123456789012".getBytes();

		String plainExpected = "Live long and prosper.";

		JweSerializer serializer = new JweSerializer(expected, symmetricKey);
		String actual = serializer.compactDeserialization();

		assertThat(actual, is(plainExpected));
	}
}
