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

package com.skplanet.syruppay.token.jwt;

import com.skplanet.syruppay.token.Builder;
import com.skplanet.syruppay.token.ClaimConfigurer;
import com.skplanet.syruppay.token.ClaimConfigurerAdapter;
import com.skplanet.syruppay.token.claims.*;

import java.io.Serializable;
import java.util.List;

/**
 * 시럽페이에서 사용하는 토큰에 대한 규격을 정의한다.
 *
 * @author 임형태
 * @since 1.0
 */
public interface Token extends Serializable, JwtToken {
    /**
     * 가맹점의 사용자 정보를 인증(Authentication) 하기 위한 객체를 구성하여 반환한다.
     *
     * @return {@link MerchantUserClaim}
     */
    MerchantUserClaim<? extends Builder> getLoginInfo();

    /**
     * 거래(주문)에 대하여 결제를 시도하기 위한 객체를 구성하여 반환한다.
     *
     * @return {@link PayClaim}
     */
    PayClaim<? extends Builder> getTransactionInfo();

    /**
     * 시럽페이 사용자를 매칭하기 위한 객체를 구성하여 반환한다.
     *
     * @return {@link MapToUserClaim}
     */
    MapToUserClaim<? extends Builder> getUserInfoMapper();

    /**
     * Expired Time 기준과 Not Before Time 을 기준하여 토큰이 유효한 시간 안에 있는지 여부를 검증하여 반환한다.
     *
     * @return boolean
     */
    boolean isValidInTime();

    /**
     * 가맹점 사용자가 가입된 SKT 통신회선의 가입 정보를 확인하기 위한 객체를 구성하여 반환한다.
     *
     * @return {@link MapToSktUserClaim}
     */
    MapToSktUserClaim<? extends Builder> getLineInfo();

    /**
     * 시럽페이 체크 아웃을 이용하기 위한 정보를 구성하여 반환한다.
     *
     * @return {@link OrderClaim}
     * @since 1.1
     */

    OrderClaim<? extends Builder> getCheckoutInfo();


    /**
     * 시럽페이 정기 결제를 이용하기 위한 정보를 구성하여 반환한다.
     *
     * @return the subscription
     * @since 1.3.4
     */
    SubscriptionClaim<? extends Builder> getSubscription();

    /**
     * 시럽페이 토큰에 포함된 Claim 정보를 확인하여 반환한다.
     *
     * @return claimConfigurer 목록
     * @since 1.3.7
     */
    List<ClaimConfigurer> getClaims(SyrupPayToken.Claim... claims);

    /**
     * 시럽페이 토큰에 포함된 Claim 정보를 확인하여 반환한다.
     *
     * @return claimConfigurer
     * @since 1.3.7
     */

    ClaimConfigurer getClaim(final SyrupPayToken.Claim claim);

    enum Claim {
        TO_SIGNUP(MerchantUserClaim.class), TO_LOGIN(MerchantUserClaim.class), TO_PAY(PayClaim.class), TO_CHECKOUT(OrderClaim.class), TO_MAP_USER(MapToUserClaim.class), TO_SUBSCRIPTION(SubscriptionClaim.class);

        <C extends ClaimConfigurerAdapter> Claim(Class<C> configurer) {
            this.configurer = configurer;
        }

        Class<?> configurer;

        public Class<?> getConfigurer() {
            return configurer;
        }
    }
}
