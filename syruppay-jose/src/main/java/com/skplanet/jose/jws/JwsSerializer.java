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

import com.skplanet.jose.JoseAction;
import com.skplanet.jose.JoseHeader;
import com.skplanet.jose.commons.codec.binary.Base64;
import com.skplanet.jose.exception.IllegalSignatureToken;
import com.skplanet.jose.jwa.Jwa;
import com.skplanet.jose.jwa.JwaFactory;
import com.skplanet.jose.jwa.JwsAlgorithm;
import com.skplanet.jose.util.StringUtils;

/**
 * Created by 박병찬 on 2015-07-17.
 */
public class JwsSerializer implements JoseAction {
	private byte[] key;

	private JwsParts jwsParts = new JwsParts();
	private JoseHeader userJoseHeader;

	public JwsSerializer(JoseHeader header, String payload, byte[] key) {
		jwsParts.joseHeader = header;
		jwsParts.payload = StringUtils.stringtoBytes(payload);
		this.key = key;
	}

	public JwsSerializer(String src, byte[] key) {
		jwsParts.parse(src);
		this.key = key;
	}

	public void setUserJoseHeader(JoseHeader userJoseHeader) {
		this.userJoseHeader = userJoseHeader;
	}

	public String compactSerialization() {
		Jwa alg = jwsParts.joseHeader.getAlgorithm();
		JwsAlgorithm jwsAlgorithm = JwaFactory.getJwsAlgorithm(alg);

		jwsParts.signature = jwsAlgorithm.sign(key, jwsParts.getSignatureSource());

		return jwsParts.toString();
	}

	private Jwa getDeserializeAlgorithm() {
		if (userJoseHeader != null && userJoseHeader.getAlgorithm() != null)
			return userJoseHeader.getAlgorithm();
		else
			return jwsParts.joseHeader.getAlgorithm();
	}

	public String compactDeserialization() {
		JwsAlgorithm jwsAlgorithm = JwaFactory.getJwsAlgorithm(getDeserializeAlgorithm());

		byte[] signSource = jwsParts.getSignatureSource();
		byte[] expected = jwsParts.signature;

		if (!jwsAlgorithm.verify(key, signSource, expected)) {
			throw new IllegalSignatureToken("invalid signature. expected:["+expected+"] actual:["+signSource+"]");
		}

		return StringUtils.byteToString(jwsParts.payload);
	}

	public JoseHeader getJoseHeader() {
		return jwsParts.joseHeader;
	}

	private class JwsParts {
		public JoseHeader joseHeader;
		public byte[] payload;
		public byte[] signature;

		@Override
		public String toString() {
			return String.format("%s.%s.%s",
					joseHeader.getEncoded(),
					Base64.encodeBase64URLSafeString(payload),
					Base64.encodeBase64URLSafeString(signature)
					);
		}

		public byte[] getSignatureSource() {
			return StringUtils.stringtoBytes(
					String.format("%s.%s", joseHeader.getEncoded(), Base64.encodeBase64URLSafeString(payload)));
		}

		public void parse(String src) {
			if (src == null || src.length() == 0) {
				throw new IllegalSignatureToken();
			}

			String[] token = src.split("\\.");
			if (token == null || token.length != 3) {
				throw new IllegalSignatureToken("Unknown jws signature format");
			}

			joseHeader = new JoseHeader();
			joseHeader.setEncoded(token[0]);
			payload = Base64.decodeBase64(token[1]);
			signature = Base64.decodeBase64(token[2]);
		}
	}
}
