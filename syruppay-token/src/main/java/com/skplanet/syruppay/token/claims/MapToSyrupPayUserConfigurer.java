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

package com.skplanet.syruppay.token.claims;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skplanet.jose.Jose;
import com.skplanet.jose.JoseBuilders;
import com.skplanet.jose.JoseHeader;
import com.skplanet.jose.jwa.Jwa;
import com.skplanet.syruppay.token.TokenBuilder;

import java.io.IOException;
import java.io.Serializable;

/**
 * 시럽페이 사용자 정보 검색을 위한 Claim 을 정의한다.
 * <p>
 * 기존 시럽페이 토큰의 하위 호화선을 보장하기 위하여 맴버 변수와 메소드 명 간의 차이가 발생할 수 있으니 주의해야 한다.<br>
 * 시럽페이 사용자를 맵핑하는 방식은 {@code MappingType.CI_HASH}와 {@code MappingType.CI_MAPPED_KEY} 를 지원한다.
 * {@code MappingType.CI_HASH} 를 사용하는 경우 사용자 CI에 대한 SHA256 해쉬로 시럽페이 사용자를 검색하여 {@code MappingType.CI_MAPPED_KEY} 를 이용하는 경우 SK Planet 의 CI 인프라 자원을 활용하여 지정된 Key 값을 기준으로 시럽페이 사용자를 맵핑한다.
 *
 * @param <H> {@link com.skplanet.syruppay.token.TokenBuilder}
 * @author 임형태
 * @see com.skplanet.syruppay.token.claims.MapToSyrupPayUserConfigurer.MappingType
 * @since 1.0
 */
public class MapToSyrupPayUserConfigurer<H extends TokenBuilder<H>> extends AbstractTokenConfigurer<MapToSyrupPayUserConfigurer<H>, H> {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private MappingType mappingType;
    private String mappingValue;
    private String identityAuthenticationId;

    public String getIdentityAuthenticationId() {
        return identityAuthenticationId;
    }

    public MapToSyrupPayUserConfigurer<H> withIdentityAuthenticationId(String identityAuthenticationId) {
        this.identityAuthenticationId = identityAuthenticationId;
        return this;
    }

    /**
     * 시럽페이 사용자 정보를 맵핑하는 방식을 반환한다.
     *
     * @return {@link com.skplanet.syruppay.token.claims.MapToSyrupPayUserConfigurer.MappingType}
     * @see com.skplanet.syruppay.token.claims.MapToSyrupPayUserConfigurer.MappingType
     */
    public MappingType getMappingType() {
        return mappingType;
    }

    /**
     * 시럽페이 사용자 정보를 맵핑하기 위한 값을 반환한다.
     * 해당 값은 {@link #getMappingType()}에 의존적이므로 주의해야 한다.
     *
     * @return the mapping value
     */
    public String getMappingValue() {
        return mappingValue;
    }

    public Personal getPersonalIfNotExistThenNullWith(final String key) throws IOException {
        if (mappingType.equals(MappingType.ENCRYPTED_PERSONAL_INFO)) {
            return MAPPER.readValue(new Jose().configuration(JoseBuilders.compactDeserializationBuilder().serializedSource(mappingValue).key(key)).deserialization(), Personal.class);
        }
        return null;
    }

    /**
     * 시럽페이 사용자 정보를 맵핑하기 위한 방식을 지정한다.
     *
     * @param type {@link com.skplanet.syruppay.token.claims.MapToSyrupPayUserConfigurer.MappingType}
     * @return <code>this</code>
     */
    public MapToSyrupPayUserConfigurer<H> withType(final MappingType type) {
        this.mappingType = type;
        return this;
    }

    /**
     * 시럽페이 사용자 정보를 매핑하기 위한 값을 지정한다.
     *
     * @param value {@link com.skplanet.syruppay.token.claims.MapToSyrupPayUserConfigurer.MappingType}의 값
     * @return <code>this</code>
     */
    public MapToSyrupPayUserConfigurer<H> withValue(final String value) {
        this.mappingValue = value;
        return this;
    }

    /**
     * 개인정보가 포함된 정보를 전달하기 위한 값을 지정한다
     *
     * @param p   개인정보가 포함된 Object
     * @param kid Key ID(as 가맹점 ID)
     * @param key 암호화 하려는 키
     * @return <code>this</code>
     * @throws IOException
     * @since 1.3.8
     */
    public MapToSyrupPayUserConfigurer<H> withValue(final Personal p, final String kid, final String key) throws IOException {
        this.mappingValue = new Jose().configuration(
                JoseBuilders.JsonEncryptionCompactSerializationBuilder()
                        .header(new JoseHeader(Jwa.A128KW, Jwa.A128CBC_HS256, kid))
                        .payload(MAPPER.writeValueAsString(p))
                        .key(key)
        ).serialization();
        return this;
    }


    /**
     * JOSE 빌드 시 지정된 Claim 이름
     *
     * @return the string
     */
    public String claimName() {
        return "userInfoMapper";
    }

    public void validRequired() throws Exception {
        if (this.mappingType != null && this.mappingValue == null) {
            throw new IllegalArgumentException("fields to map couldn't be null. type : " + this.mappingType + ", value : " + this.mappingValue);
        }
    }

    /**
     * 시럽페이 사용자 정보를 맵핑하기 위한 방식을 정의한다.
     */
    public enum MappingType {
        CI_HASH, CI_MAPPED_KEY, ENCRYPTED_PERSONAL_INFO
    }

    /**
     * 가맹점으로 부터 가맹점 회원 정보를 공유 받기 위한 DTO 를 정의한다
     *
     * @since 1.3.8
     */
    public static class Personal implements Serializable  {
        private String username;
        private String lineNumber;
        private OperatorCode operatorCode;
        private String ssnFirst7Digit;
        private String email;
        private String ciHash;
        private PayableCard payableCard;

        public Personal(final String username, final String ssnFirst7Digit, final String lineNumber) throws IllegalArgumentException {
            if (username == null || ssnFirst7Digit == null || lineNumber == null) {
                throw new IllegalArgumentException(String.format("you should set with valid parameters to create this instance. username: %s, ssnFirst7Digit: %s, lineNumber: %s", username, ssnFirst7Digit, lineNumber));
            } else if (ssnFirst7Digit.length() != 7) {
                throw new IllegalArgumentException(String.format("length of ssnFirst7Digit should be 7. yours: %s (%d)", ssnFirst7Digit, ssnFirst7Digit.length()));
            }
            this.username = username;
            this.ssnFirst7Digit = ssnFirst7Digit;
            this.lineNumber = lineNumber;
        }

        public Personal() {
        }

        public String getUsername() {
            return username;
        }

        public String getSsnFirst7Digit() {
            return ssnFirst7Digit;
        }

        public String getLineNumber() {
            return lineNumber;
        }

        public Personal setUsername(final String username) {
            assert username != null && (username.length() > 0) : "username shouldn't be null and not empty.";
            this.username = username;
            return this;
        }

        public Personal setSsnFirst7Digit(final String ssnFirst7Digit) {
            assert ssnFirst7Digit != null && (ssnFirst7Digit.length() > 0) : "ssnFirst7Digit shouldn't be null and not empty.";
            assert ssnFirst7Digit.length() == 7 : String.format("length of ssnFirst7Digit should be 7. yours inputs is : %s", ssnFirst7Digit);
            this.ssnFirst7Digit = ssnFirst7Digit;
            return this;
        }

        public OperatorCode getOperatorCode() {
            return operatorCode;
        }

        public Personal setOperatorCode(final OperatorCode operatorCode) {
            this.operatorCode = operatorCode;
            return this;
        }

        public Personal setLineNumber(final String lineNumber) {
            assert lineNumber != null && (lineNumber.length() > 0) : "lineNumber shouldn't be null and not empty.";
            this.lineNumber = lineNumber;
            return this;
        }

        public String getEmail() {
            return email;
        }

        public Personal setEmail(final String email) {
            this.email = email;
            return this;
        }

        public String getCiHash() {
            return ciHash;
        }

        public Personal setCiHash(final String ciHash) {
            this.ciHash = ciHash;
            return this;
        }

        public PayableCard getPayableCard() {
            return payableCard;
        }

        public Personal setPayableCard(final PayableCard payableCard) {
            this.payableCard = payableCard;
            return this;
        }
    }

    public static enum OperatorCode {
        SKT, KT, LGU, SKTM, KTM, LGUM, UNKNOWN
    }

    /**
     * 가맹점으로 부터 카드 정보를 공유 받기 위한 DTO 를 정의한다
     *
     * @since 1.3.9
     */
    public static class PayableCard implements Serializable {
        private String cardNo;
        private String expireDate;
        private String cardIssuer;
        private String cardIssuerName;
        private String cardName;
        private String cardNameInEnglish;
        private String cardAcquirer;
        private CardType cardType;

        public String getCardNo() {
            return cardNo;
        }

        public PayableCard setCardNo(final String cardNo) {
            assert cardNo != null && (cardNo.length() > 0) : "cardNo shouldn't be null and not empty.";
            this.cardNo = cardNo;
            return this;
        }

        public String getExpireDate() {
            return expireDate;
        }

        public PayableCard setExpireDate(final String expireDate) {
            assert expireDate != null && (expireDate.length() > 0) : "expireDate shouldn't be null and not empty.";
            this.expireDate = expireDate;
            return this;
        }

        public String getCardIssuer() {
            return cardIssuer;
        }

        public PayableCard setCardIssuer(final String cardIssuer) {
            assert cardIssuer != null && (cardIssuer.length() > 0) : "cardIssuer shouldn't be null and not empty.";
            this.cardIssuer = cardIssuer;
            return this;
        }

        public String getCardIssuerName() {
            return cardIssuerName;
        }

        public PayableCard setCardIssuerName(final String cardIssuerName) {
            assert cardIssuerName != null && (cardIssuerName.length() > 0) : "cardIssuerName shouldn't be null and not empty.";
            this.cardIssuerName = cardIssuerName;
            return this;
        }

        public String getCardName() {
            return cardName;
        }

        public PayableCard setCardName(final String cardName) {
            assert cardName != null && (cardName.length() > 0) : "cardNo shouldn't be null and not empty.";
            this.cardName = cardName;
            return this;
        }

        public String getCardAcquirer() {
            return cardAcquirer;
        }

        public PayableCard setCardAcquirer(final String cardAcquirer) {
            assert cardAcquirer != null && cardAcquirer.length() > 0 : "cardAcquirer shouldn't be null and not empty.";
            this.cardAcquirer = cardAcquirer;
            return this;
        }

        public CardType getCardType() {
            return cardType;
        }

        public PayableCard setCardType(final CardType cardType) {
            assert cardType != null : "cardType shouldn't be null.";
            this.cardType = cardType;
            return this;
        }

        public String getCardNameInEnglish() {
            return cardNameInEnglish;
        }

        public PayableCard setCardNameInEnglish(final String cardNameInEnglish) {
            this.cardNameInEnglish = cardNameInEnglish;
            return this;
        }
    }

    public enum CardType {
        CREDIT("CC"),
        CHECK("CH");

        private String value;

        CardType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

    }
}
