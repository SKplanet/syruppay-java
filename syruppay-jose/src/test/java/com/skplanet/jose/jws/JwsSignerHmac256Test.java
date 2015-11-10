package com.skplanet.jose.jws;

import com.skplanet.jose.JoseHeader;
import com.skplanet.jose.jwa.Jwa;
import org.junit.Before;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by 박병찬 on 2015-07-14.
 */
public class JwsSignerHmac256Test {
	@Before public void setUp() throws Exception {
	}

	@Test public void testEncodedHeader() throws UnsupportedEncodingException {
		String payload = "{\"iss\":\"joe\",\n" + "   \"exp\":1300819380,\n" + "   \"http://example.com/is_root\":true}";
		String symmetircKey = "AyM1SysPpbyDfgZld3umj1qzKObwVMkoqQ-EstJQLr_T-1qS0gZH75aKtMN3Yj0iPS4hcgUuTwjAzZr1Z9CAow";

		String expected = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJqb2UiLAogICAiZXhwIjoxMzAwODE5MzgwLAogICAiaHR0cDovL2V4YW1wbGUuY29tL2lzX3Jvb3QiOnRydWV9.NnnMCS7jsU-kBIm3oJIc5xEHLGzzXLX6O2wVxlslAgo";

		JoseHeader joseHeader = new JoseHeader();
		joseHeader.setHeader(JoseHeader.JoseHeaderKeySpec.TYPE, "JWT");
		joseHeader.setAlgorithm(Jwa.HS256);

		JwsSerializer jwsSerializer = new JwsSerializer(joseHeader, payload, symmetircKey.getBytes());
		String actual = jwsSerializer.compactSerialization();
		assertThat(actual, is(expected));
	}
}
