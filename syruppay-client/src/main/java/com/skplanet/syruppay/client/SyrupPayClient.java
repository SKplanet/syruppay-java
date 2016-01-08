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
import com.skplanet.jose.Jose;
import com.skplanet.jose.JoseBuilders;
import com.skplanet.syruppay.client.event.ApproveEvent;
import com.skplanet.syruppay.client.event.CancelEvent;
import com.skplanet.syruppay.client.event.GetSsoCredentialEvent;
import com.skplanet.syruppay.client.event.RefundEvent;
import com.skplanet.syruppay.client.message.JweMessageBodyProvider;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * 시럽페이 S2S 요청에 대한 기능을 수행한다.
 *
 * @author 임형태
 * @since 0.1
 */
public final class SyrupPayClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(SyrupPayClient.class.getName());
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final SyrupPayEnvironment syrupPayEnvironment;

    /**
     * 시럽페이 S2S 요청을 위한 클라이언트를 생성한다.
     *
     * @param syrupPayEnvironment
     *         {@link com.skplanet.syruppay.client.SyrupPayEnvironment} 시럽페이 인프라에 대한 환경을 정의한 객체
     */
    public SyrupPayClient(final SyrupPayEnvironment syrupPayEnvironment) {
        this.syrupPayEnvironment = syrupPayEnvironment;
    }

    private HttpAuthenticationFeature feature;
    private String merchantId;
    private String merchantSecret;
    private JweMessageBodyProvider jweMessageBodyProvider;

    private MediaType accept = JOSE;
    private MediaType contentType = JOSE;
    public static MediaType JOSE = new MediaType("application", "jose");

    /**
     * Http Accept Type을 지정한다.
     * 시럽페이 S2S 클라이언트는 1개의 Accept Type 만 설정이 가능하며 Default 는 {@link MediaType#APPLICATION_JSON_TYPE}으로 정의되어 있다.
     *
     * @param accept
     *         the accept
     */
    public void setAccept(final MediaType accept) {
        this.accept = accept;
    }

    /**
     * Http Content-Type을 지정한다.
     * Default 는 {@link MediaType#APPLICATION_JSON_TYPE}으로 정의되어 있다.
     *
     * @param contentType
     *         the content type
     */
    public void setContentType(final MediaType contentType) {
        this.contentType = contentType;
    }

    /**
     * Http Basic Authorization Header 를 이용한 인증을 설정한다.
     *
     * @param merchantId
     *         the client id
     * @param merchantSecret
     *         the client secret
     */
    public void basicAuthentication(final String merchantId, final String merchantSecret) {
        if (feature != null) {
            throw new IllegalArgumentException("already set information to authenticate with http basic. I recommend that you should user new object, because this object has status value.");
        }
        this.merchantId = merchantId;
        this.merchantSecret = merchantSecret;
        feature = HttpAuthenticationFeature.basic(merchantId, merchantSecret);
    }

    /**
     * 전송 메시지에 대한 JWE 암호화를 위한 설정을 추가한다.
     *
     * @param iss
     *         JWE의 ISS
     * @param secret
     *         JWE 암호화를 위한 Secret Key
     */
    public void useJweWhileCommunicating(String iss, String secret) {
        if (jweMessageBodyProvider != null) {
            throw new IllegalArgumentException("already set information to convert message in body. I recommend that you should user new object, because this object has status value.");
        }
        jweMessageBodyProvider = new JweMessageBodyProvider(iss, secret);
    }

    private SSLContext sslContext() {
        System.setProperty("jsse.enableSNIExtension", "false");
        final TrustManager[] certs = new TrustManager[]{
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
        } catch (GeneralSecurityException ignored) {
        }
        return ctx;
    }

    /**
     * 시럽 페이 서버로 거래 승인을 요청한다.
     *
     * @param requestApprove
     *         {@link com.skplanet.syruppay.client.event.ApproveEvent.RequestApprove} 객체
     * @return {@link com.skplanet.syruppay.client.event.ApproveEvent.ResponseApprove}
     * @throws IOException
     *         the iO exception
     */
    public ApproveEvent.ResponseApprove approve(final ApproveEvent.RequestApprove requestApprove) throws IOException {
        return call(syrupPayEnvironment.getTransactionServerUrl() + "/v1/api-basic/payment/approval", requestApprove, ApproveEvent.ResponseApprove.class);
    }

    /**
     * 시럽페이 서버로 가맹점 사용자의 SSO 가 존재하는지 여부를 확인한다.
     *
     * @param requestGettingSso
     *         {@link com.skplanet.syruppay.client.event.GetSsoCredentialEvent.RequestGettingSso} 객체
     * @return {@link GetSsoCredentialEvent.ResponseGettingSso}
     * @throws IOException
     *         the iO exception
     */
    public GetSsoCredentialEvent.ResponseGettingSso getSso(final GetSsoCredentialEvent.RequestGettingSso requestGettingSso) throws IOException {
        requestGettingSso.setIss(merchantId);
        return call(syrupPayEnvironment.getAuthenticationServerUrl() + "/v1/api-basic/merchants/" + merchantId + "/sso-credentials/create", requestGettingSso, GetSsoCredentialEvent.ResponseGettingSso.class);
    }

    /**
     * 시럽페이 서버로 정상 승인에 대하여 거래 취소를 요청합니다.
     *
     * @param requestRefund
     *         {@link com.skplanet.syruppay.client.event.RefundEvent.RequestRefund} 객체
     * @return {@link com.skplanet.syruppay.client.event.RefundEvent.ResponseRefund}
     * @throws IOException
     *         the iO exception
     */
    public RefundEvent.ResponseRefund refund(final RefundEvent.RequestRefund requestRefund) throws IOException {
        return call(syrupPayEnvironment.getTransactionServerUrl() + "/v1/api-basic/payment/refund", requestRefund, RefundEvent.ResponseRefund.class);
    }

    /**
     * 가맹점에서 예외상황 발생 시 거래 취소를 요청합니다.
     * 이 API는 Syrup Pay로부터 정상 거래승인을 받았으나 내부 오류로 정상 처리를 하지 못할 경우 사용합니다. (망 취소)
     *
     * @param requestCancel
     *         {@link com.skplanet.syruppay.client.event.CancelEvent.RequestCancel} 객체
     * @return {@link com.skplanet.syruppay.client.event.CancelEvent.ResponseCancel}
     * @throws IOException
     *         the iO exception
     */
    public RefundEvent.ResponseRefund cancel(final CancelEvent.RequestCancel requestCancel) throws IOException {
        return call(syrupPayEnvironment.getTransactionServerUrl() + "/v1/api-basic/payment/cancel/exception", requestCancel, CancelEvent.ResponseCancel.class);
    }

    private <R> R call(String urlOfResource, Object request, Class<R> r) throws IOException {
        try {
            return client(urlOfResource.startsWith("https")).target(urlOfResource)
                    .request(contentType)
                    .accept(accept)
                    .post((contentType.equals(MediaType.APPLICATION_JSON_TYPE) ? Entity.json(request) : Entity.entity(request, contentType)), r);
        } catch (ClientErrorException e) {
            LOGGER.warn("{} excepted while get sso request: {}, has a entity: {}", e.getMessage(), request, e.getResponse().hasEntity());
            R re = getObjectWithErrorMessageIfExistNullReturn(e, r);
            if (re != null) {
                return re;
            }
            throw e;
        }
    }

    private <T> T getObjectWithErrorMessageIfExistNullReturn(ClientErrorException e, Class<T> clz) throws IOException {
        if (e.getResponse().hasEntity()) {
            if (e.getResponse().getMediaType().getSubtype().equals("jose")) {
                return OBJECT_MAPPER.readValue(
                        new Jose().configuration(JoseBuilders.compactDeserializationBuilder()
                                .serializedSource(getMessageBody((InputStream) (e.getResponse().getEntity())))
                                .key(merchantSecret))
                                .deserialization()
                        , clz);
            } else {
                return OBJECT_MAPPER.readValue((InputStream) (e.getResponse().getEntity()), clz);
            }
        }
        return null;
    }

    private String getMessageBody(InputStream entityStream) throws IOException {
        final char[] buffer = new char[1024];
        final StringBuilder out = new StringBuilder();
        Reader in = null;
        try {
            in = new InputStreamReader(entityStream, "UTF-8");
            for (; ; ) {
                int rsz = in.read(buffer, 0, buffer.length);
                if (rsz < 0)
                    break;
                out.append(buffer, 0, rsz);
            }
            in.close();
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return out.toString();
    }

    private Client client(final boolean useSsl) {
        final ClientBuilder cb = ClientBuilder.newBuilder();
        if (useSsl) {
            cb.sslContext(sslContext());
        }
        final Client c = cb.build();
        if (feature != null) {
            c.register(feature);
        }
        if (jweMessageBodyProvider != null) {
            c.register(jweMessageBodyProvider);
        }

        return c;
    }
}
