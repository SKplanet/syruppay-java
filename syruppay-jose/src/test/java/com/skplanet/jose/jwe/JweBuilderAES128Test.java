package com.skplanet.jose.jwe;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.UnsupportedEncodingException;

import com.skplanet.jose.JoseHeader;
import com.skplanet.jose.jwa.alg.AesKeyWrapAlgorithm;
import com.skplanet.jose.jwa.alg.JweAlgResult;
import com.skplanet.jose.jwa.enc.AesEncryptionWithHmacSha;
import com.skplanet.jose.commons.codec.binary.Base64;
import com.skplanet.jose.jwa.enc.ContentEncryptKeyGenerator;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.skplanet.jose.jwa.JweAlgorithm;
import com.skplanet.jose.jwa.Jwa;

public class JweBuilderAES128Test {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void TestJoseHeaderAndEncode() throws UnsupportedEncodingException {
		JoseHeader header = new JoseHeader(Jwa.A128KW, Jwa.A128CBC_HS256);
		String joseHeader = header.getEncoded();

		assertThat(joseHeader, is("eyJhbGciOiJBMTI4S1ciLCJlbmMiOiJBMTI4Q0JDLUhTMjU2In0"));
	}
	
	@Test
	public void TestContentEncryptionKey() {
		JweAlgorithm jweAlgorithm = new AesKeyWrapAlgorithm(16);
		
		byte[] cek = { (byte) 4, (byte) 211, (byte) 31, (byte) 197, (byte) 84, (byte) 157, (byte) 252,
				(byte) 254, (byte) 11, (byte) 100, (byte) 157, (byte) 250, (byte) 63, (byte) 170, (byte) 106,
				(byte) 206, (byte) 107, (byte) 124, (byte) 212, (byte) 45, (byte) 111, (byte) 107, (byte) 9,
				(byte) 219, (byte) 200, (byte) 177, (byte) 0, (byte) 240, (byte) 143, (byte) 156, (byte) 44, (byte) 207 };
		ContentEncryptKeyGenerator cekGenerator = new ContentEncryptKeyGenerator(32);
		cekGenerator.setUserEncryptionKey(cek);

		byte[] symmetricKey = Base64.decodeBase64("GawgguFyGrWKav7AX4VKUg");

		JweAlgResult jweAlgResult = jweAlgorithm.encryption(symmetricKey, cekGenerator);
		byte[] actual = jweAlgResult.getEncryptedCek();

		assertThat(Base64.encodeBase64URLSafeString(actual), is("6KB707dM9YTIgHtLvtgWQ8mKwboJW3of9locizkDTHzBC2IlrT1oOQ"));
	}
	
	@Test
	public void TestGetContentEncryption() {
		AesEncryptionWithHmacSha jweEncryption = new AesEncryptionWithHmacSha(32, 16);

		byte[] raw = { (byte) 76, (byte) 105, (byte) 118, (byte) 101, (byte) 32, (byte) 108, (byte) 111, (byte) 110,
				(byte) 103, (byte) 32, (byte) 97, (byte) 110, (byte) 100, (byte) 32, (byte) 112, (byte) 114,
				(byte) 111, (byte) 115, (byte) 112, (byte) 101, (byte) 114, (byte) 46 };

		byte[] secret = { (byte) 4, (byte) 211, (byte) 31, (byte) 197, (byte) 84, (byte) 157, (byte) 252, (byte) 254,
				(byte) 11, (byte) 100, (byte) 157, (byte) 250, (byte) 63, (byte) 170, (byte) 106, (byte) 206,
				(byte) 107, (byte) 124, (byte) 212, (byte) 45, (byte) 111, (byte) 107, (byte) 9, (byte) 219,
				(byte) 200, (byte) 177, (byte) 0, (byte) 240, (byte) 143, (byte) 156, (byte) 44, (byte) 207 };

		byte[] iv = { (byte) 3, (byte) 22, (byte) 60, (byte) 12, (byte) 43, (byte) 67, (byte) 104, (byte) 105,
				(byte) 108, (byte) 108, (byte) 105, (byte) 99, (byte) 111, (byte) 116, (byte) 104, (byte) 101 };

		byte[] key = new byte[16];
		System.arraycopy(secret, 16, key, 0, 16);

//		byte[] b = jweEncryption.encryption(key, iv, raw);

//		assertThat(Base64.encodeBase64URLSafeString(b), is("KDlTtXchhZTGufMYmOYGS4HffxPSUrfmqCHXaI9wOGY"));
	}

	@Test
	public void TestBuild() {
		byte[] cek = { (byte) 4, (byte) 211, (byte) 31, (byte) 197, (byte) 84, (byte) 157, (byte) 252,
				(byte) 254, (byte) 11, (byte) 100, (byte) 157, (byte) 250, (byte) 63, (byte) 170, (byte) 106,
				(byte) 206, (byte) 107, (byte) 124, (byte) 212, (byte) 45, (byte) 111, (byte) 107, (byte) 9,
				(byte) 219, (byte) 200, (byte) 177, (byte) 0, (byte) 240, (byte) 143, (byte) 156, (byte) 44, (byte) 207 };

		byte[] symmetricKey = Base64.decodeBase64("GawgguFyGrWKav7AX4VKUg");

		byte[] iv = { (byte) 3, (byte) 22, (byte) 60, (byte) 12, (byte) 43, (byte) 67, (byte) 104, (byte) 105,
				(byte) 108, (byte) 108, (byte) 105, (byte) 99, (byte) 111, (byte) 116, (byte) 104, (byte) 101 };

		byte[] raw = { (byte) 76, (byte) 105, (byte) 118, (byte) 101, (byte) 32, (byte) 108, (byte) 111, (byte) 110,
				(byte) 103, (byte) 32, (byte) 97, (byte) 110, (byte) 100, (byte) 32, (byte) 112, (byte) 114,
				(byte) 111, (byte) 115, (byte) 112, (byte) 101, (byte) 114, (byte) 46 };

		String expected = "eyJhbGciOiJBMTI4S1ciLCJlbmMiOiJBMTI4Q0JDLUhTMjU2In0.6KB707dM9YTIgHtLvtgWQ8mKwboJW3of9locizkDTHzBC2IlrT1oOQ.AxY8DCtDaGlsbGljb3RoZQ.KDlTtXchhZTGufMYmOYGS4HffxPSUrfmqCHXaI9wOGY.U0m_YmjN04DJvceFICbCVQ";

		JweSerializer serializer = new JweSerializer(
				new JoseHeader(Jwa.A128KW, Jwa.A128CBC_HS256),
				new String(raw),
				symmetricKey);
		serializer.setUserEncryptionKey(cek, iv);
		String result = serializer.compactSerialization();
		
		assertThat(result, is(expected));
	}
}
