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

import com.skplanet.syruppay.token.TokenBuilder;

/**
 * 정기/비정기 결제 정보에 대한 Claim 을 구성한다.
 *
 * @author 임형태
 * @since 1.3
 */
public class SubscriptionConfigurer<H extends TokenBuilder<H>> extends AbstractTokenConfigurer<SubscriptionConfigurer<H>, H> {
    private String autoPaymentId;
    private PayConfigurer.MatchedUser matchedUser;

    public SubscriptionConfigurer<H> withAutoPaymentId(final String autoPaymentId) {
        this.autoPaymentId = autoPaymentId;
        return this;
    }

    public SubscriptionConfigurer<H> withMatchedUser(final PayConfigurer.MatchedUser matchedUser) {
        this.matchedUser = matchedUser;
        return this;
    }

    public String getAutoPaymentId() {
        return autoPaymentId;
    }

    public PayConfigurer.MatchedUser getMatchedUser() {
        return matchedUser;
    }

    public String claimName() {
        return "subscription";
    }

    public void validRequired() throws Exception {
        // ignored
    }
}
