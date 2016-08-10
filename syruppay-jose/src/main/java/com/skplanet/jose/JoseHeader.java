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

import com.skplanet.jose.commons.codec.binary.Base64;
import com.skplanet.jose.exception.IllegalJoseHeaderException;
import com.skplanet.jose.exception.UnsupportedJOSEAlgorithm;
import com.skplanet.jose.jwa.Jwa;
import com.skplanet.jose.jwa.JwaFactory;
import com.skplanet.jose.util.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by 박병찬 on 2015-07-14.
 */
public class JoseHeader {
	private String encoded;
	private Map<String, String> header = new LinkedHashMap<String, String>();

	public JoseHeader() {
	}

	public JoseHeader(Jwa alg) {
		this(alg, null, null);
	}

	public JoseHeader(Jwa alg, String kid) {
		this(alg, null, kid);
	}

	public JoseHeader(Jwa alg, Jwa enc) {
		this(alg, enc, null);
	}

	public JoseHeader(Jwa alg, Jwa enc, String kid) {
		setDefaultHeader(alg, enc, kid);
	}

	protected void setDefaultHeader(Jwa alg, Jwa enc, String kid) {
		setAlgorithm(alg);
		setEncryption(enc);

		if (kid != null && kid.length() > 0)
			setHeader(JoseHeaderKeySpec.KEY_ID, kid);
	}

	public JoseHeader setHeader(String key, String value) {
		header.put(key, value);
		return this;
	}

	public String getHeader(String key) {
		return header.get(key);
	}

	public JoseHeader setAlgorithm(Jwa alg) {
		if (!JoseSupportAlgorithm.isSupported(alg)) {
			throw new IllegalJoseHeaderException("unsupported algoritm "+alg.getValue());
		}

		setHeader(JoseHeaderKeySpec.ALGORITHM, alg.getValue());
		return this;
	}

	public Jwa getAlgorithm() {
		return Enum.valueOf(Jwa.class, getHeader(JoseHeaderKeySpec.ALGORITHM).replace("-", "_").toUpperCase());
	}

	public JoseHeader setEncryption(Jwa enc) {
		if (enc == null) return this;

		if (!JoseSupportEncryption.isSupported(enc)) {
			throw new IllegalJoseHeaderException("unsupported encryption algorithm "+enc.getValue());
		}
		setHeader(JoseHeaderKeySpec.ENCRYPTION, enc.getValue());
		return this;
	}

	public Jwa getEncryption() {
		if (!header.containsKey(JoseHeaderKeySpec.ENCRYPTION))
			return null;
		return Enum.valueOf(Jwa.class, getHeader(JoseHeaderKeySpec.ENCRYPTION).replace("-", "_"));
	}

	public String getEncoded() {
		if (encoded != null)
			return encoded;

		if (!header.containsKey(JoseHeaderKeySpec.VER)) {
			String version = this.getClass().getPackage().getImplementationVersion();
			if (StringUtils.isNotEmpty(version))
				setHeader(JoseHeaderKeySpec.VER, version);
		}

		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return Base64.encodeBase64URLSafeString(StringUtils.stringtoBytes(objectMapper.writeValueAsString(header)));
		} catch (IOException e) {
			throw new IllegalJoseHeaderException("json encode exception");
		}
	}

	public JoseHeader setEncoded(String encoded) {
		String[] token = encoded.split("\\.");
		if (token != null && token.length == 3 || token.length == 5) {
			this.encoded = token[0];
		} else {
			this.encoded = encoded;
		}

		String json = StringUtils.byteToString(Base64.decodeBase64(this.encoded));

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			header = objectMapper.readValue(json, HashMap.class);
		} catch (Exception e) {
			throw new IllegalJoseHeaderException("base64 decode exception");
		}

		Jwa alg = getAlgorithm();
		if (!JoseSupportAlgorithm.isSupported(alg)) {
			throw new IllegalJoseHeaderException("unsupported algorithm "+alg);
		}

		Jwa enc = getEncryption();
		if (enc != null && !JoseSupportEncryption.isSupported(enc)) {
			throw new IllegalJoseHeaderException("unsupported encryption "+enc);
		}

		return this;
	}

	/**
	 * alg 알고리즘으로 JWE, JWS 여부를 판단한다.
	 *
	 * @return JoseMethod
	 * @exception UnsupportedJOSEAlgorithm
	 */
	public JoseMethod getJoseMethod() {
		if (JoseSupportAlgorithm.isJWESupported(getAlgorithm()))
			return JoseMethod.JWE;
		else if (JoseSupportAlgorithm.isJWSSupported(getAlgorithm()))
			return JoseMethod.JWS;
		else
			throw new UnsupportedJOSEAlgorithm(getAlgorithm()+" is not supported.");
	}

	public static class JoseHeaderKeySpec {
		public static final String ALGORITHM = "alg";
		public static final String ENCRYPTION = "enc";
		public static final String KEY_ID = "kid";
		public static final String TYPE = "typ";
		public static final String VER = "ver";
	}

	private static class JoseSupportAlgorithm {
		public static boolean isSupported(Jwa alg) {
			return isJWESupported(alg) || isJWSSupported(alg);
		}

		public static boolean isJWESupported(Jwa alg) {
			return JwaFactory.getJweAlgorithm(alg) != null;
		}

		public static boolean isJWSSupported(Jwa alg) {
			return JwaFactory.getJwsAlgorithm(alg) != null;
		}
	}

	private static class JoseSupportEncryption {
		public static boolean isSupported(Jwa enc) {
			return JwaFactory.getJweEncryption(enc) != null;
		}
	}
}
