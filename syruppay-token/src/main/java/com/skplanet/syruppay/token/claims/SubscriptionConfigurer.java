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
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 정기/비정기 결제 정보에 대한 Claim 을 구성한다.
 *
 * @author 임형태
 * @since 1.3
 */
public class SubscriptionConfigurer<H extends TokenBuilder<H>> extends AbstractTokenConfigurer<SubscriptionConfigurer<H>, H> {
    private SubscriptionType subscriptionType;
    private String shippingAddress;
    private long subscriptionStartDate;
    private long subscriptionFinishDate;
    private PaymentCycle paymentCycle;
    private List<ProductInfo> productInfo = new ArrayList<ProductInfo>();

    public String getShippingAddress() {
        return shippingAddress;
    }

    public SubscriptionConfigurer<H> withShippingAddress(PayConfigurer.ShippingAddress shippingAddress) {
        this.shippingAddress = shippingAddress.mapToStringForFds();
        return this;
    }

    public SubscriptionConfigurer<H> fixed() {
        subscriptionType = SubscriptionType.FIXED;
        return this;
    }

    public SubscriptionConfigurer<H> unfixed() {
        subscriptionType = SubscriptionType.UNFIXED;
        return this;
    }

    public SubscriptionConfigurer<H> withSubscriptionStartDate(long subscriptionStartDate) {
        this.subscriptionStartDate = subscriptionStartDate;
        return this;
    }

    public SubscriptionConfigurer<H> withSubscriptionFinishDate(long subscriptionFinishDate) {
        this.subscriptionFinishDate = subscriptionFinishDate;
        return this;
    }

    public SubscriptionConfigurer<H> withPaymentCycle(PaymentCycle paymentCycle) {
        this.paymentCycle = paymentCycle;
        return this;
    }

    public SubscriptionConfigurer<H> addProductInfo(ProductInfo productInfo) {
        this.productInfo.add(productInfo);
        return this;
    }

    public SubscriptionConfigurer<H> withProductInfo(List<ProductInfo> productInfo) {
        this.productInfo = productInfo;
        return this;
    }

    public SubscriptionType getSubscriptionType() {
        return subscriptionType;
    }

    public long getSubscriptionStartDate() {
        return subscriptionStartDate;
    }

    public long getSubscriptionFinishDate() {
        return subscriptionFinishDate;
    }

    public PaymentCycle getPaymentCycle() {
        return paymentCycle;
    }

    public List<ProductInfo> getProductInfo() {
        return Collections.unmodifiableList(productInfo);
    }

    public String claimName() {
        return "subscription";
    }

    public void validRequired() throws Exception {
        if (this.subscriptionType == null) {
            throw new IllegalArgumentException("some of required fields is null(or empty) or wrong. " +
                    "you should set subscriptionType : null"

            );
        }

        if (productInfo.isEmpty()) {
            throw new IllegalArgumentException("product info list couldn't be empty. you should set product at least one more by addProductInfo() or setProductInfo.");
        }

        for (ProductInfo p : productInfo) {
            p.validRequiredToProductInfo();
        }
    }

    public static enum SubscriptionType {
        FIXED, UNFIXED
    }

    public static enum PaymentCycle {
        ONCE_A_WEEK, ONCE_TWO_WEEKS, ONCE_A_MONTH, ONCE_TWO_MONTHS
    }

    public static class ProductInfo {
        private String productId;
        private String productTitle;
        private List<String> productUrls;
        private int paymentAmt;
        private PayConfigurer.Currency currencyCode;


        public void validRequiredToProductInfo() {
            if (productId == null || productId.isEmpty()
                    || productTitle == null || productTitle.isEmpty()
                    || paymentAmt == 0
                    || currencyCode == null) {
                throw new IllegalArgumentException("some of required fields is null(or empty) or wrong. " +
                        "you should set productId : " + productId +
                        ",  productTitle : " + productTitle +
                        ",  currencyCode : " + currencyCode +
                        " and paymentAmt should be bigger than 0. yours : " + paymentAmt);
            }
        }

        public ProductInfo setProductId(String productId) {
            this.productId = productId;
            return this;
        }

        public ProductInfo setProductTitle(String productTitle) {
            this.productTitle = productTitle;
            return this;
        }

        public ProductInfo setProductUrls(List<String> productUrls) {
            this.productUrls = productUrls;
            return this;
        }

        @JsonIgnore
        public ProductInfo setPaymentAmount(int paymentAmount) {
            this.paymentAmt = paymentAmt;
            return this;
        }

        public void setCurrencyCode(PayConfigurer.Currency currencyCode) {
            this.currencyCode = currencyCode;
        }

        public String getProductId() {
            return productId;
        }

        public String getProductTitle() {
            return productTitle;
        }

        public List<String> getProductUrls() {
            return Collections.unmodifiableList(productUrls);
        }

        @JsonIgnore
        public int getPaymentAmount() {
            return paymentAmt;
        }

        public PayConfigurer.Currency getCurrencyCode() {
            return currencyCode;
        }
    }
}
