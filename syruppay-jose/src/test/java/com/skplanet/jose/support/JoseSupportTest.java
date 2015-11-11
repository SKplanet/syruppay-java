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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigInteger;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import com.skplanet.jose.jwa.crypto.CryptoUtils;
import com.skplanet.jose.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;

public class JoseSupportTest {
	private String plainText = "Live long and prosper.";
	private String kid = "test";

	byte[] pubKey = null;
	byte[] priKey = null;

	RSAPublicKey publicKey = null;
	RSAPrivateKey privateKey = null;

	String symmetrickey = "GawgguFyGrWKav7AX4VKUg";
	byte[] symmetricKeyBytes = Base64.decodeBase64(symmetrickey);

	@Before
	public void setUp() throws Exception {
		String n = "sXchDaQebHnPiGvyDOAT4saGEUetSyo9MKLOoWFsueri23bOdgWp4Dy1Wl"
				+"UzewbgBHod5pcM9H95GQRV3JDXboIRROSBigeC5yjU1hGzHHyXss8UDpre"
				+"cbAYxknTcQkhslANGRUZmdTOQ5qTRsLAt6BTYuyvVRdhS8exSZEy_c4gs_"
				+"7svlJJQ4H9_NxsiIoLwAEk7-Q3UXERGYw_75IDrGA84-lA_-Ct4eTlXHBI"
				+"Y2EaV7t7LjJaynVJCpkv4LKjTTAumiGUIuQhrNhZLuF_RJLqHpM2kgWFLU"
				+"7-VTdL1VbC2tejvcI2BlMkEpk1BzBZI0KQB0GaDWFLN-aEAw3vRw";
				
		String e = "AQAB";
				
		String d = "VFCWOqXr8nvZNyaaJLXdnNPXZKRaWCjkU5Q2egQQpTBMwhprMzWzpR8Sxq"
				+"1OPThh_J6MUD8Z35wky9b8eEO0pwNS8xlh1lOFRRBoNqDIKVOku0aZb-ry"
				+"nq8cxjDTLZQ6Fz7jSjR1Klop-YKaUHc9GsEofQqYruPhzSA-QgajZGPbE_"
				+"0ZaVDJHfyd7UUBUKunFMScbflYAAOYJqVIVwaYR5zWEEceUjNnTNo_CVSj"
				+"-VvXLO5VZfCUAVLgW4dpf1SrtZjSt34YLsRarSb127reG_DUwg9Ch-Kyvj"
				+"T1SkHgUWRVGcyly7uvVGRSDwsXypdrNinPA4jlhoNdizK2zF2CWQ";

		BigInteger bn = new BigInteger(1, Base64.decodeBase64(n));
		BigInteger be = new BigInteger(1, Base64.decodeBase64(e));
		BigInteger bd = new BigInteger(1, Base64.decodeBase64(d));

		publicKey = CryptoUtils.generateRsaPublicKey(bn, be);
		privateKey = CryptoUtils.generateRsaPrivateKey(bn, bd);

		pubKey = publicKey.getEncoded();
		priKey = privateKey.getEncoded();
	}

	@Test
	public void testBuild_a128kwNoPutCekAndIv() {
		byte[] symmetricKey = Base64.decodeBase64("GawgguFyGrWKav7AX4VKUg");

		String jweContent = JoseSupport.build_a128kw(null, symmetricKey, plainText);
		String verifyContent = JoseSupport.verify_a128kw(symmetricKey, jweContent);

		assertThat(verifyContent, is(plainText));
	}

	@Test
	public void testBuild_a128kwWithPucCekAndIv() {
		byte[] cek = { (byte) 4, (byte) 211, (byte) 31, (byte) 197, (byte) 84, (byte) 157, (byte) 252,
				(byte) 254, (byte) 11, (byte) 100, (byte) 157, (byte) 250, (byte) 63, (byte) 170, (byte) 106,
				(byte) 206, (byte) 107, (byte) 124, (byte) 212, (byte) 45, (byte) 111, (byte) 107, (byte) 9,
				(byte) 219, (byte) 200, (byte) 177, (byte) 0, (byte) 240, (byte) 143, (byte) 156, (byte) 44, (byte) 207 };

		byte[] symmetricKey = Base64.decodeBase64("GawgguFyGrWKav7AX4VKUg");

		byte[] iv = { (byte) 3, (byte) 22, (byte) 60, (byte) 12, (byte) 43, (byte) 67, (byte) 104, (byte) 105,
				(byte) 108, (byte) 108, (byte) 105, (byte) 99, (byte) 111, (byte) 116, (byte) 104, (byte) 101 };

		String jweContent = JoseSupport.build_a128kw(null, symmetricKey, plainText, cek, iv);

		String expected = "eyJhbGciOiJBMTI4S1ciLCJlbmMiOiJBMTI4Q0JDLUhTMjU2In0.6KB707dM9YTIgHtLvtgWQ8mKwboJW3of9locizkDTHzBC2IlrT1oOQ.AxY8DCtDaGlsbGljb3RoZQ.KDlTtXchhZTGufMYmOYGS4HffxPSUrfmqCHXaI9wOGY.U0m_YmjN04DJvceFICbCVQ";
		assertThat(jweContent, is(expected));
	}

	@Test
	public void testVerifyKid_a128kw() {
		byte[] symmetricKey = Base64.decodeBase64("GawgguFyGrWKav7AX4VKUg");
		String jweContent = JoseSupport.build_a128kw(this.kid, symmetricKey, plainText);

		String kid = JoseSupport.verify_a128kwHeader(jweContent);
		assertThat(kid, is(this.kid));
	}

	@Test
	public void testVerify_a128kw() {
		String encryptionData = "eyJhbGciOiJBMTI4S1ciLCJlbmMiOiJBMTI4Q0JDLUhTMjU2In0.6KB707dM9YTIgHtLvtgWQ8mKwboJW3of9locizkDTHzBC2IlrT1oOQ.AxY8DCtDaGlsbGljb3RoZQ.KDlTtXchhZTGufMYmOYGS4HffxPSUrfmqCHXaI9wOGY.U0m_YmjN04DJvceFICbCVQ";
		byte[] symmetricKey = Base64.decodeBase64("GawgguFyGrWKav7AX4VKUg");
		
		String actual = JoseSupport.verify_a128kw(symmetricKey, encryptionData);
		
		assertThat(actual, is(plainText));
	}

	@Test
	public void testBuild_rsa15_1() {
		String jweContent = JoseSupport.build_rsa15(this.kid, pubKey, plainText);
		String[] split = jweContent.split("\\.");
	    assertThat(split.length, is(5));
	}
	
	@Test
	public void testVerify_ras15Header() {
		String jweContent = "eyJhbGciOiJSU0ExXzUiLCJraWQiOiJ0ZXN0IiwiZW5jIjoiQTEyOENCQy1IUzI1NiJ9.JoNBUONAxwr6y9yIAyO8CaPKBB_HTqThNFjtlO61zpvezSmecPO2kEZ80lsImEh6OGQPqDsqUZiMvlYWmYUBFfSGE4HAgZdV3tD0b-z9UuxXPuOG4WAgFe0UXyfMGYEaAOheQfV7xpPpvlzgIXSBYC46g3gUIljC6l6-17C0XYf5BtyXxEco8676qWg78QHIQNtdziy-F2AB70LWkvx_gHKcGbPh_cqJo8nLSigsCjm2drRgpsXE5EuDXC0A6AQX1yWfUAMhSkf6usDZ0s9eUOtvqYIoVKIYmUTaQ7--YfGIkjOOh6weL4dN7-4KtXMiGMRYdZY5CJk-szJBEYl2Mw.w8Za8wynu7itBYiE1oe4tQ.ttRai683I_H_wzkzym301oFzFvw0-8HnBhiJAoG3GwY._DXGFUYCtjZH6_Ck-akeiA";
		
		String kid = JoseSupport.verify_ras15Header(jweContent);
		assertThat(kid, is("test"));
	}
	
	@Test
	public void testVerify_rsa15() {
		String jweContent = "eyJhbGciOiJSU0ExXzUiLCJlbmMiOiJBMTI4Q0JDLUhTMjU2In0.lYNTuc5qE2piY8_NbkaD9741Zvv8SHZjtF2Z1cUPBFh6ocUDBUNTdLjFe4T-xxLASDqjxTEcxrNWvrDvt-2ISf065tAmUxcKcRGAwxY8Jt6HIoy5RrXaacXzJm2jlAhg2EZl7Z4PbR8LMUyHH4kiDyLYdQhoZjNt10n919CUVDz14NsanxZaT9I0Fg2t8-HuvKSNAFWGN7pSWJVD_jcEtTTLlsZR-zilV1vaY-DVaTayEUV1-NT_7NfsKlrGPCifZI3s9XPr6oEReBqIyaXwE0nV4P1oI4RGBYbwO685qzkd_9I6F9Aw16OuND5ErI1t5ZvPfyeM_7u7F5aOaKzccA.AxY8DCtDaGlsbGljb3RoZQ.KDlTtXchhZTGufMYmOYGS4HffxPSUrfmqCHXaI9wOGY.9hH0vgRfYgPnAHOd8stkvw";
		
		String actual = JoseSupport.verify_rsa15(priKey, jweContent);

		assertThat(actual, is(plainText));
	}

	@Test
	public void testBuild_rsaoaep() throws Exception {
		String jweContent = JoseSupport.build_rsaoaep(this.kid, pubKey, plainText);

		String actual = JoseSupport.verify_rsaoaep(priKey, jweContent);

		assertThat(actual, is(plainText));
	}

	@Test
	public void testVerify_rasoaepHeader() throws Exception {
		String jweContent = JoseSupport.build_rsaoaep(this.kid, pubKey, plainText);

		String actual = JoseSupport.verify_rasoaepHeader(jweContent);

		assertThat(actual, is(kid));
	}

	@Test
	public void testVerify_rsaoaep() throws Exception {

	}

	@Test
	public void testJws_RsaSign() throws Exception {
		String jws = JoseSupport.Jws_RsaSign(plainText, privateKey);
		String actual = JoseSupport.Jws_RsaVerify(jws, publicKey);

		assertThat(actual, is(plainText));
	}

	@Test
	public void testJws_HmacSign() throws Exception {
		String jws = JoseSupport.Jws_HmacSign(plainText, symmetrickey);
		String actual = JoseSupport.Jws_HmacVerify(jws, symmetrickey);

		assertThat(actual, is(plainText));
	}
}
