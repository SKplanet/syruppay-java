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

package com.skplanet.syruppay.token;

import com.skplanet.jose.Jose;
import com.skplanet.jose.JoseBuilders;
import com.skplanet.jose.JoseHeader;
import com.skplanet.jose.jwa.Jwa;
import com.skplanet.syruppay.token.claims.MapToSktUserConfigurer;
import com.skplanet.syruppay.token.claims.MapToSyrupPayUserConfigurer;
import com.skplanet.syruppay.token.claims.MerchantUserConfigurer;
import com.skplanet.syruppay.token.claims.OrderConfigurer;
import com.skplanet.syruppay.token.claims.PayConfigurer;
import com.skplanet.syruppay.token.jwt.SyrupPayToken;
import com.skplanet.syruppay.token.jwt.Token;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.node.ObjectNode;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.Key;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Syrup Pay 에서 사용하는 토큰을 생성 및 암/복호화에 대한 기능을 수행한다.
 * <p>
 * 토큰은 JWT 규격을 준수하며 Claim 에 대한 확장은 {@link com.skplanet.syruppay.token.ClaimConfigurer}를 이용하여 확장할 수 있으며
 * 이에 대한 인터페이스는 {@link com.skplanet.syruppay.token.SyrupPayTokenBuilder}를 통해 {@link #pay()}와 {@link #login()}와 같이 노출해야 한다.
 *
 * @author 임형태
 * @since 1.0
 */
public final class SyrupPayTokenBuilder extends AbstractConfiguredTokenBuilder<Jwt, SyrupPayTokenBuilder> implements ClaimBuilder<Jwt>, TokenBuilder<SyrupPayTokenBuilder> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SyrupPayTokenBuilder.class.getName());

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.setVisibility(JsonMethod.FIELD, JsonAutoDetect.Visibility.ANY);
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
    }

    private String iss;
    private long nbf;
    private String sub;
    private int expiredMinutes = 10;

    /**
     * JWT 토큰에 대한 무결성을 검증(JWS)한 후 {@link com.skplanet.syruppay.token.jwt.Token} 객체를 생성하여 반환한다.
     *
     * @param token
     *         문자열의 JWT
     * @param key
     *         토큰 무결성을 검증할 키
     * @return {@link com.skplanet.syruppay.token.jwt.Token}
     * @throws java.io.IOException
     *         토큰이 유효하지 않은 경우
     */
    public static Token verify(String token, String key) throws IOException {
        return verify(token, key.getBytes());
    }

    /**
     * JWT 토큰에 대한 무결성을 검증(JWS)한 후 {@link com.skplanet.syruppay.token.jwt.Token} 객체를 생성하여 반환한다.
     *
     * @param token
     *         문자열의 JWT
     * @param key
     *         토큰 무결성을 검증할 키
     * @return {@link com.skplanet.syruppay.token.jwt.Token}
     * @throws java.io.IOException
     *         토큰이 유효하지 않은 경우
     */
    public static Token verify(String token, Key key) throws IOException {
        return verify(token, key.getEncoded());
    }

    /**
     * JWT 토큰에 대한 무결성을 검증(JWS)한 후 {@link com.skplanet.syruppay.token.jwt.Token} 객체를 생성하여 반환한다.
     *
     * @param token
     *         문자열의 JWT
     * @param key
     *         토큰 무결성을 검증할 키
     * @return {@link com.skplanet.syruppay.token.jwt.Token}
     * @throws java.io.IOException
     *         토큰이 유효하지 않은 경우
     */
    public static Token verify(String token, byte[] key) throws IOException {
        try {
            return objectMapper.readValue(new Jose().configuration(JoseBuilders.compactDeserializationBuilder().serializedSource(token).key(key)).deserialization(), SyrupPayToken.class);
        } catch (IOException e) {
            LOGGER.error("exception that decrypting token. key : {}, token : {}", new String(key), token);
            throw e;
        }
    }

    /**
     * 시럽페이 토큰 생성 주체를 설정한다.
     *
     * @param merchantId
     *         가맹점 ID
     * @return <code>this</code>
     */
    public SyrupPayTokenBuilder of(String merchantId) {
        this.iss = merchantId;
        return this;
    }

    /**
     * JWT 의 sub 에 해당하는 서브 주제를 설정한다.
     *
     * @param subject
     *         the subject
     * @return <code>this</code>
     */
    public SyrupPayTokenBuilder additionalSubject(String subject) {
        this.sub = subject;
        return this;
    }

    /**
     * JWT 의 nbf 에 해당하는 값으로 토큰이 유효한 시작 시간을 설정한다.
     *
     * @param milliseconds
     *         밀리세컨드
     * @return <code>this</code>
     */
    public SyrupPayTokenBuilder isNotValidBefore(long milliseconds) {
        this.nbf = milliseconds / 1000;
        return this;
    }

    /**
     * JWT 의 nbf 에 해당하는 값으로 토큰이 유효한 시작 시간을 설정한다.
     *
     * @param datetime
     *         문자 형식의 일자
     * @param f
     *         문자 형식
     * @return <code>this</code>
     * @throws java.text.ParseException
     *         문자형식의 일자 오류
     */
    public SyrupPayTokenBuilder isNotValidBefore(String datetime, SimpleDateFormat f) throws ParseException {
        this.nbf = f.parse(datetime).getTime() / 1000;
        return this;
    }

    /**
     * 만료 시간을 생성 시점 이후로 분단위로 입력하여 설정한다.
     *
     * @param expiredMinutes
     *         만료되는 분
     * @return <code>this</code>
     */
    public SyrupPayTokenBuilder expiredMinutes(int expiredMinutes) {
        this.expiredMinutes = expiredMinutes;
        return this;
    }

    /**
     * 시럽페이 로그인을 위한 설정 객체를 확인하여 반환한다.
     *
     * @return {@link com.skplanet.syruppay.token.claims.MerchantUserConfigurer}
     * @throws Exception
     *         the exception
     */
    public MerchantUserConfigurer<SyrupPayTokenBuilder> login() throws Exception {
        return getOrApply(new MerchantUserConfigurer<SyrupPayTokenBuilder>());
    }

    /**
     * 시럽페이 회원가입을 위한 설정 객체를 확인하여 반환한다.
     *
     * @return {@link com.skplanet.syruppay.token.claims.MerchantUserConfigurer}
     * @throws Exception
     *         the exception
     */
    public MerchantUserConfigurer<SyrupPayTokenBuilder> signUp() throws Exception {
        return getOrApply(new MerchantUserConfigurer<SyrupPayTokenBuilder>());
    }

    /**
     * 시럽페이 결제를 위한 설정 객체를 확인하여 반환한다.
     *
     * @return {@link com.skplanet.syruppay.token.claims.PayConfigurer}
     * @throws Exception
     *         the exception
     */
    public PayConfigurer<SyrupPayTokenBuilder> pay() throws Exception {
        return getOrApply(new PayConfigurer<SyrupPayTokenBuilder>());
    }

    /**
     * 시럽페이 체크아웃 기느을 위한 설정 객체를 확인하여 반환한다.
     *
     * @return {@link com.skplanet.syruppay.token.claims.OrderConfigurer}
     * @throws Exception
     *         the exception
     */
    public OrderConfigurer<SyrupPayTokenBuilder> checkout() throws Exception {
        return getOrApply(new OrderConfigurer<SyrupPayTokenBuilder>());
    }

    /**
     * 시럽페이 사용자 맵핑을 위한 설정 객체를 확인하여 반환한다.
     *
     * @return {@link com.skplanet.syruppay.token.claims.MapToSyrupPayUserConfigurer}
     * @throws Exception
     *         the exception
     */
    public MapToSyrupPayUserConfigurer<SyrupPayTokenBuilder> mapToSyrupPayUser() throws Exception {
        return getOrApply(new MapToSyrupPayUserConfigurer<SyrupPayTokenBuilder>());
    }

    /**
     * SKT 사용자인 시럽페이 사용자 맵핑을 위한 설정 객체를 확인하여 반환한다.
     *
     * @return {@link com.skplanet.syruppay.token.claims.MapToSktUserConfigurer}
     * @throws Exception
     *         the exception
     */
    public MapToSktUserConfigurer<SyrupPayTokenBuilder> mapToSktUser() throws Exception {
        return getOrApply(new MapToSktUserConfigurer<SyrupPayTokenBuilder>());
    }

    @SuppressWarnings("unchecked")
    private <C extends ClaimConfigurerAdapter<Jwt, SyrupPayTokenBuilder>> C getOrApply(C configurer)
            throws Exception {
        C existingConfig = (C) getConfigurer(configurer.getClass());
        if (existingConfig != null) {
            return existingConfig;
        }
        return apply(configurer);
    }

    protected Jwt doBuild() throws Exception {
        if (iss == null || iss.isEmpty()) {
            throw new IllegalArgumentException("issuer couldn't be null. you should set of by SyrupPayTokenBuilder#of(String of)");
        }
        Jwt c = new Jwt();
        c.setIss(iss);
        c.setIat(System.currentTimeMillis() / 1000);
        c.setExp(c.getIat() + (expiredMinutes * 60));
        c.setNbf(nbf);
        c.setSub(sub);
        return c;
    }

    /**
     * JWT(JWS) 토큰을 생성하여 반환한다.
     *
     * @param secret
     *         Signing 할 Secret
     * @return JWT(JWS)
     * @throws Exception
     *         the exception
     */
    public String generateTokenBy(String secret) throws Exception {
        return generateTokenBy(secret.getBytes());
    }

    /**
     * JWT(JWS) 토큰을 생성하여 반환한다.
     *
     * @param secret
     *         Signing 할 Secret
     * @return JWT(JWS)
     * @throws Exception
     *         the exception
     */
    public String generateTokenBy(byte[] secret) throws Exception {
        JoseHeader h = new JoseHeader(Jwa.HS256);
        h.setHeader(JoseHeader.JoseHeaderKeySpec.TYPE, "JWT");
        h.setHeader(JoseHeader.JoseHeaderKeySpec.KEY_ID, iss);
        return new Jose().configuration(JoseBuilders.JsonSignatureCompactSerializationBuilder().header(h).payload(toJson()).key(secret)).serialization();
    }

    /**
     * JWT(JWS) 토큰을 생성하여 반환한다.
     *
     * @param secret
     *         Signing 할 Secret
     * @return JWT(JWS)
     * @throws Exception
     *         the exception
     */
    public String generateTokenBy(Key secret) throws Exception {
        return generateTokenBy(secret.getEncoded());
    }

    protected String toJson() throws Exception {
        JsonNode n = objectMapper.readTree(objectMapper.writeValueAsString(build()));
        for (Class clz : getClasses()) {
            ClaimConfigurer c = getConfigurer(clz);
            c.validRequired();
            ((ObjectNode) n).putPOJO(c.claimName(), c);
        }
        return objectMapper.writeValueAsString(n);
    }
}
