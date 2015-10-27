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
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * 결제를 위한 거래 인증 정보를 Claim 을 설정한다.
 *
 * @author 임형태
 * @since 1.0
 */
public final class PayConfigurer<H extends TokenBuilder<H>> extends AbstractTokenConfigurer<PayConfigurer<H>, H> {
    private static final Set<String> ISO_LANGUAGES = new HashSet<String>(Arrays.asList(Locale.getISOLanguages()));
    private static final Set<String> ISO_COUNTRIES = new HashSet<String>(Arrays.asList(Locale.getISOCountries()));

    private String mctTransAuthId;
    private PaymentInformationBySeller paymentInfo = new PaymentInformationBySeller();
    private PaymentRestriction paymentRestrictions = new PaymentRestriction();

    public static boolean isValidCountryAlpha2Code(String code) {
        return ISO_COUNTRIES.contains(code.contains(":") ? code.substring(code.indexOf(":") + 1).toUpperCase() : code.toUpperCase());
    }

    public static boolean isValidLanuageCode(String code) {
        return ISO_LANGUAGES.contains(code);
    }

    public String getMctTransAuthId() {
        return mctTransAuthId;
    }

    public PaymentInformationBySeller getPaymentInfo() {
        return paymentInfo;
    }

    public PaymentRestriction getPaymentRestrictions() {
        return paymentRestrictions;
    }

    public PayConfigurer<H> withOrderIdOfMerchant(String orderId) {
        mctTransAuthId = orderId;
        return this;
    }

    public PayConfigurer<H> withProductTitle(String productTitle) {
        paymentInfo.productTitle = productTitle;
        return this;
    }

    public PayConfigurer<H> withProductUrls(List<String> productUrls) {
        for (String productDetail : productUrls) {
            if (!(productDetail.startsWith("http") || productDetail.startsWith("https"))) {
                throw new IllegalArgumentException("product details should be contained http or https urls. check your input!");
            }
        }
        paymentInfo.productUrls.addAll(productUrls);
        return this;
    }

    public PayConfigurer<H> withProductUrls(String... url) {
        return withProductUrls(Arrays.asList(url));
    }

    public PayConfigurer<H> withLanguageForDisplay(Language l) {
        paymentInfo.lang = l.toString();
        return this;
    }

    public PayConfigurer<H> withCurrency(Currency c) {
        paymentInfo.currencyCode = c.toString();
        return this;
    }

    public PayConfigurer<H> withShippingAddress(ShippingAddress shippingAddress) {
        paymentInfo.shippingAddress = shippingAddress.mapToStringForFds();
        return this;
    }

    public PayConfigurer<H> withShippingAddress(String shippingAddress) {
        paymentInfo.shippingAddress = shippingAddress;
        return this;
    }

    public PayConfigurer<H> withAmount(int paymentAmount) {
        if (paymentAmount <= 0) {
            throw new IllegalArgumentException("Cannot be smaller than 0. Check yours input value : " + paymentAmount);
        }
        paymentInfo.paymentAmt = paymentAmount;
        return this;
    }

    public PayConfigurer<H> withDeliveryPhoneNumber(String deliveryPhoneNumber) {
        paymentInfo.deliveryPhoneNumber = deliveryPhoneNumber;
        return this;
    }

    public PayConfigurer<H> withDeliveryName(String deliveryName) {
        paymentInfo.deliveryName = deliveryName;
        return this;
    }

    public PayConfigurer<H> withBeAbleToExchangeToCash(boolean exchangeable) {
        paymentInfo.isExchangeable = exchangeable;
        return this;
    }

    public PayConfigurer<H> withInstallmentPerCardInformation(List<CardInstallmentInformation> cards) {
        paymentInfo.cardInfoList.addAll(cards);
        return this;
    }

    public PayConfigurer<H> withInstallmentPerCardInformation(CardInstallmentInformation... card) {
        paymentInfo.cardInfoList.addAll(Arrays.asList(card));
        return this;
    }

    public PayConfigurer<H> withPayableRuleWithCard(PayableLocaleRule r) {
        paymentRestrictions.cardIssuerRegion = r.toCode();
        return this;
    }

    public String claimName() {
        return "transactionInfo";
    }

    public void validRequired() throws Exception {
        if (this.mctTransAuthId == null || this.paymentInfo.productTitle == null || this.paymentInfo.lang == null || this.paymentInfo.currencyCode == null || this.paymentInfo.paymentAmt <= 0) {
            throw new IllegalArgumentException("some of required fields is null or wrong. " +
                    "you should set orderIdOfMerchant : " + mctTransAuthId
                    + ",  productTitle : " + paymentInfo.productTitle
                    + ",  languageForDisplay : " + paymentInfo.lang
                    + ",  currency : " + paymentInfo.currencyCode
                    + ",  amount : " + paymentInfo.paymentAmt
            );
        }

        if (mctTransAuthId.length() > 40) {
            throw new IllegalArgumentException("order id of merchant couldn't be longer than 40. but yours is " + mctTransAuthId.length());
        }
    }


    public static enum Language {
        KO, EN
    }

    public static enum Currency {
        KRW, USD
    }

    public static enum PayableLocaleRule {
        ONLY_ALLOWED_KOR("ALLOWED:KOR"), ONLY_NOT_ALLOED_KOR("NOT_ALLOWED:KOR"), ONLY_ALLOWED_USA("ALLOWED:USA"), ONLY_NOT_ALLOED_USA("NOT_ALLOWED:USA");

        String code;

        PayableLocaleRule(String code) {
            this.code = code;
        }

        public String toCode() {
            return code;
        }
    }

    public static enum DeliveryRestriction {
        NOT_FAR_AWAY, FAR_AWAY, FAR_FAR_AWAY
    }

    public static final class ShippingAddress implements Serializable {
        private static final long serialVersionUID = 5453957807241639495L;
        private String id;
        private String userActionCode;
        private String name;
        private String countryCode;
        private String zipCode;
        private String mainAddress;
        private String detailAddress;
        private String city;
        private String state;
        private String recipientName;
        private String recipientPhoneNumber;
        private DeliveryRestriction deliveryRestriction;
        private Integer defaultDeliveryCost;
        private Integer additionalDeliveryCost;
        private Integer orderApplied;

        public ShippingAddress() {
        }

        public ShippingAddress(String zipCode, String mainAddress, String detailAddress, String city, String state, String countryCode) {
            this.zipCode = zipCode;
            this.mainAddress = mainAddress;
            this.detailAddress = detailAddress;
            this.city = city;
            this.state = state;
            this.countryCode = setCountryCode(countryCode).getCountryCode();
        }

        public String getUserActionCode() {
            return userActionCode;
        }

        public ShippingAddress setUserActionCode(String userActionCode) {
            this.userActionCode = userActionCode;
            return this;
        }

        public String getId() {
            return id;
        }

        public ShippingAddress setId(String id) {
            this.id = id;
            return this;
        }

        public String getName() {
            return name;
        }

        public ShippingAddress setName(String name) {
            this.name = name;
            return this;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public ShippingAddress setCountryCode(String countryCode) {
            if (!isValidCountryAlpha2Code(countryCode)) {
                throw new IllegalArgumentException("countryCode should meet the specifications of ISO-3166 Alpha2(as KR, US) except prefix like a2. yours : " + countryCode);
            }
            this.countryCode = countryCode.toLowerCase();
            return this;
        }

        public String getZipCode() {
            return zipCode;
        }

        public ShippingAddress setZipCode(String zipCode) {
            this.zipCode = zipCode;
            return this;
        }

        public String getMainAddress() {
            return mainAddress;
        }

        public ShippingAddress setMainAddress(String mainAddress) {
            this.mainAddress = mainAddress;
            return this;
        }

        public String getDetailAddress() {
            return detailAddress;
        }

        public ShippingAddress setDetailAddress(String detailAddress) {
            this.detailAddress = detailAddress;
            return this;
        }

        public String getCity() {
            return city;
        }

        public ShippingAddress setCity(String city) {
            this.city = city;
            return this;
        }

        public String getState() {
            return state;
        }

        public ShippingAddress setState(String state) {
            this.state = state;
            return this;
        }

        public String getRecipientName() {
            return recipientName;
        }

        public ShippingAddress setRecipientName(String recipientName) {
            this.recipientName = recipientName;
            return this;
        }

        public String getRecipientPhoneNumber() {
            return recipientPhoneNumber;
        }

        public ShippingAddress setRecipientPhoneNumber(String recipientPhoneNumber) {
            if (recipientPhoneNumber != null && !recipientPhoneNumber.matches("\\d+")) {
                throw new IllegalArgumentException("phone number should be contained numbers. remove characters as '-'. yours : " + recipientPhoneNumber);
            }
            this.recipientPhoneNumber = recipientPhoneNumber;
            return this;
        }

        public DeliveryRestriction getDeliveryRestriction() {
            return deliveryRestriction;
        }

        public ShippingAddress setDeliveryRestriction(DeliveryRestriction deliveryRestriction) {
            this.deliveryRestriction = deliveryRestriction;
            return this;
        }

        public Integer getDefaultDeliveryCost() {
            return defaultDeliveryCost;
        }

        public ShippingAddress setDefaultDeliveryCost(Integer defaultDeliveryCost) {
            this.defaultDeliveryCost = defaultDeliveryCost;
            return this;
        }

        public Integer getAdditionalDeliveryCost() {
            return additionalDeliveryCost;
        }

        public ShippingAddress setAdditionalDeliveryCost(Integer additionalDeliveryCost) {
            this.additionalDeliveryCost = additionalDeliveryCost;
            return this;
        }

        public int getOrderApplied() {
            return orderApplied;
        }

        public ShippingAddress setOrderApplied(Integer orderApplied) {
            this.orderApplied = orderApplied;
            return this;
        }

        public String mapToStringForFds() {
            return countryCode + "|" + zipCode + "|" + mainAddress + "|" + detailAddress + "|" + city + "|" + state + "|";
        }

        public void validRequiredToCheckout() {
            if (id == null || name == null || countryCode == null || zipCode == null || mainAddress == null || detailAddress == null || recipientName == null || recipientPhoneNumber == null) {
                throw new IllegalArgumentException("ShippingAddress object to checkout couldn't be with null fields. id : " + id + ", name : " + name + ", countryCode : " + countryCode + ", zipCode : " + zipCode + ", mainAddress : " + mainAddress + ", detailAddress : " + detailAddress + ", recipientName : " + recipientName + ", recipientPhoneNumber : " + recipientPhoneNumber);
            }

            if (!isValidCountryAlpha2Code(countryCode)) {
                throw new IllegalArgumentException("countryCode should meet the specifications of ISO-3166 Alpha2(as KR, US) except prefix like a2. yours : " + countryCode);
            }

            if (defaultDeliveryCost <= 0) {
                throw new IllegalArgumentException("defaultDeliveryCost field should be bigger than 0. yours : " + defaultDeliveryCost);
            }
        }
    }

    public static final class CardInstallmentInformation implements Serializable {
        private static final long serialVersionUID = 3062753174786502628L;
        private String cardCode;
        private String monthlyInstallmentInfo;

        public CardInstallmentInformation() {
        }

        public CardInstallmentInformation(String cardCode, String monthlyInstallmentInfo) {
            this.cardCode = cardCode;
            this.monthlyInstallmentInfo = monthlyInstallmentInfo;
        }

        public String getCardCode() {
            return cardCode;
        }

        public String getMonthlyInstallmentInfo() {
            return monthlyInstallmentInfo;
        }
    }

    public static final class PaymentInformationBySeller implements Serializable {
        private static final long serialVersionUID = -3493693117216167705L;
        private List<CardInstallmentInformation> cardInfoList = new ArrayList<CardInstallmentInformation>();
        private String productTitle;
        private List<String> productUrls = new ArrayList<String>();
        private String lang = "KO";
        private String currencyCode = "KRW";
        private int paymentAmt;
        private String shippingAddress;
        private String deliveryPhoneNumber;
        private String deliveryName;
        private boolean isExchangeable;

        public String getProductTitle() {
            return productTitle;
        }

        public List<String> getProductUrls() {
            return Collections.unmodifiableList(productUrls);
        }

        public String getLang() {
            return lang;
        }

        public String getCurrencyCode() {
            return currencyCode;
        }

        public int getPaymentAmt() {
            return paymentAmt;
        }

        public String getShippingAddress() {
            return shippingAddress;
        }

        public String getDeliveryPhoneNumber() {
            return deliveryPhoneNumber;
        }

        public String getDeliveryName() {
            return deliveryName;
        }

        @JsonProperty("isExchangeable")
        public boolean isExchangeable() {
            return isExchangeable;
        }

        public List<CardInstallmentInformation> getCardInfoList() {
            return Collections.unmodifiableList(cardInfoList);
        }

        @Deprecated
        @JsonProperty("productDetails")
        public void setProductDetails(List<String> productDetails) {
            this.productUrls = productDetails;
        }
    }

    public static final class PaymentRestriction implements Serializable {
        private static final long serialVersionUID = 3528805314551672041L;
        private String cardIssuerRegion = "ALLOWED:KOR";

        public String getCardIssuerRegion() {
            return cardIssuerRegion;
        }
    }
}