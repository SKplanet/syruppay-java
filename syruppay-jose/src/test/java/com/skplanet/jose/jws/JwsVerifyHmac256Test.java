package com.skplanet.jose.jws;

import org.junit.Before;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by 박병찬 on 2015-07-14.
 */
public class JwsVerifyHmac256Test {
	@Before public void setUp() throws Exception {
	}

	@Test public void testEncodedHeader() throws UnsupportedEncodingException {
		String expected = "{\"iss\":\"joe\",\n" + "   \"exp\":1300819380,\n" + "   \"http://example.com/is_root\":true}";
		String signing = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJqb2UiLAogICAiZXhwIjoxMzAwODE5MzgwLAogICAiaHR0cDovL2V4YW1wbGUuY29tL2lzX3Jvb3QiOnRydWV9.NnnMCS7jsU-kBIm3oJIc5xEHLGzzXLX6O2wVxlslAgo";
		String symmetircKey = "AyM1SysPpbyDfgZld3umj1qzKObwVMkoqQ-EstJQLr_T-1qS0gZH75aKtMN3Yj0iPS4hcgUuTwjAzZr1Z9CAow";

		JwsSerializer serializer = new JwsSerializer(signing, symmetircKey.getBytes());
		String actual = serializer.compactDeserialization();

		assertThat(actual, is(expected));
	}
}
