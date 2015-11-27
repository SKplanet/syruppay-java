package com.skplanet.jose;

import com.skplanet.jose.jwa.Jwa;
import junit.framework.TestCase;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by byeongchan.park@sk.com(1000808) on 2015-11-27.
 */
public class JoseHeaderTest extends TestCase {

	public void setUp() throws Exception {
		super.setUp();
	}

	@Test public void testJoseHeaderEncode() throws Exception {
		JoseHeader header = new JoseHeader(Jwa.A128KW, Jwa.A128CBC_HS256);
		String joseHeader = header.getEncoded();

		assertThat(joseHeader, is("eyJhbGciOiJBMTI4S1ciLCJlbmMiOiJBMTI4Q0JDLUhTMjU2In0"));
	}

	@Test public void testDecodeJoseHeader() {
		String header = "eyJhbGciOiJBMTI4S1ciLCJlbmMiOiJBMTI4Q0JDLUhTMjU2In0";

		JoseHeader joseHeader = new JoseHeader();
		joseHeader.setEncoded(header);

		assertThat(joseHeader.getAlgorithm().getValue(), is(Jwa.A128KW.getValue()));
		assertThat(joseHeader.getEncrytion().getValue(), is(Jwa.A128CBC_HS256.getValue()));
	}
}
