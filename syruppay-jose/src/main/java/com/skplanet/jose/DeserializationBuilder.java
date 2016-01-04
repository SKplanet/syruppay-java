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

import com.skplanet.jose.jwa.Jwa;
import com.skplanet.jose.jwe.JweSerializer;
import com.skplanet.jose.jws.JwsSerializer;

/**
 * Created by 박병찬 on 2015-07-30.
 */
public class DeserializationBuilder extends JoseCompactBuilder {
	private JoseHeader userJoseHeader;
	private String serializedSource;

	public DeserializationBuilder(JoseActionType joseActionType) {
		super.compactBuilder(joseActionType);
	}

	public DeserializationBuilder(JoseMethod joseMethod, JoseActionType joseActionType) {
		super.compactBuilder(joseMethod, joseActionType);
	}

	/**
	 * JWE, JWS content
	 * @param serializedSource
	 * @return
	 */
	public DeserializationBuilder serializedSource(String serializedSource) {
		this.serializedSource = serializedSource;
		return this;
	}

	/**
	 * deserialize payload as specific JOSE header 'alg'
	 *
	 * @param alg {@link Jwa}
	 * @return
	 */
	public DeserializationBuilder userAlgorithm(Jwa alg) {
		this.userJoseHeader = new JoseHeader(alg);
		return this;
	}

	/**
	 * deserialize payload as specific JOSE header 'alg' and 'enc'
	 *
	 * @param alg {@link Jwa}
	 * @param enc {@link Jwa}
	 * @return
	 */
	public DeserializationBuilder userAlgorithm(Jwa alg, Jwa enc) {
		this.userJoseHeader = new JoseHeader(alg, enc);
		return this;
	}

	public JoseAction create() {
		JoseHeader header = new JoseHeader();
		header.setEncoded(serializedSource);
		joseMethod = header.getJoseMethod();

		switch (joseSerializeType) {
		case COMPACT_SERIALIZATION:
			if (JoseMethod.JWE == joseMethod && JoseActionType.DESERIALIZATION == joseActionType) {
				JweSerializer serializer = new JweSerializer(serializedSource, key);
				serializer.setUserJoseHeader(userJoseHeader);
				return serializer;
			} else if (JoseMethod.JWS == joseMethod && JoseActionType.DESERIALIZATION == joseActionType) {
				JwsSerializer serializer = new JwsSerializer(serializedSource, key);
				serializer.setUserJoseHeader(userJoseHeader);
				return serializer;
			} else {
				throw new IllegalArgumentException("unknown JoseSerializeType and JoseActionType");
			}
		case JSON_SERIALIZATION:
			return null;
		default:
			return null;
		}
	}
}
