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

import com.skplanet.syruppay.token.ClaimConfigurer;
import com.skplanet.syruppay.token.TokenBuilder;
import com.skplanet.syruppay.token.claims.MapToSktUserConfigurer;
import com.skplanet.syruppay.token.claims.MapToSyrupPayUserConfigurer;
import com.skplanet.syruppay.token.claims.MerchantUserConfigurer;
import com.skplanet.syruppay.token.claims.OrderConfigurer;
import com.skplanet.syruppay.token.claims.PayConfigurer;
import com.skplanet.syruppay.token.claims.SubscriptionConfigurer;

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
     * @return {@link com.skplanet.syruppay.token.claims.MerchantUserConfigurer}
     */
    public MerchantUserConfigurer<? extends TokenBuilder> getLoginInfo();

    /**
     * 거래(주문)에 대하여 결제를 시도하기 위한 객체를 구성하여 반환한다.
     *
     * @return {@link com.skplanet.syruppay.token.claims.PayConfigurer}
     */
    public PayConfigurer<? extends TokenBuilder> getTransactionInfo();

    /**
     * 시럽페이 사용자를 매칭하기 위한 객체를 구성하여 반환한다.
     *
     * @return {@link com.skplanet.syruppay.token.claims.MapToSyrupPayUserConfigurer}
     */
    public MapToSyrupPayUserConfigurer<? extends TokenBuilder> getUserInfoMapper();

    /**
     * Expired Time 기준과 Not Before Time 을 기준하여 토큰이 유효한 시간 안에 있는지 여부를 검증하여 반환한다.
     *
     * @return boolean
     */
    public boolean isValidInTime();

    /**
     * 가맹점 사용자가 가입된 SKT 통신회선의 가입 정보를 확인하기 위한 객체를 구성하여 반환한다.
     *
     * @return {@link com.skplanet.syruppay.token.claims.MapToSktUserConfigurer}
     */
    public MapToSktUserConfigurer<? extends TokenBuilder> getLineInfo();

    /**
     * 시럽페이 체크 아웃을 이용하기 위한 정보를 구성하여 반환한다.
     *
     * @return {@link com.skplanet.syruppay.token.claims.OrderConfigurer}
     * @since 1.1
     */

    public OrderConfigurer<? extends TokenBuilder> getCheckoutInfo();


    /**
     * 시럽페이 정기 결제를 이용하기 위한 정보를 구성하여 반환한다.
     *
     * @return the subscription
     * @since 1.3.4
     */
    public SubscriptionConfigurer<? extends TokenBuilder> getSubscription();

    /**
     * 시럽페이 토큰에 포함된 Claim 정보를 확인하여 반환한다.
     *
     * @return claimConfigurer 목록
     * @since 1.3.7
     */
    public List<ClaimConfigurer> getClaims(SyrupPayToken.Claim... claims);

    /**
     * 시럽페이 토큰에 포함된 Claim 정보를 확인하여 반환한다.
     *
     * @return claimConfigurer
     * @since 1.3.7
     */

    public ClaimConfigurer getClaim(final SyrupPayToken.Claim claim);
}
