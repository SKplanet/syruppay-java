/*
 * Copyright (c) 2015 SK PLANET. ALL Rights Reserved.
 *
 * Syrup Pay Client Library
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

package com.skplanet.syruppay.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skplanet.syruppay.client.event.ApproveEvent;
import com.skplanet.syruppay.client.mocks.ApproveMocks;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class SyrupPayClientTest {
    private ObjectMapper objectMapper = new ObjectMapper();
    SyrupPayClient syrupPayClient;

    @Before
    public void setUp() throws Exception {
        syrupPayClient = new SyrupPayClient();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBasicAuthentication_중복설정() throws Exception {
        // Give
        syrupPayClient.basicAuthentication("hmall001", "RfezFtrZneqNsJWEQbdFflhoxdBbKYnp");

        // When
        syrupPayClient.basicAuthentication("hmall001", "RfezFtrZneqNsJWEQbdFflhoxdBbKYnp");

        // Then
    }

    @Test
    public void testApprove_널_요청() throws Exception {
        // Give
        syrupPayClient.basicAuthentication("hmall001", "RfezFtrZneqNsJWEQbdFflhoxdBbKYnp");

        // When
        ApproveEvent.ResponseApprove re = syrupPayClient.approve(new URI("https://private-api-devpay.syrup.co.kr/v1/api-basic/payment/approval"), null);

        //Then
        assertThat(re, is(notNullValue()));
        assertThat(re.getSyrupPayError(), is(notNullValue()));
        assertThat(re.getSyrupPayError().getHttpStatus(), is("BAD_REQUEST"));
    }

    @Test
    public void testApprove() throws Exception {
        // Give
        syrupPayClient.basicAuthentication("hmall001", "RfezFtrZneqNsJWEQbdFflhoxdBbKYnp");

        // When
        ApproveEvent.ResponseApprove re = syrupPayClient.approve(new URI("https://private-api-devpay.syrup.co.kr/v1/api-basic/payment/approval"), ApproveMocks.getRequestApprove());

        //Then
        assertThat(re, is(notNullValue()));
        assertThat(re.getSyrupPayError(), is(notNullValue()));
        assertThat(re.getSyrupPayError().getHttpStatus(), is("BAD_REQUEST"));
    }
}