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

import com.skplanet.jose.Jose;
import com.skplanet.jose.JoseBuilders;
import com.skplanet.jose.JoseHeader;
import com.skplanet.jose.jwa.Jwa;
import com.skplanet.jose.jwa.JweAlgorithm;
import com.skplanet.jose.jwa.alg.AesKeyWrapAlgorithm;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class Jwe_DIR_A128CBC_HS256Test {
	String expected = "eyJhbGciOiJkaXIiLCJlbmMiOiJBMTI4Q0JDLUhTMjU2In0..AxY8DCtDaGlsbGljb3RoZQ.1d7zsu1QRvHlo5ruft6_X6TQcoWyHrdEel4i8H9iWPc.TU6RmmPleY9xySVaODFHyQ";
	byte[] key = "12345678901234567890123456789012".getBytes();
	String payload = "Live long and prosper.";

	@Test
	public void testJweSerailize() {
		byte[] iv = { (byte) 3, (byte) 22, (byte) 60, (byte) 12, (byte) 43, (byte) 67, (byte) 104, (byte) 105,
				(byte) 108, (byte) 108, (byte) 105, (byte) 99, (byte) 111, (byte) 116, (byte) 104, (byte) 101 };

		JweSerializer serializer = new JweSerializer(
				new JoseHeader(Jwa.DIR, Jwa.A128CBC_HS256),
				new String(payload),
				key);
		serializer.setUserEncryptionKey(null, iv);
		String actual = serializer.compactSerialization();

		assertThat(actual, is(expected));
	}

	@Test
	public void testJweDeserialize() {
		String actual = new Jose().configuration(JoseBuilders.compactDeserializationBuilder()
			.serializedSource(expected)
			.key(key)
		).deserialization();

		assertThat(actual, is(payload));
	}
}
