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

package com.skplanet.jose.support;

import com.skplanet.jose.Jose;
import com.skplanet.jose.JoseHeader;
import com.skplanet.jose.jwa.Jwa;
import com.skplanet.jose.jwe.JweSerializer;

import java.security.PrivateKey;
import java.security.PublicKey;

import static com.skplanet.jose.JoseBuilders.*;

/**
 * JWE 규격의 암복화 처리를 호출하는 클래스
 *
 * @author 1000808 (byeongchan.park@sk.com)
 */
@Deprecated
public class JoseSupport {
	/**
	 * JOSE 규격 alg : A128KW, enc : A128CBC-HS256의 JWE 데이터를 생성한다.
	 *
	 * @param kid          : JOSE Header에 입력할 kid
	 * @param symmetricKey : 대칭키
	 * @param src          : 암호화 데이터
	 * @return 정상인경우 jwe string, build 오류인 경우 exception throw 하거나 null을 리턴한다.
	 */
	public static String build_a128kw(String kid, byte[] symmetricKey, String src) {
		return new Jose().configuration(
				JsonEncryptionCompactSerializationBuilder()
						.header(new JoseHeader(Jwa.A128KW, Jwa.A128CBC_HS256, kid))
						.payload(src)
						.key(symmetricKey)
		).serialization();
	}

	/**
	 * JOSE 규격 alg : A128KW, enc : A128CBC-HS256의 JWE 데이터를 생성한다.<p>
	 * 이 메소드는 암호화의 검증을 위해 사용한다. 서비스에서는 사용하지 말아야 한다.
	 *
	 * @param symmetricKey : 대칭키
	 * @param src : 암호화 데이터
	 * @param cek : content encryption key
	 * @param iv  : Initialization Vector
	 * @return 정상인경우 jwe string, build 오류인 경우 exception throw 하거나 null을 리턴한다.
	 */
	public static String build_a128kw(String kid, byte[] symmetricKey, String src, byte[] cek, byte[] iv) {
		Jose jose = new Jose();
		jose.configuration(JsonEncryptionCompactSerializationBuilder()
				.header(new JoseHeader(Jwa.A128KW, Jwa.A128CBC_HS256, kid))
				.payload(src)
				.key(symmetricKey));
		JweSerializer joseAction = (JweSerializer) jose.getInstance();
		joseAction.setUserEncryptionKey(cek, iv);

		return joseAction.compactSerialization();
	}

	/**
	 * JOSE 규격 JOSE 규격 alg : A128KW, enc : A128CBC-HS256의 JOSE 규격을 검증한다.
	 * 만약 kid가 있으면 kid를 리턴한다.
	 *
	 * @param src : 암호화 데이터
	 * @return verify 오류인 경우 exception throw 하거나 null을 리턴한다.
	 */
	public static String verify_a128kwHeader(String src) {
		JoseHeader joseHeader = new JoseHeader();
		joseHeader.setEncoded(src);
		return joseHeader.getHeader(JoseHeader.JoseHeaderKeySpec.KEY_ID);
	}

	/**
	 * JOSE 규격 alg : A128KW, enc : A128CBC-HS256의 JWE 데이터를 복호화 한다.
	 *
	 * @param symmetricKey : 대칭키
	 * @param src : 복호화할 데이터
	 * @return 정상인경우 json string, verify 오류인 경우 exception throw 하거나 null을 리턴한다.
	 */
	public static String verify_a128kw(byte[] symmetricKey, String src) {
		return new Jose().configuration(
				compactDeserializationBuilder()
						.serializedSource(src)
						.key(symmetricKey)
		).deserialization();
	}

	/**
	 * JOSE 규격 alg : RSA1_5, enc : A128CBC-HS256의 JWE 데이터를 생성한다.
	 *
	 * @param kid       : JOSE Header에 입력할 kid
	 * @param publicKey : 공개키
	 * @param src       : 암호화 데이터
	 * @return 정상인경우 jwe string, build 오류인 경우 exception throw 하거나 null을 리턴한다.
	 */
	public static String build_rsa15(String kid, byte[] publicKey, String src) {
		return new Jose().configuration(
				JsonEncryptionCompactSerializationBuilder()
						.header(new JoseHeader(Jwa.RSA1_5, Jwa.A128CBC_HS256, kid))
						.payload(src)
						.key(publicKey)
		).serialization();
	}

	/**
	 * JOSE 규격 JOSE 규격 alg : RSA1_5, enc : A128CBC-HS256의 JOSE 규격을 검증한다.
	 * 만약 kid가 있으면 kid를 리턴한다.
	 *
	 * @param src : 암호화 데이터
	 * @return verify 오류인 경우 exception throw 하거나 null을 리턴한다.
	 */
	public static String verify_ras15Header(String src) {
		JoseHeader joseHeader = new JoseHeader();
		joseHeader.setEncoded(src);
		return joseHeader.getHeader(JoseHeader.JoseHeaderKeySpec.KEY_ID);
	}

	/**
	 * JOSE 규격 alg : RSA1_5, enc : A128CBC-HS256의 JWE 데이터를 복호화 한다.
	 *
	 * @param privateKey : 개인키
	 * @param src        : 암호화 데이터
	 * @return 정상인경우 json string, verify 오류인 경우 exception throw 하거나 null을 리턴한다.
	 */
	public static String verify_rsa15(byte[] privateKey, String src) {
		return new Jose().configuration(
				compactDeserializationBuilder()
						.serializedSource(src)
						.key(privateKey)
		).deserialization();

	}

	/**
	 * JOSE 규격 alg : RSA, enc : A128CBC-HS256의 JWE 데이터를 생성한다.
	 *
	 * @param kid       : JOSE Header에 입력할 kid
	 * @param publicKey : 공개키
	 * @param src       : 암호화 데이터
	 * @return 정상인경우 jwe string, build 오류인 경우 exception throw 하거나 null을 리턴한다.
	 */
	public static String build_rsaoaep(String kid, byte[] publicKey, String src) {
		return new Jose().configuration(
				JsonEncryptionCompactSerializationBuilder()
						.header(new JoseHeader(Jwa.RSA_OAEP, Jwa.A128CBC_HS256, kid))
						.payload(src).key(publicKey)
		).serialization();
	}

	/**
	 * JOSE 규격 JOSE 규격 alg : RSA, enc : A128CBC-HS256의 JOSE 규격을 검증한다.
	 * 만약 kid가 있으면 kid를 리턴한다.
	 *
	 * @param src : 암호화 데이터
	 * @return verify 오류인 경우 exception throw 하거나 null을 리턴한다.
	 */
	public static String verify_rasoaepHeader(String src) {
		JoseHeader joseHeader = new JoseHeader();
		joseHeader.setEncoded(src);
		return joseHeader.getHeader(JoseHeader.JoseHeaderKeySpec.KEY_ID);
	}

	/**
	 * JOSE 규격 alg : RSA, enc : A128CBC-HS256의 JWE 데이터를 복호화 한다.
	 *
	 * @param privateKey : 개인키
	 * @param src        : 암호화 데이터
	 * @return 정상인경우 json string, verify 오류인 경우 exception throw 하거나 null을 리턴한다.
	 */
	public static String verify_rsaoaep(byte[] privateKey, String src) {
		return new Jose().configuration(
				compactDeserializationBuilder()
						.serializedSource(src).key(privateKey)
		).deserialization();
	}

	/**
	 * JOSE 규격 alg : RSA, enc : SHA256의 JWS 데이터를 생성 한다.
	 *
	 * @param claims : Sign된 contents
	 * @param privateKey     : 개인키
	 * @return 정상인경우 json string, verify 오류인 경우 exception throw 하거나 null을 리턴한다.
	 */
	public static String Jws_RsaSign(String claims, PrivateKey privateKey) {
		JoseHeader joseHeader = new JoseHeader(Jwa.RS256);
		joseHeader.setHeader(JoseHeader.JoseHeaderKeySpec.TYPE, "JWT");

		return new Jose().configuration(
				JsonSignatureCompactSerializationBuilder()
						.header(joseHeader)
						.payload(claims)
						.key(privateKey.getEncoded())
		).serialization();
	}

	/**
	 * JOSE 규격 alg : RSA, enc : SHA256의 JWS 데이터를 Decoding 한다.
	 *
	 * @param signedContents : Sign된 contents
	 * @param publicKey      : 개인키
	 * @return 정상인경우 json string, verify 오류인 경우 exception throw 하거나 null을 리턴한다.
	 */
	public static String Jws_RsaVerify(String signedContents, PublicKey publicKey) {
		return new Jose().configuration(
				compactDeserializationBuilder()
						.serializedSource(signedContents)
						.key(publicKey.getEncoded())
		).deserialization();
	}

	/**
	 * JOSE 규격 alg : HMAC, enc : SHA256의 JWS 데이터를 생성 한다.
	 *
	 * @param claims : Sign된 contents
	 * @param symmetricKey     : 개인키
	 * @return 정상인경우 json string, verify 오류인 경우 exception throw 하거나 null을 리턴한다.
	 */
	public static String Jws_HmacSign(String claims, String symmetricKey) {
		JoseHeader joseHeader = new JoseHeader(Jwa.HS256);
		joseHeader.setHeader(JoseHeader.JoseHeaderKeySpec.TYPE, "JWT");

		return new Jose().configuration(
				JsonSignatureCompactSerializationBuilder()
						.header(joseHeader).payload(claims)
						.key(symmetricKey.getBytes())
		).serialization();
	}

	/**
	 * JOSE 규격 alg : HMAC, enc : SHA256의 JWS 데이터를 Decoding 한다.
	 *
	 * @param signedContents : Sign된 contents
	 * @param symmetricKey      : 개인키
	 * @return 정상인경우 json string, verify 오류인 경우 exception throw 하거나 null을 리턴한다.
	 */
	public static String Jws_HmacVerify(String signedContents, String symmetricKey) {
		return new Jose().configuration(
				compactDeserializationBuilder()
						.serializedSource(signedContents)
						.key(symmetricKey.getBytes())
		).deserialization();
	}

}
