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

import org.junit.Before;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by 박병찬 on 2015-07-14.
 */
public class JwsVerifyHmac256Test {
	@Before public void setUp() throws Exception {
	}

	@Test public void testEncodedHeader() throws UnsupportedEncodingException {
		String expected = "{\"iss\":\"joe\",\n" + "   \"exp\":1300819380,\n" + "   \"http://example.com/is_root\":true}";
		String signing = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJqb2UiLAogICAiZXhwIjoxMzAwODE5MzgwLAogICAiaHR0cDovL2V4YW1wbGUuY29tL2lzX3Jvb3QiOnRydWV9.NnnMCS7jsU-kBIm3oJIc5xEHLGzzXLX6O2wVxlslAgo";
		String symmetircKey = "AyM1SysPpbyDfgZld3umj1qzKObwVMkoqQ-EstJQLr_T-1qS0gZH75aKtMN3Yj0iPS4hcgUuTwjAzZr1Z9CAow";

		JwsSerializer serializer = new JwsSerializer(signing, symmetircKey.getBytes());
		String actual = serializer.compactDeserialization();

		assertThat(actual, is(expected));
	}
}
