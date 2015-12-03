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
import com.skplanet.jose.commons.codec.binary.Base64;
import com.skplanet.jose.exception.IllegalAuthenticationTag;
import com.skplanet.jose.jwa.Jwa;
import com.skplanet.jose.jwa.JweAlgorithm;
import com.skplanet.jose.jwa.alg.AesKeyWrapAlgorithm;
import com.skplanet.jose.jwa.alg.JweAlgResult;
import com.skplanet.jose.jwa.crypto.Algorithm;
import com.skplanet.jose.jwa.enc.AesEncryptionWithHmacSha;
import com.skplanet.jose.jwa.enc.ContentEncryptKeyGenerator;
import com.skplanet.jose.jwa.enc.JweEncResult;
import com.skplanet.jose.util.ByteUtils;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class Jwe_A128KW_A128CBC_HS256Test {
	@Test
	public void TestAesKeyWrap() {
		byte[] cek = { (byte) 4, (byte) 211, (byte) 31, (byte) 197, (byte) 84, (byte) 157, (byte) 252,
				(byte) 254, (byte) 11, (byte) 100, (byte) 157, (byte) 250, (byte) 63, (byte) 170, (byte) 106,
				(byte) 206, (byte) 107, (byte) 124, (byte) 212, (byte) 45, (byte) 111, (byte) 107, (byte) 9,
				(byte) 219, (byte) 200, (byte) 177, (byte) 0, (byte) 240, (byte) 143, (byte) 156, (byte) 44, (byte) 207 };
		ContentEncryptKeyGenerator cekGenerator = new ContentEncryptKeyGenerator(32);
		cekGenerator.setUserEncryptionKey(cek);

		byte[] key = Base64.decodeBase64("GawgguFyGrWKav7AX4VKUg");

		JweAlgorithm jweAlgorithm = new AesKeyWrapAlgorithm(16);
		JweAlgResult jweAlgResult = jweAlgorithm.encryption(key, cekGenerator);
		byte[] actual = jweAlgResult.getEncryptedCek();

		assertThat(Base64.encodeBase64URLSafeString(actual), is("6KB707dM9YTIgHtLvtgWQ8mKwboJW3of9locizkDTHzBC2IlrT1oOQ"));
	}
	
	@Test
	public void TestEncryption() {
		byte[] payload = { (byte) 76, (byte) 105, (byte) 118, (byte) 101, (byte) 32, (byte) 108, (byte) 111, (byte) 110,
				(byte) 103, (byte) 32, (byte) 97, (byte) 110, (byte) 100, (byte) 32, (byte) 112, (byte) 114,
				(byte) 111, (byte) 115, (byte) 112, (byte) 101, (byte) 114, (byte) 46 };

		byte[] secret = { (byte) 4, (byte) 211, (byte) 31, (byte) 197, (byte) 84, (byte) 157, (byte) 252, (byte) 254,
				(byte) 11, (byte) 100, (byte) 157, (byte) 250, (byte) 63, (byte) 170, (byte) 106, (byte) 206,
				(byte) 107, (byte) 124, (byte) 212, (byte) 45, (byte) 111, (byte) 107, (byte) 9, (byte) 219,
				(byte) 200, (byte) 177, (byte) 0, (byte) 240, (byte) 143, (byte) 156, (byte) 44, (byte) 207 };

		byte[] iv = { (byte) 3, (byte) 22, (byte) 60, (byte) 12, (byte) 43, (byte) 67, (byte) 104, (byte) 105,
				(byte) 108, (byte) 108, (byte) 105, (byte) 99, (byte) 111, (byte) 116, (byte) 104, (byte) 101 };

		byte[] key = ByteUtils.firstHalf(secret);

		AesEncryptionWithHmacSha jweEncryption = new AesEncryptionWithHmacSha(32, 16, Algorithm.HS256);
		JweEncResult result = jweEncryption.encryptAndSign(secret, iv, payload, new byte[0]);

		assertThat(Base64.encodeBase64URLSafeString(result.getCipherText()), is("KDlTtXchhZTGufMYmOYGS4HffxPSUrfmqCHXaI9wOGY"));
	}

	@Test
	public void TestJweSerialize() {
		byte[] cek = { (byte) 4, (byte) 211, (byte) 31, (byte) 197, (byte) 84, (byte) 157, (byte) 252,
				(byte) 254, (byte) 11, (byte) 100, (byte) 157, (byte) 250, (byte) 63, (byte) 170, (byte) 106,
				(byte) 206, (byte) 107, (byte) 124, (byte) 212, (byte) 45, (byte) 111, (byte) 107, (byte) 9,
				(byte) 219, (byte) 200, (byte) 177, (byte) 0, (byte) 240, (byte) 143, (byte) 156, (byte) 44, (byte) 207 };

		byte[] key = Base64.decodeBase64("GawgguFyGrWKav7AX4VKUg");

		byte[] iv = { (byte) 3, (byte) 22, (byte) 60, (byte) 12, (byte) 43, (byte) 67, (byte) 104, (byte) 105,
				(byte) 108, (byte) 108, (byte) 105, (byte) 99, (byte) 111, (byte) 116, (byte) 104, (byte) 101 };

		byte[] payload = { (byte) 76, (byte) 105, (byte) 118, (byte) 101, (byte) 32, (byte) 108, (byte) 111, (byte) 110,
				(byte) 103, (byte) 32, (byte) 97, (byte) 110, (byte) 100, (byte) 32, (byte) 112, (byte) 114,
				(byte) 111, (byte) 115, (byte) 112, (byte) 101, (byte) 114, (byte) 46 };

		String expected = "eyJhbGciOiJBMTI4S1ciLCJlbmMiOiJBMTI4Q0JDLUhTMjU2In0.6KB707dM9YTIgHtLvtgWQ8mKwboJW3of9locizkDTHzBC2IlrT1oOQ.AxY8DCtDaGlsbGljb3RoZQ.KDlTtXchhZTGufMYmOYGS4HffxPSUrfmqCHXaI9wOGY.U0m_YmjN04DJvceFICbCVQ";

		JweSerializer serializer = new JweSerializer(
				new JoseHeader(Jwa.A128KW, Jwa.A128CBC_HS256),
				new String(payload),
				key);
		serializer.setUserEncryptionKey(cek, iv);
		String result = serializer.compactSerialization();
		
		assertThat(result, is(expected));
	}

	@Test
	public void testAesKeyUnwrap() {
		byte[] cek = Base64.decodeBase64("6KB707dM9YTIgHtLvtgWQ8mKwboJW3of9locizkDTHzBC2IlrT1oOQ");
		byte[] key = Base64.decodeBase64("GawgguFyGrWKav7AX4VKUg");
		byte[] expected = { (byte) 4, (byte) 211, (byte) 31, (byte) 197, (byte) 84, (byte) 157, (byte) 252, (byte) 254,
				(byte) 11, (byte) 100, (byte) 157, (byte) 250, (byte) 63, (byte) 170, (byte) 106, (byte) 206,
				(byte) 107, (byte) 124, (byte) 212, (byte) 45, (byte) 111, (byte) 107, (byte) 9, (byte) 219,
				(byte) 200, (byte) 177, (byte) 0, (byte) 240, (byte) 143, (byte) 156, (byte) 44, (byte) 207 };

		JweAlgorithm jweAlgorithm = new AesKeyWrapAlgorithm(16);
		byte[] actual = jweAlgorithm.decryption(key, cek);

		assertThat(ByteUtils.equals(actual, expected), is(true));
	}

	@Test
	public void testVerifyAt() {
		String header = "eyJhbGciOiJBMTI4S1ciLCJlbmMiOiJBMTI4Q0JDLUhTMjU2In0";

		byte[] iv = { (byte) 3, (byte) 22, (byte) 60, (byte) 12, (byte) 43, (byte) 67, (byte) 104, (byte) 105,
				(byte) 108, (byte) 108, (byte) 105, (byte) 99, (byte) 111, (byte) 116, (byte) 104, (byte) 101 };

		String cipherText = "KDlTtXchhZTGufMYmOYGS4HffxPSUrfmqCHXaI9wOGY";

		byte[] cek = { (byte) 4, (byte) 211, (byte) 31, (byte) 197, (byte) 84, (byte) 157, (byte) 252, (byte) 254,
				(byte) 11, (byte) 100, (byte) 157, (byte) 250, (byte) 63, (byte) 170, (byte) 106, (byte) 206,
				(byte) 107, (byte) 124, (byte) 212, (byte) 45, (byte) 111, (byte) 107, (byte) 9, (byte) 219,
				(byte) 200, (byte) 177, (byte) 0, (byte) 240, (byte) 143, (byte) 156, (byte) 44, (byte) 207 };

		String expected = "U0m_YmjN04DJvceFICbCVQ";

		AesEncryptionWithHmacSha jweEncryption = new AesEncryptionWithHmacSha(32, 16, Algorithm.HS256);

		try {
			jweEncryption.verifyAndDecrypt(cek, iv, Base64.decodeBase64(cipherText), header.getBytes(), Base64.decodeBase64(expected));
		} catch (IllegalAuthenticationTag e) {
			fail();
		}
	}

	@Test
	public void testDecryption() {
		String header = "eyJhbGciOiJBMTI4S1ciLCJlbmMiOiJBMTI4Q0JDLUhTMjU2In0";

		byte[] cek = { (byte) 4, (byte) 211, (byte) 31, (byte) 197, (byte) 84, (byte) 157, (byte) 252, (byte) 254,
				(byte) 11, (byte) 100, (byte) 157, (byte) 250, (byte) 63, (byte) 170, (byte) 106, (byte) 206,
				(byte) 107, (byte) 124, (byte) 212, (byte) 45, (byte) 111, (byte) 107, (byte) 9, (byte) 219,
				(byte) 200, (byte) 177, (byte) 0, (byte) 240, (byte) 143, (byte) 156, (byte) 44, (byte) 207 };

		byte[] iv = { (byte) 3, (byte) 22, (byte) 60, (byte) 12, (byte) 43, (byte) 67, (byte) 104, (byte) 105,
				(byte) 108, (byte) 108, (byte) 105, (byte) 99, (byte) 111, (byte) 116, (byte) 104, (byte) 101 };

		String cipherText = "KDlTtXchhZTGufMYmOYGS4HffxPSUrfmqCHXaI9wOGY";

		String at = "U0m_YmjN04DJvceFICbCVQ";
		String expected = "Live long and prosper.";

		AesEncryptionWithHmacSha jweEncryption = new AesEncryptionWithHmacSha(32, 16, Algorithm.HS256);
		byte[] actual = jweEncryption.verifyAndDecrypt(cek, iv, Base64.decodeBase64(cipherText), header.getBytes(), Base64.decodeBase64(at));

		assertThat(Arrays.equals(actual, expected.getBytes()), is(true));
	}

	@Test
	public void testJweDeserialize() {
		String serializedSource = "eyJhbGciOiJBMTI4S1ciLCJlbmMiOiJBMTI4Q0JDLUhTMjU2In0.6KB707dM9YTIgHtLvtgWQ8mKwboJW3of9locizkDTHzBC2IlrT1oOQ.AxY8DCtDaGlsbGljb3RoZQ.KDlTtXchhZTGufMYmOYGS4HffxPSUrfmqCHXaI9wOGY.U0m_YmjN04DJvceFICbCVQ";
		byte[] key = Base64.decodeBase64("GawgguFyGrWKav7AX4VKUg");

		String expected = "Live long and prosper.";

		String actual = new Jose().configuration(JoseBuilders.compactDeserializationBuilder()
				.serializedSource(serializedSource)
				.key(key)
		).deserialization();

		assertThat(actual, is(expected));
	}
}
