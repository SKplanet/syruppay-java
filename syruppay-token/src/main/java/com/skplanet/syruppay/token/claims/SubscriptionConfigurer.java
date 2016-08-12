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
    private RegistrationRestrictions registrationRestrictions;
    private Plan plan;

    public SubscriptionConfigurer<H> withAutoPaymentId(final String autoPaymentId) {
        this.autoPaymentId = autoPaymentId;
        return this;
    }

    public SubscriptionConfigurer<H> withRestrictionOf(final PayConfigurer.MatchedUser matchedUser) {
        if(registrationRestrictions == null) {
            registrationRestrictions = new RegistrationRestrictions();
        }
        this.registrationRestrictions.matchedUser = matchedUser;
        return this;
    }

    public SubscriptionConfigurer<H> with(final Plan plan) {
        this.plan = plan;
        return this;
    }

    public Plan getPlan() {
        return plan;
    }

    public String getAutoPaymentId() {
        return autoPaymentId;
    }

    public RegistrationRestrictions getRegistrationRestrictions() {
        return registrationRestrictions;
    }

    public void setMatchedUser(final PayConfigurer.MatchedUser matchedUser) {
        if(registrationRestrictions == null) {
            registrationRestrictions = new RegistrationRestrictions();
        }
        registrationRestrictions.matchedUser = matchedUser;
    }

    public String claimName() {
        return "subscription";
    }

    public void validRequired() throws Exception {
        // ignored
    }

    public static class RegistrationRestrictions {
        private PayConfigurer.MatchedUser matchedUser;

        public PayConfigurer.MatchedUser getMatchedUser() {
            return matchedUser;
        }
    }

    public static class Plan {
        private final Interval interval;
        private final String name;

        public Plan(final Interval interval, final String name) {
            assert interval != null && name != null && !(name.length() == 0);
            this.interval = interval;
            this.name = name;
        }

        public Interval getInterval() {
            return interval;
        }

        public String getName() {
            return name;
        }
    }

    public static enum Interval {
        ONDEMAND, MONTHLY, WEEKLY, BIWEEKLY
    }
}
