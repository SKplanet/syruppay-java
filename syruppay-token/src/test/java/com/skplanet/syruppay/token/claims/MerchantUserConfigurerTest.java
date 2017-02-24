package com.skplanet.syruppay.token.claims;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * @author 1002225
 * @since 2016. 10. 19..
 */
public class MerchantUserConfigurerTest {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testSsoPolicy_equasl_with_null() {
        assertThat(MerchantUserClaim.SsoPolicy.NOT_APPLICABLE.equals(null), is(false));
    }

}