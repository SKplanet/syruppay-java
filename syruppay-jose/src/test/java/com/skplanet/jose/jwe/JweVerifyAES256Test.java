package com.skplanet.jose.jwe;

import com.skplanet.jose.commons.codec.binary.Base64;
import com.skplanet.jose.jwa.JweAlgorithm;
import com.skplanet.jose.jwa.alg.AesKeyWrapAlgorithm;
import com.skplanet.jose.util.ByteUtils;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class JweVerifyAES256Test {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetDecodeCek() {
		byte[] expected = { (byte) 4, (byte) 211, (byte) 31, (byte) 197, (byte) 84, (byte) 157, (byte) 252, (byte) 254,
				(byte) 11, (byte) 100, (byte) 157, (byte) 250, (byte) 63, (byte) 170, (byte) 106, (byte) 206,
				(byte) 107, (byte) 124, (byte) 212, (byte) 45, (byte) 111, (byte) 107, (byte) 9, (byte) 219,
				(byte) 200, (byte) 177, (byte) 0, (byte) 240, (byte) 143, (byte) 156, (byte) 44, (byte) 207 };

		byte[] cek = Base64.decodeBase64("UVf1x6nVsOmpxjlUFSiQdzbsOMYuAh3FQlH0nY3yhDWVJFh9HLtHIQ");
		byte[] key = "12345678901234567890123456789012".getBytes();

		JweAlgorithm jweAlgorithm = new AesKeyWrapAlgorithm(32);
		byte[] actual = jweAlgorithm.decryption(key, cek);

		assertThat(ByteUtils.equals(actual, expected), is(true));
	}
	
	@Test
	public void testVerify() {
		String encryptionData = "eyJhbGciOiJBMjU2S1ciLCJlbmMiOiJBMTI4Q0JDLUhTMjU2In0.UVf1x6nVsOmpxjlUFSiQdzbsOMYuAh3FQlH0nY3yhDWVJFh9HLtHIQ.AxY8DCtDaGlsbGljb3RoZQ.KDlTtXchhZTGufMYmOYGS4HffxPSUrfmqCHXaI9wOGY.Ij5PCOS8FKQNFKADfNAgHQ";
		byte[] symmetricKey = "12345678901234567890123456789012".getBytes();
		
		String expected = "Live long and prosper.";

		JweSerializer serializer = new JweSerializer(encryptionData, symmetricKey);
		String actual = serializer.compactDeserialization();

		assertThat(actual, is(expected));
	}
}
