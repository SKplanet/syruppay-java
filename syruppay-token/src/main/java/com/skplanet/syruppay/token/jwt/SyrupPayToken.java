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
import com.skplanet.syruppay.token.ClaimConfigurerAdapter;
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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 시럽페이에서 사용되는 토큰를 추상화하여 정의한다.
 *
 * @author 임형태
 * @since 1.0
 */
public class SyrupPayToken implements Token {
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
        return nbf != null ? nbf : 0L;
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
        if(transactionInfo == null) {
            LOGGER.warn("create transactionInfo to set payment information");
            transactionInfo = new PayConfigurer();
        }
        LOGGER.warn("set paymentInfo element by deprecated method");
        transactionInfo.withAmount(paymentInfo.getPaymentAmt());
        transactionInfo.withLanguageForDisplay(PayConfigurer.Language.valueOf(paymentInfo.getLang().toUpperCase()));
        transactionInfo.withShippingAddress(paymentInfo.getShippingAddress());
        transactionInfo.withProductTitle(paymentInfo.getProductTitle());
        transactionInfo.withProductUrls(paymentInfo.getProductUrls());
        transactionInfo.withCurrency(PayConfigurer.Currency.valueOf(paymentInfo.getCurrencyCode().toUpperCase()));
        transactionInfo.withDeliveryName(paymentInfo.getDeliveryName());
        transactionInfo.withDeliveryPhoneNumber(paymentInfo.getDeliveryPhoneNumber());
        transactionInfo.withInstallmentPerCardInformation(paymentInfo.getCardInfoList());
    }

    @Deprecated
    @JsonProperty("paymentRestrictions")
    public void setPaymentRestrictions(PayConfigurer.PaymentRestriction paymentRestriction) {
        if(transactionInfo == null) {
            LOGGER.warn("create transactionInfo to set payment restrictions");
            transactionInfo = new PayConfigurer();
        }
        LOGGER.warn("set paymentRestrictions element by deprecated method");
        for (PayConfigurer.PayableLocaleRule r : PayConfigurer.PayableLocaleRule.values()) {
            if (r.toCode().equals(paymentRestriction.getCardIssuerRegion())) {
                transactionInfo.withPayableRuleWithCard(r);
            }
        }
    }

    public SubscriptionConfigurer getSubscription() {
        return subscription;
    }

    public List<ClaimConfigurer> getClaims() {
        return getClaims(Claim.values());
    }

    public ClaimConfigurer getClaim(final Claim claim) {
        try {
            return getFieldOfClaimIfNotExistNull(claim);
        } catch (IllegalAccessException e) {
            LOGGER.warn("except while find claim from token: {}", e.getMessage());
        } catch (NoSuchFieldException e) {
            LOGGER.warn("except while find claim from token: {}", e.getMessage());
        }
        return null;
    }

    public List<ClaimConfigurer> getClaims(final Claim... claims) {
        List<ClaimConfigurer> l = new ArrayList<ClaimConfigurer>();
        for (Claim c : claims) {
            try {
                ClaimConfigurer cc = getFieldOfClaimIfNotExistNull(c);
                if (cc != null) {
                    l.add(cc);
                }
            } catch (IllegalAccessException e) {
                LOGGER.warn("except while find claim from token: {}", e.getMessage());
            } catch (NoSuchFieldException e) {
                LOGGER.warn("except while find claim from token: {}", e.getMessage());
            }
        }
        return Collections.unmodifiableList(l);
    }

    private ClaimConfigurer getFieldOfClaimIfNotExistNull(final Claim c) throws IllegalAccessException, NoSuchFieldException {
        for (Field f : SyrupPayToken.class.getDeclaredFields()) {
            if (f.getType().isAssignableFrom(c.getConfigurer())) {
                return (ClaimConfigurer) f.get(this);
            }
        }
        return null;
    }

    public static enum Claim {
        TO_SIGNUP(MerchantUserConfigurer.class), TO_LOGIN(MerchantUserConfigurer.class), TO_PAY(PayConfigurer.class), TO_CHECKOUT(OrderConfigurer.class), TO_MAP_USER(MapToSyrupPayUserConfigurer.class), TO_SUBSCRIPTION(SubscriptionConfigurer.class);

        <C extends ClaimConfigurerAdapter> Claim(Class<C> configurer) {
            this.configurer = configurer;
        }

        Class<?> configurer;

        public Class<?> getConfigurer() {
            return configurer;
        }
    }

}
