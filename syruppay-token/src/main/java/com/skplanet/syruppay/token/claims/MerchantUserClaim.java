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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skplanet.syruppay.token.Builder;

/**
 * 가맹점 사용자에 대한 Claim 설정을 정의한다.
 * 기존 시럽페이 토큰의 하위 호화선을 보장하기 위하여 맴버 변수와 메소드 명 간의 차이가 발생할 수 있으니 주의해야 한다.
 *
 * @author 임형태
 * @since 1.0
 */
public final class MerchantUserClaim<H extends Builder<H>> extends AbstractTokenClaim<MerchantUserClaim<H>, H> {
    private String mctUserId;
    private String extraUserId;
    @Deprecated
    private String implicitSSOSeed;
    @JsonProperty("SSOCredential")
    private String ssoCredential;
    private String deviceIdentifier;
    @JsonProperty("SSOPolicy")
    private SsoPolicy ssoPolicy;

    public MerchantUserClaim() {
    }

    /**
     * 가맹점의 Unique 한 회원 ID를 반환한다.
     *
     * @return 가맹점의 회원 ID
     */
    public String getMctUserId() {
        return mctUserId;
    }

    /**
     * 가맹점에서 관리하는 회원 ID 와 MDN, 디바이스 번호 등과 같이 별도로 관리되는 회원 체계의 식별자를 반환한다.
     *
     * @return 추가 가맹점 회원 ID
     */
    public String getExtraUserId() {
        return extraUserId;
    }

    /**
     * SSO 를 암묵적으로 생성하기 위한 Seed 값을 반환한다.
     *
     * @return Seed 값
     */
    public String getImplicitSSOSeed() {
        return implicitSSOSeed;
    }

    /**
     * 시럽페이로 자동 로그인 하기 위한 SSO Credential 를 반환한다.
     *
     * @return the sso credential
     */
    @JsonProperty("SSOCredential")
    public String getSsoCredential() {
        return ssoCredential;
    }

    public String getDeviceIdentifier() {
        return deviceIdentifier;
    }

    @JsonProperty("SSOPolicy")
    public SsoPolicy getSsoPolicy() {
        return ssoPolicy;
    }

    /**
     * 시럽페이로 자동 로그인 하기 위한 SSO Credential 을 입력한다.
     *
     * @param ssoCredential sso credential
     * @return <code>this</code>
     */
    public MerchantUserClaim<H> withSsoCredential(String ssoCredential) {
        this.ssoCredential = ssoCredential;
        return this;
    }

    /**
     * 가맹점에서 관리하는 Unique 한 회원 ID 를 입력한다.
     * 해당 가맹점 회원 ID 는 시럽페이 회원과 가맹점의 회원에 대한 매개변수로 사용된다.
     *
     * @param merchantUserId 회원 ID
     * @return <code>this</code>
     */
    public MerchantUserClaim<H> withMerchantUserId(String merchantUserId) {
        this.mctUserId = merchantUserId;
        return this;
    }

    /**
     * 가맹점에서 관리하는 회원 ID 와 MDN, 디바이스 번호 등과 같이 별도로 관리되는 회원 체계의 식별자를 입력한다.
     *
     * @param extraMerchantUserId 추가 가맹점 회원 ID
     * @return <code>this</code>
     */
    public MerchantUserClaim<H> withExtraMerchantUserId(String extraMerchantUserId) {
        this.extraUserId = extraMerchantUserId;
        return this;
    }

    /**
     * SSO 를 암묵적으로 생성하기 위한 Seed 값을 입력한다.
     *
     * @param implicitSSOSeed Seed 값
     * @return <code>this</code>
     */
    public MerchantUserClaim<H> withImplicitSSOSeed(String implicitSSOSeed) {
        this.implicitSSOSeed = implicitSSOSeed;
        return this;
    }

    /**
     * Device를 식별할 수 있는 고유 id를 입력한다.
     *
     * @param deviceIdentifier device 를 식별할 수 있는 ID 값
     */
    public MerchantUserClaim<H> withDeviceIdentifier(String deviceIdentifier) {
        this.deviceIdentifier = deviceIdentifier;
        return this;
    }

    public MerchantUserClaim<H> isNotApplicableSso() {
        this.ssoPolicy = SsoPolicy.NOT_APPLICABLE;
        return this;
    }

    public String claimName() {
        return "loginInfo";
    }

    public void validRequired() throws Exception {
        if (this.mctUserId == null || this.mctUserId.length() == 0) {
            throw new IllegalArgumentException("when you try to login or sign up, merchant user id couldn't be null. you should set merchant user id  by SyrupPayTokenHandler.login().withMerchantUserId(String) or SyrupPayTokenHandler.signup().withMerchantUserId(String)");
        }
    }

    public enum SsoPolicy {
        NOT_APPLICABLE
    }
}
