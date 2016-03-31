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

import com.skplanet.syruppay.client.event.ApproveEvent;
import com.skplanet.syruppay.client.event.GetSsoCredentialEvent;
import com.skplanet.syruppay.client.mocks.ApproveMocks;
import com.skplanet.syruppay.client.mocks.GetSsoMocks;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.util.encoders.Base64Encoder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class SyrupPayClientTest {
    SyrupPayClient syrupPayClient;

    @Before
    public void setUp() throws Exception {
        syrupPayClient = new SyrupPayClient(SyrupPayEnvironment.DEVELOPMENT);
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
        ApproveEvent.ResponseApprove re = syrupPayClient.approve(null);

        //Then
        assertThat(re, is(notNullValue()));
        assertThat(re.getSyrupPayError(), is(notNullValue()));
        assertThat(re.getSyrupPayError().getHttpStatus(), is("BAD_REQUEST"));
    }

    @Test
    public void testApprove() throws Exception {
        // Give
        syrupPayClient.basicAuthentication("hmall001", "RfezFtrZneqNsJWEQbdFflhoxdBbKYnp");
        syrupPayClient.setContentType(SyrupPayClient.JOSE);
        syrupPayClient.setAccept(SyrupPayClient.JOSE);
        syrupPayClient.useJweWhileCommunicating("hmall001", "fQGZACj5upMx2vqSTxCOsctFwNQN0I9Z");

        // When
        ApproveEvent.ResponseApprove re = syrupPayClient.approve(ApproveMocks.getRequestApprove());

        //Then
        assertThat(re, is(notNullValue()));
        assertThat(re.getSyrupPayError(), is(notNullValue()));
        assertThat(re.getSyrupPayError().getHttpStatus(), is("BAD_REQUEST"));
    }

    @Test
    public void testGetSso_WITH_JSON() throws Exception {
        // Give
        syrupPayClient = new SyrupPayClient(SyrupPayEnvironment.LOCALHOST);
        syrupPayClient.basicAuthentication("hmall001", "RfezFtrZneqNsJWEQbdFflhoxdBbKYnp");
        syrupPayClient.useJweWhileCommunicating("hmall001", "fQGZACj5upMx2vqSTxCOsctFwNQN0I9Z");

        // When
        GetSsoCredentialEvent.ResponseGettingSso re = syrupPayClient.getSso(GetSsoMocks.gettingSso());

        //Then
        assertThat(re, is(notNullValue()));
        assertThat(re.getSyrupPayError(), is(notNullValue()));
        assertThat(re.getSyrupPayError().getHttpStatus(), is("BAD_REQUEST"));
    }

    @Test
    public void testGetSso_WITH_JOSE() throws Exception {
        // Give
        syrupPayClient = new SyrupPayClient(SyrupPayEnvironment.LOCALHOST);
        syrupPayClient.setContentType(SyrupPayClient.JOSE);
        syrupPayClient.setAccept(SyrupPayClient.JOSE);

        syrupPayClient.basicAuthentication("sktmall_s001", "G3aIW7hYmlTjag3FDc63OGLNWwvagVUU");
        syrupPayClient.useJweWhileCommunicating("sktmall_s001", "G3aIW7hYmlTjag3FDc63OGLNWwvagVUU");

        // When
        GetSsoCredentialEvent.ResponseGettingSso re = syrupPayClient.getSso(GetSsoMocks.gettingSso());

        //Then
        assertThat(re, is(notNullValue()));
        assertThat(re.getSyrupPayError(), is(notNullValue()));
        assertThat(re.getSyrupPayError().getHttpStatus(), is("BAD_REQUEST"));
    }

    @Test
    public void testGetSso_WITH_JOSE_조회_안되는_사용자() throws Exception {
        // Give
        syrupPayClient = new SyrupPayClient(SyrupPayEnvironment.LOCALHOST);
        syrupPayClient.setContentType(SyrupPayClient.JOSE);
        syrupPayClient.setAccept(SyrupPayClient.JOSE);

        syrupPayClient.basicAuthentication("sktmall_s001", "G3aIW7hYmlTjag3FDc63OGLNWwvagVUU");
        syrupPayClient.useJweWhileCommunicating("sktmall_s001", "G3aIW7hYmlTjag3FDc63OGLNWwvagVUU");

        // When
        GetSsoCredentialEvent.ResponseGettingSso re = syrupPayClient.getSso(GetSsoMocks.notGettingSso());

        //Then
        assertThat(re, is(notNullValue()));
        assertThat(re.getSyrupPayError(), is(notNullValue()));
        assertThat(re.getSyrupPayError().getHttpStatus(), is("NOT_FOUND"));
    }

    @Test
    public void testGet_Sso_WITH_BASIC_AUTHENTICATION() throws IOException {
        URL url = new URL("http://ip:port/login");
        String encoding = Base64.encodeBase64String("id:password".getBytes());

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Authorization", "Basic " + encoding);
        InputStream content = connection.getInputStream();
        BufferedReader in =
                new BufferedReader(new InputStreamReader(content));
        String line;
        while ((line = in.readLine()) != null) {
            System.out.println(line);
        }
    }

    @Test
    public void testForReadme() throws IOException {
        // 접속 환경 설정
        SyrupPayClient syrupPayClient = new SyrupPayClient(SyrupPayEnvironment.DEVELOPMENT);
        syrupPayClient.basicAuthentication("가맹점 ID", "가맹점 Basic Authentication Key");
        syrupPayClient.useJweWhileCommunicating("가맹점 ID", "가맹점 Secret");

        ApproveEvent.RequestApprove request = new ApproveEvent.RequestApprove();
        request.setRequestIdOfMerchant("가맹점 거래 승인요청 ID");
        request.setRequestTimeOfMerchant(1448870110);
        request.setOrderIdOfMerchant("가맹점 거래인증 ID");
        request.setPaymentAmount(10000);
        request.setTaxFreeAmount(0);
        request.setOcTransAuthId("TA20151130000000000020083");
        request.setTransactionAuthenticationValue("y7we9C6TA_k-nEiYGnkeCUN8INuVCeyNJWcxbNmaKSI");

        ApproveEvent.ResponseApprove response = syrupPayClient.approve(request);

    }
}