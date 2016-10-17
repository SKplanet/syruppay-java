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

import com.skplanet.jose.jwe.JweSerializationBuilder;
import com.skplanet.jose.jws.JwsSerializationBuilder;

/**
 * Created by 박병찬 on 2015-07-15.
 */
public class JoseBuilders {
	public static JwsSerializationBuilder JsonSignatureCompactSerializationBuilder() {
		return new JwsSerializationBuilder();
	}

	@Deprecated
	public static DeserializationBuilder JsonSignatureCompactDeserializationBuilder() {
		return new DeserializationBuilder(JoseMethod.JWS);
	}

	public static JweSerializationBuilder JsonEncryptionCompactSerializationBuilder() {
		return new JweSerializationBuilder();
	}

	@Deprecated
	public static DeserializationBuilder JsonEncryptionCompactDeserializationBuilder() {
		return new DeserializationBuilder(JoseMethod.JWE);
	}

	public static DeserializationBuilder compactDeserializationBuilder() {
		return new DeserializationBuilder();
	}
}
