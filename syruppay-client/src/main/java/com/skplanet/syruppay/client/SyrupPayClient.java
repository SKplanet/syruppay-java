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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skplanet.syruppay.client.event.ApproveEvent;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author 임형태
 * @since 0.1
 */
public final class SyrupPayClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(SyrupPayClient.class.getName());
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private HttpAuthenticationFeature feature;
    private MediaType accept = MediaType.APPLICATION_JSON_TYPE;
    private MediaType contentType = MediaType.APPLICATION_JSON_TYPE;

    public void setAccept(MediaType accept) {
        this.accept = accept;
    }

    public void setContentType(MediaType contentType) {
        this.contentType = contentType;
    }

    public void basicAuthentication(final String clientId, final String clientSecret) {
        if (feature != null) {
            throw new IllegalArgumentException("already set information to authenticate with http basic. you should user new object, because this object has status value.");
        }
        feature = HttpAuthenticationFeature.basic(clientId, clientSecret);
    }

    public static SSLContext sslContext() {
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

    public ApproveEvent.ResponseApprove approve(final URI resource, final ApproveEvent.RequestApprove requestApprove) throws IOException {
        try {
            return client(resource).target(resource)
                    .request(contentType)
                    .accept(accept)
                    .post(Entity.json(requestApprove), ApproveEvent.ResponseApprove.class);
        } catch (ClientErrorException e) {
            if (e.getResponse().hasEntity()) {
                return OBJECT_MAPPER.readValue((InputStream) e.getResponse().getEntity(), ApproveEvent.ResponseApprove.class);
            }
            LOGGER.warn("", e.getResponse().getEntity());
            throw e;
        }
    }

    private Client client(final URI resource) {
        final ClientBuilder cb = ClientBuilder.newBuilder();
        if (resource.getScheme().startsWith("https")) {
            cb.sslContext(sslContext());
        }
        final Client c = cb.build();
        if (feature != null) {
            c.register(feature);
        }
        return c;
    }
}
