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
import com.skplanet.jose.commons.codec.binary.Base64;
import com.skplanet.jose.jwa.Jwa;
import com.skplanet.jose.jwa.JweAlgorithm;
import com.skplanet.jose.jwa.alg.AesKeyWrapAlgorithm;
import com.skplanet.jose.jwa.enc.AesEncryptionWithHmacSha;
import com.skplanet.jose.util.ByteUtils;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class JweVerifyAES128Test {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testVerifyJoseHeader() {
		String header = "eyJhbGciOiJBMTI4S1ciLCJlbmMiOiJBMTI4Q0JDLUhTMjU2In0";

		JoseHeader joseHeader = new JoseHeader();
		joseHeader.setEncoded(header);
		String kid = joseHeader.getHeader(JoseHeader.JoseHeaderKeySpec.KEY_ID);

		assertThat(joseHeader.getAlgorithm().getValue(), is(Jwa.A128KW.getValue()));
		assertThat(joseHeader.getEncrytion().getValue(), is(Jwa.A128CBC_HS256.getValue()));
	}

	@Test
	public void testGetDecodeCek() {
		byte[] cek = Base64.decodeBase64("6KB707dM9YTIgHtLvtgWQ8mKwboJW3of9locizkDTHzBC2IlrT1oOQ");
		byte[] key = Base64.decodeBase64("GawgguFyGrWKav7AX4VKUg");

		JweAlgorithm jweAlgorithm = new AesKeyWrapAlgorithm(16);
		byte[] actual = jweAlgorithm.decryption(key, cek);

		byte[] expected = { (byte) 4, (byte) 211, (byte) 31, (byte) 197, (byte) 84, (byte) 157, (byte) 252, (byte) 254,
				(byte) 11, (byte) 100, (byte) 157, (byte) 250, (byte) 63, (byte) 170, (byte) 106, (byte) 206,
				(byte) 107, (byte) 124, (byte) 212, (byte) 45, (byte) 111, (byte) 107, (byte) 9, (byte) 219,
				(byte) 200, (byte) 177, (byte) 0, (byte) 240, (byte) 143, (byte) 156, (byte) 44, (byte) 207 };

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

		byte[] key = new byte[16];
		System.arraycopy(cek, 0, key, 0, 16);

		AesEncryptionWithHmacSha jweEncryption = new AesEncryptionWithHmacSha(32, 16);
//		jweEncryption.verifyAuthenticationTag(key, iv, Base64.decodeBase64(cipherText), header.getBytes(),
//				Base64.decodeBase64(expected));
	}
	
	@Test
	public void testGetContent() {
		byte[] cek = { (byte) 4, (byte) 211, (byte) 31, (byte) 197, (byte) 84, (byte) 157, (byte) 252, (byte) 254,
				(byte) 11, (byte) 100, (byte) 157, (byte) 250, (byte) 63, (byte) 170, (byte) 106, (byte) 206,
				(byte) 107, (byte) 124, (byte) 212, (byte) 45, (byte) 111, (byte) 107, (byte) 9, (byte) 219,
				(byte) 200, (byte) 177, (byte) 0, (byte) 240, (byte) 143, (byte) 156, (byte) 44, (byte) 207 };
		
		byte[] iv = { (byte) 3, (byte) 22, (byte) 60, (byte) 12, (byte) 43, (byte) 67, (byte) 104, (byte) 105,
				(byte) 108, (byte) 108, (byte) 105, (byte) 99, (byte) 111, (byte) 116, (byte) 104, (byte) 101 };

		String cipherText = "KDlTtXchhZTGufMYmOYGS4HffxPSUrfmqCHXaI9wOGY";
		
		String expected = "Live long and prosper.";

		byte[] key = new byte[16];
		System.arraycopy(cek, 16, key, 0, 16);

//		AesEncryptionWithHmacSha jweEncryption = new AesEncryptionWithHmacSha();
//		byte[] actual = jweEncryption.decryption(key, iv, Base64.decodeBase64(cipherText));
		
//		assertThat(new String(actual), is(expected));
	}
	
	@Test
	public void testVerify() {
		String encryptionData = "eyJhbGciOiJBMTI4S1ciLCJlbmMiOiJBMTI4Q0JDLUhTMjU2In0.6KB707dM9YTIgHtLvtgWQ8mKwboJW3of9locizkDTHzBC2IlrT1oOQ.AxY8DCtDaGlsbGljb3RoZQ.KDlTtXchhZTGufMYmOYGS4HffxPSUrfmqCHXaI9wOGY.U0m_YmjN04DJvceFICbCVQ";
		byte[] symmetricKey = Base64.decodeBase64("GawgguFyGrWKav7AX4VKUg");
		
		String expected = "Live long and prosper.";

		JweSerializer serializer = new JweSerializer(encryptionData, symmetricKey);
		String actual = serializer.compactDeserialization();

		assertThat(actual, is(expected));
	}
}
