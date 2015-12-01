/*
 * Syrup Pay Token Library
 *
 * Copyright (C) 2015 SK PLANET. ALL Rights Reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the SK PLANET., Bundang-gu, 264,
 * Pangyo-ro The Planet SK planet co., Ltd., Seongnam-si, Gyeonggi-do, Korea
 * or see https://www.syruppay.co.kr/
 */

package com.skplanet.syruppay.client;

import com.skplanet.syruppay.client.event.ApproveEvent;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author 임형태
 * @since 0.1
 */
public class SyrupPayClient {
    public static SSLContext ssl() {
        System.setProperty("jsse.enableSNIExtension", "false");
        TrustManager[] certs = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkServerTrusted(X509Certificate[] chain, String authType)
                            throws CertificateException {
                    }

                    public void checkClientTrusted(X509Certificate[] chain, String authType)
                            throws CertificateException {
                    }
                }
        };
        SSLContext ctx = null;
        try {
            ctx = SSLContext.getInstance("TLSv1.2");
            ctx.init(null, certs, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(ctx.getSocketFactory());
        } catch (java.security.GeneralSecurityException ex) {
        }
        return ctx;
    }

    public ApproveEvent.ResponseApprove approve(ApproveEvent.RequestApprove requestApprove) {
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("hmall001", "RfezFtrZneqNsJWEQbdFflhoxdBbKYnp");
        Client client = ClientBuilder.newBuilder().sslContext(ssl()).build();
        return client.target("https://private-api-devpay.syrup.co.kr/v1/api-basic/payment/approval")
                .register(feature)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(requestApprove), ApproveEvent.ResponseApprove.class);
    }
}
