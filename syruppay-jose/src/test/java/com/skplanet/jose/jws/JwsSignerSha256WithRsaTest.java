package com.skplanet.jose.jws;

import com.skplanet.jose.JoseHeader;
import com.skplanet.jose.commons.codec.binary.Base64;
import com.skplanet.jose.jwa.Jwa;
import com.skplanet.jose.jwa.crypto.CryptoUtils;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by 박병찬 on 2015-07-15.
 */
public class JwsSignerSha256WithRsaTest {
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testEncodeHeader() {
		String payload = "{\"iss\":\"joe\",\n" + "   \"exp\":1300819380,\n" + "   \"http://example.com/is_root\":true}";

		String n = "ofgWCuLjybRlzo0tZWJjNiuSfb4p4fAkd_wWJcyQoTbji9k0l8W26mPddx\n"
				+ "        HmfHQp-Vaw-4qPCJrcS2mJPMEzP1Pt0Bm4d4QlL-yRT-SFd2lZS-pCgNMs\n"
				+ "        D1W_YpRPEwOWvG6b32690r2jZ47soMZo9wGzjb_7OMg0LOL-bSf63kpaSH\n"
				+ "        SXndS5z5rexMdbBYUsLA9e-KXBdQOS-UTo7WTBEMa2R2CapHg665xsmtdV\n"
				+ "        MTBQY4uDZlxvb3qCo5ZwKh9kG4LT6_I5IhlJH7aGhyxXFvUK-DWNmoudF8\n"
				+ "        NAco9_h9iaGNj8q2ethFkMLs91kzk2PAcDTW9gb54h4FRWyuXpoQ";

		String e = "AQAB";

		String d = "Eq5xpGnNCivDflJsRQBXHx1hdR1k6Ulwe2JZD50LpXyWPEAeP88vLNO97I\n"
				+ "        jlA7_GQ5sLKMgvfTeXZx9SE-7YwVol2NXOoAJe46sui395IW_GO-pWJ1O0\n"
				+ "        BkTGoVEn2bKVRUCgu-GjBVaYLU6f3l9kJfFNS3E0QbVdxzubSu3Mkqzjkn\n"
				+ "        439X0M_V51gfpRLI9JYanrC4D4qAdGcopV_0ZHHzQlBjudU2QvXt4ehNYT\n"
				+ "        CBr6XCLQUShb1juUO1ZdiYoFaFQT5Tw8bGUl_x_jTj3ccPDVZFD9pIuhLh\n"
				+ "        BOneufuBiB4cS98l2SR_RQyGWSeWjnczT0QU91p1DhOVRuOopznQ";

		String expected = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJqb2UiLAogICAiZXhwIjoxMzAwODE5MzgwLAogICAiaHR0cDovL2V4YW1wbGUuY29tL2lzX3Jvb3QiOnRydWV9.NWRVJ4Piwy8m9dh077uP67Z2qEsPoa6TbI8Lz6g_ZA49uxDwWkJKpln-8t0nzY_9FFV6yVr8-gHe8N44QXbAzZIDdmFex8ukV3dIHBk4jFmDq-DDdvXo3go3vHjecLOTh6RdTb-ik5ft0JZpnoYc9APWt-aGOI3bEiUqVmwvjZ0z6F62YD12Li_BShEqNygdk_t8dbExYFU-NGuqGlNPZy1Zll1VUefitsmOcYA32JXPiZUHQ1_rZXMiJQsPS9yuiKx_0y7KG1cmccIuVvneWPboE264Cz4pgQAMd86b78RAobrh3sxc1hZokYCFuuHhImHD9wvZrl55oyQRwg7-lg";

		BigInteger bn = new BigInteger(1, Base64.decodeBase64(n));
		BigInteger be = new BigInteger(1, Base64.decodeBase64(e));
		BigInteger bd = new BigInteger(1, Base64.decodeBase64(d));

		RSAPublicKey publicKey = CryptoUtils.generateRsaPublicKey(bn, be);
		RSAPrivateKey privateKey = CryptoUtils.generateRsaPrivateKey(bn, bd);

		JoseHeader joseHeader = new JoseHeader();
		joseHeader.setHeader(JoseHeader.JoseHeaderKeySpec.TYPE, "JWT");
		joseHeader.setAlgorithm(Jwa.RS256);

		JwsSerializer jwsSerializer = new JwsSerializer(joseHeader, payload, privateKey.getEncoded());
		String token = jwsSerializer.compactSerialization();

		assertThat(token, is(expected));
	}
}
