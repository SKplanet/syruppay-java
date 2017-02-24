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

import com.skplanet.syruppay.token.Builder;

import java.io.Serializable;

/**
 * 정기/비정기 결제 정보에 대한 Claim 을 구성한다.
 *
 * @author 임형태
 * @since 1.3
 */
public class SubscriptionClaim<H extends Builder<H>> extends AbstractTokenClaim<SubscriptionClaim<H>, H> {
    private String autoPaymentId;
    private RegistrationRestrictions registrationRestrictions;
    private Plan plan;
    private String mctSubscriptionRequestId;
    private String promotionCode;

    public SubscriptionClaim<H> withAutoPaymentId(final String autoPaymentId) {
        this.autoPaymentId = autoPaymentId;
        return this;
    }

    public SubscriptionClaim<H> withRestrictionOf(final PayClaim.MatchedUser matchedUser) {
        if (registrationRestrictions == null) {
            registrationRestrictions = new RegistrationRestrictions();
        }
        this.registrationRestrictions.matchedUser = matchedUser;
        return this;
    }

    public SubscriptionClaim<H> with(final Plan plan) {
        this.plan = plan;
        return this;
    }

    public SubscriptionClaim<H> withMerchantSubscriptionRequestId(String mctSubscriptionRequestId) {
        this.mctSubscriptionRequestId = mctSubscriptionRequestId;
        return this;
    }

    public String getPromotionCode() {
        return promotionCode;
    }

    public SubscriptionClaim<H> withPromotionCode(final String promotionCode) {
        this.promotionCode = promotionCode;
        return this;
    }

    public String getMctSubscriptionRequestId() {
        return mctSubscriptionRequestId;
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

    public void setMatchedUser(final PayClaim.MatchedUser matchedUser) {
        if (registrationRestrictions == null) {
            registrationRestrictions = new RegistrationRestrictions();
        }
        registrationRestrictions.matchedUser = matchedUser;
    }

    public String claimName() {
        return "subscription";
    }

    public void validRequired() throws Exception {
        if (plan != null) {
            plan.valid();
        }
    }

    public static class RegistrationRestrictions implements Serializable {
        private PayClaim.MatchedUser matchedUser;

        public PayClaim.MatchedUser getMatchedUser() {
            return matchedUser;
        }
    }

    public static class Plan implements Serializable {
        private Interval interval;
        private String name;

        public Plan(final Interval interval, final String name) {
            assert interval != null && name != null && !(name.length() == 0);
            this.interval = interval;
            this.name = name;
        }

        public Plan() {
        }

        public Interval getInterval() {
            return interval;
        }

        public String getName() {
            return name;
        }

        public void valid() {
            if (interval == null || name == null || name.length() == 0) {
                throw new IllegalArgumentException("plan of subscription object couldn't be with null or empty fields interval : " + interval + ", name : " + name);
            }
        }
    }

    public static enum Interval {
        ONDEMAND, MONTHLY, WEEKLY, BIWEEKLY
    }
}
