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

import com.skplanet.syruppay.token.claims.MapToSktUserConfigurer;
import com.skplanet.syruppay.token.claims.MapToSyrupPayUserConfigurer;
import com.skplanet.syruppay.token.claims.MerchantUserConfigurer;
import com.skplanet.syruppay.token.claims.OrderConfigurer;
import com.skplanet.syruppay.token.claims.PayConfigurer;
import com.skplanet.syruppay.token.claims.SubscriptionConfigurer;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 시럽페이에서 사용되는 토큰를 추상화하여 정의한다.
 *
 * @author 임형태
 * @since 1.0
 */
public final class SyrupPayToken implements Token {
    private static final Logger LOGGER = LoggerFactory.getLogger(SyrupPayToken.class.getName());

    private static final long serialVersionUID = 1684081647352606412L;

    private final String aud = "https://pay.syrup.co.kr";
    private final String typ = "jose";
    private String iss;
    private long exp;
    private long iat;
    private String jti;
    private Long nbf;
    private String sub;

    private MerchantUserConfigurer loginInfo;
    private PayConfigurer transactionInfo;
    private MapToSyrupPayUserConfigurer userInfoMapper;
    private MapToSktUserConfigurer lineInfo;
    private OrderConfigurer checkoutInfo;
    private SubscriptionConfigurer subscription;

    /**
     * {@inheritDoc}
     */
    public OrderConfigurer getCheckoutInfo() {
        return checkoutInfo;
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    public boolean isValidInTime() {
        return (nbf == null || (nbf <= 0) || DateTime.now().isAfter(nbf * 1000)) && DateTime.now().isBefore(exp * 1000);
    }

    /**
     * {@inheritDoc}
     */
    public String getAud() {
        return aud;
    }

    /**
     * {@inheritDoc}
     */
    public String getTyp() {
        return typ;
    }

    /**
     * {@inheritDoc}
     */
    public String getIss() {
        return iss;
    }

    /**
     * {@inheritDoc}
     */
    public long getExp() {
        return exp;
    }

    /**
     * {@inheritDoc}
     */
    public long getIat() {
        return iat;
    }

    /**
     * {@inheritDoc}
     */
    public String getJti() {
        return jti;
    }

    /**
     * {@inheritDoc}
     */
    public long getNbf() {
        return nbf == null ? 0 : nbf;
    }

    /**
     * {@inheritDoc}
     */
    public String getSub() {
        return sub;
    }

    /**
     * {@inheritDoc}
     */
    public MerchantUserConfigurer getLoginInfo() {
        return loginInfo;
    }

    /**
     * {@inheritDoc}
     */
    public PayConfigurer getTransactionInfo() {
        if (transactionInfo == null) {
            transactionInfo = new PayConfigurer();
        }
        return transactionInfo;
    }

    @Deprecated
    @JsonProperty("transactionInfo")
    public void setTransactionInfo(PayConfigurer transactionInfo) {
        if (this.transactionInfo != null && this.transactionInfo.getMctTransAuthId() == null && transactionInfo.getPaymentInfo().getProductTitle() == null) {
            LOGGER.warn("set only mctTransAuthId of transactionInfo element by deprecated method");
            this.transactionInfo.withOrderIdOfMerchant(transactionInfo.getMctTransAuthId());
        } else {
            this.transactionInfo = transactionInfo;
        }
    }

    /**
     * {@inheritDoc}
     */
    public MapToSyrupPayUserConfigurer getUserInfoMapper() {
        return userInfoMapper;
    }

    /**
     * {@inheritDoc}
     */
    public MapToSktUserConfigurer getLineInfo() {
        return lineInfo;
    }

    @Deprecated
    @JsonProperty("paymentInfo")
    public void setPaymentInfo(PayConfigurer.PaymentInformationBySeller paymentInfo) {
        LOGGER.warn("set paymentInfo element by deprecated method");
        getTransactionInfo().withAmount(paymentInfo.getPaymentAmt());
        getTransactionInfo().withLanguageForDisplay(PayConfigurer.Language.valueOf(paymentInfo.getLang().toUpperCase()));
        getTransactionInfo().withShippingAddress(paymentInfo.getShippingAddress());
        getTransactionInfo().withProductTitle(paymentInfo.getProductTitle());
        getTransactionInfo().withProductUrls(paymentInfo.getProductUrls());
        getTransactionInfo().withCurrency(PayConfigurer.Currency.valueOf(paymentInfo.getCurrencyCode().toUpperCase()));
        getTransactionInfo().withDeliveryName(paymentInfo.getDeliveryName());
        getTransactionInfo().withDeliveryPhoneNumber(paymentInfo.getDeliveryPhoneNumber());
        getTransactionInfo().withInstallmentPerCardInformation(paymentInfo.getCardInfoList());
    }

    @Deprecated
    @JsonProperty("paymentRestrictions")
    public void setPaymentRestrictions(PayConfigurer.PaymentRestriction paymentRestriction) {
        LOGGER.warn("set paymentRestrictions element by deprecated method");
        for (PayConfigurer.PayableLocaleRule r : PayConfigurer.PayableLocaleRule.values()) {
            if (r.toCode().equals(paymentRestriction.getCardIssuerRegion())) {
                getTransactionInfo().withPayableRuleWithCard(r);
            }
        }
    }

    public SubscriptionConfigurer getSubscription() {
        return subscription;
    }
}
