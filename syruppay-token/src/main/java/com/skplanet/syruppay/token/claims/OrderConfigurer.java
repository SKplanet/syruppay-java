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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 시럽페이 체크아웃을 위한 Claim 정보를 설정한다.
 *
 * @author 임형태
 * @since 1.1
 */
public final class OrderConfigurer<H extends TokenBuilder<H>> extends AbstractTokenConfigurer<OrderConfigurer<H>, H> {
    private int productPrice;
    private String submallName;
    private String privacyPolicyRequirements;
    private boolean mainShippingAddressSettingDisabled;
    private ProductDeliveryInfo productDeliveryInfo;
    private List<Offer> offerList = new ArrayList<Offer>();
    private List<Loyalty> loyaltyList = new ArrayList<Loyalty>();
    private List<PayConfigurer.ShippingAddress> shippingAddressList = new ArrayList<PayConfigurer.ShippingAddress>();
    private List<MonthlyInstallment> monthlyInstallmentList = new ArrayList<MonthlyInstallment>();

    public List<MonthlyInstallment> getMonthlyInstallmentList() {
        return Collections.unmodifiableList(monthlyInstallmentList);
    }

    public String getPrivacyPolicyRequirements() {
        return privacyPolicyRequirements;
    }

    @JsonProperty("mainShippingAddressSettingDisabled")
    public boolean isMainShippingAddressSettingDisabled() {
        return mainShippingAddressSettingDisabled;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public String getSubmallName() {
        return submallName;
    }

    public ProductDeliveryInfo getProductDeliveryInfo() {
        return this.productDeliveryInfo;
    }

    public String claimName() {
        return "checkoutInfo";
    }

    public void validRequired() throws Exception {
        if (productPrice <= 0) {
            throw new IllegalArgumentException("product price field couldn't be zero. check yours input value : " + productPrice);
        }
        if (productDeliveryInfo == null) {
            throw new IllegalArgumentException("you should contain ProductDeliveryInfo object.");
        }
        productDeliveryInfo.validRequired();

        for (Offer o : offerList) {
            o.validRequired();
        }
        for (Loyalty l : loyaltyList) {
            l.validRequired();
        }
        for (PayConfigurer.ShippingAddress a : shippingAddressList) {
            a.validRequiredToCheckout();
        }
        for (MonthlyInstallment m : monthlyInstallmentList) {
            m.validRequired();
        }
    }

    public OrderConfigurer<H> withPrivacyPolicyRequirements(String privacyPolicyRequirements) {
        this.privacyPolicyRequirements = privacyPolicyRequirements;
        return this;
    }

    public OrderConfigurer<H> enableMainShippingAddressSetting() {
        this.mainShippingAddressSettingDisabled = true;
        return this;
    }

    public OrderConfigurer<H> withShippingAddresses(PayConfigurer.ShippingAddress... shippingAddress) {
        return withShippingAddresses(Arrays.asList(shippingAddress));
    }

    public OrderConfigurer<H> withShippingAddresses(List<PayConfigurer.ShippingAddress> shippingAddresses) {
        for (PayConfigurer.ShippingAddress a : shippingAddresses) {
            a.validRequiredToCheckout();
        }
        shippingAddressList.addAll(shippingAddresses);
        return this;
    }

    public OrderConfigurer<H> withProductPrice(int productPrice) {
        if (productPrice <= 0) {
            throw new IllegalArgumentException("Cannot be smaller than 0. Check yours input value : " + productPrice);
        }
        this.productPrice = productPrice;
        return this;
    }

    public OrderConfigurer<H> withSubmallName(String submallName) {
        this.submallName = submallName;
        return this;
    }

    public OrderConfigurer<H> withProductDeliveryInfo(ProductDeliveryInfo productDeliveryInfo) {
        this.productDeliveryInfo = productDeliveryInfo;
        return this;
    }

    public OrderConfigurer<H> withOffers(Offer... offer) {
        return withOffers(Arrays.asList(offer));
    }

    public OrderConfigurer<H> withOffers(List<Offer> offers) {
        for (Offer o : offers) {
            o.validRequired();
        }
        this.offerList.addAll(offers);
        return this;
    }

    public OrderConfigurer<H> withLoyalties(Loyalty... loyalty) {
        return withLoyalties(Arrays.asList(loyalty));
    }

    public OrderConfigurer<H> withLoyalties(List<Loyalty> loyalties) {
        for (Loyalty l : loyalties) {
            l.validRequired();
        }
        this.loyaltyList.addAll(loyalties);
        return this;
    }

    public OrderConfigurer<H> withMonthlyInstallment(MonthlyInstallment... monthlyInstallment) {
        return withMonthlyInstallment(Arrays.asList(monthlyInstallment));
    }

    public OrderConfigurer<H> withMonthlyInstallment(List<MonthlyInstallment> monthlyInstallments) {
        for (MonthlyInstallment s : monthlyInstallments) {
            s.validRequired();
        }
        this.monthlyInstallmentList.addAll(monthlyInstallments);
        return this;
    }

    public List<Offer> getOfferList() {
        return Collections.unmodifiableList(offerList);
    }

    public List<Loyalty> getLoyaltyList() {
        return Collections.unmodifiableList(loyaltyList);
    }

    public List<PayConfigurer.ShippingAddress> getShippingAddressList() {
        return Collections.unmodifiableList(shippingAddressList);
    }

    public static enum LoyaltyId {
        POINT_OF_11ST("www.11st.co.kr:point"), MILEAGE_OF_11ST("www.11st.co.kr:mileage"), T_MEMBERSHIP("www.sktmemebership.co.kr"), OK_CASHBAG("www.okcashbag.com");

        private String urn;

        LoyaltyId(String urn) {
            this.urn = urn;
        }

        public String getUrn() {
            return urn;
        }
    }

    public static enum ErrorType {
        MAINTENACE, SYSTEM_ERR;
    }

    public static enum DeliveryRestriction {
        NOT_FAR_AWAY, FAR_AWAY, FAR_FAR_AWAY
    }

    public static enum OfferType {
        DELIVERY_COUPON
    }

    public static enum AcceptType {
        CARD, SYRUP_PAY_COUPON
    }

    public static enum DeliveryType {
        PREPAID, FREE, DIY, QUICK, PAYMENT_ON_DELIVERY
    }

    interface Element {
        public void validRequired();
    }

    public static final class Accept implements Serializable, Element {
        private static final long serialVersionUID = 3616983855074719924L;
        private AcceptType type;
        private List<Map<String, Object>> conditions = new ArrayList<Map<String, Object>>();

        public AcceptType getType() {
            return type;
        }

        public Accept setType(AcceptType type) {
            this.type = type;
            return this;
        }

        public List<Map<String, Object>> getConditions() {
            return Collections.unmodifiableList(conditions);
        }

        public Accept setConditions(List<Map<String, Object>> conditions) {
            this.conditions = conditions;
            return this;
        }

        public void validRequired() {
            if (type == null) {
                throw new IllegalArgumentException("Accept object couldn't be with null fields.");
            }

            if (conditions.isEmpty()) {
                throw new IllegalArgumentException("Conditions of Accept object couldn't be empty. you should contain with conditions of Accept object.");
            }
        }
    }

    public static final class ProductDeliveryInfo implements Serializable, Element {
        private static final long serialVersionUID = 2327522688494682416L;

        private DeliveryType deliveryType;
        private String deliveryName;
        private boolean defaultDeliveryCostApplied;
        private boolean additionalDeliveryCostApplied;
        private boolean shippingAddressDisplay;

        public boolean isShippingAddressDisplay() {
            return shippingAddressDisplay;
        }

        public ProductDeliveryInfo setShippingAddressDisplay(boolean shippingAddressDisplay) {
            this.shippingAddressDisplay = shippingAddressDisplay;
            return this;
        }

        public DeliveryType getDeliveryType() {
            return deliveryType;
        }

        public ProductDeliveryInfo setDeliveryType(DeliveryType deliveryType) {
            this.deliveryType = deliveryType;
            return this;
        }

        public String getDeliveryName() {
            return deliveryName;
        }

        public ProductDeliveryInfo setDeliveryName(String deliveryName) {
            this.deliveryName = deliveryName;
            return this;
        }

        public boolean isDefaultDeliveryCostApplied() {
            return defaultDeliveryCostApplied;
        }

        public ProductDeliveryInfo setDefaultDeliveryCostApplied(boolean defaultDeliveryCostApplied) {
            this.defaultDeliveryCostApplied = defaultDeliveryCostApplied;
            return this;
        }

        public boolean isAdditionalDeliveryCostApplied() {
            return additionalDeliveryCostApplied;
        }

        public ProductDeliveryInfo setAdditionalDeliveryCostApplied(boolean additionalDeliveryCostApplied) {
            this.additionalDeliveryCostApplied = additionalDeliveryCostApplied;
            return this;
        }

        public void validRequired() {
            if (deliveryType == null || deliveryName == null) {
                throw new IllegalArgumentException("ProductDeliveryInfo object couldn't be with null fields. deliveryType : " + deliveryType + ", deliveryName : " + deliveryName);
            }
        }
    }

    public static final class Offer implements Serializable, Element {
        private static final long serialVersionUID = 5714765820164415055L;
        private String id;
        private String userActionCode;
        private OfferType type;
        private String name;
        private int amountOff;
        private boolean userSelectable;
        private int orderApplied;
        private String exclusiveGroupId;
        private String exclusiveGroupName;
        private List<Accept> accepted = new ArrayList<Accept>();

        public String getUserActionCode() {
            return userActionCode;
        }

        public Offer setUserActionCode(String userActionCode) {
            this.userActionCode = userActionCode;
            return this;
        }

        public String getExclusiveGroupId() {
            return exclusiveGroupId;
        }

        public Offer setExclusiveGroupId(String exclusiveGroupId) {
            this.exclusiveGroupId = exclusiveGroupId;
            return this;
        }

        public String getExclusiveGroupName() {
            return exclusiveGroupName;
        }

        public Offer setExclusiveGroupName(String exclusiveGroupName) {
            this.exclusiveGroupName = exclusiveGroupName;
            return this;
        }

        public String getId() {
            return id;
        }

        public Offer setId(String id) {
            this.id = id;
            return this;
        }

        public OfferType getType() {
            return type;
        }

        public Offer setType(OfferType type) {
            this.type = type;
            return this;
        }

        public String getName() {
            return name;
        }

        public Offer setName(String name) {
            this.name = name;
            return this;
        }

        public int getAmountOff() {
            return amountOff;
        }

        public Offer setAmountOff(int amountOff) {
            if (amountOff <= 0) {
                throw new IllegalArgumentException("amountOff should be bigger than 0. yours : " + amountOff);
            }
            this.amountOff = amountOff;
            return this;
        }

        public boolean isUserSelectable() {
            return userSelectable;
        }

        public Offer setUserSelectable(boolean userSelectable) {
            this.userSelectable = userSelectable;
            return this;
        }

        public int getOrderApplied() {
            return orderApplied;
        }

        public Offer setOrderApplied(int orderApplied) {
            this.orderApplied = orderApplied;
            return this;
        }

        public List<Accept> getAccepted() {
            return Collections.unmodifiableList(accepted);
        }

        public Offer setAccepted(List<Accept> accepted) {
            this.accepted = accepted;
            return this;
        }

        public void validRequired() {
            if (id == null || name == null) {
                throw new IllegalArgumentException("Offer object couldn't be with null fields id : " + id + ", name : " + name);
            }
            if (amountOff <= 0) {
                throw new IllegalArgumentException("amountOff field should be bigger than 0. yours amountOff is : " + amountOff);
            }
        }
    }

    public static class AdditionalDiscount implements Serializable, Element {
        private static final long serialVersionUID = 8516378683113314131L;
        private int percentOff;
        private int maxApplicableAmt;

        public int getPercentOff() {
            return percentOff;
        }

        public AdditionalDiscount setPercentOff(int percentOff) {
            if (percentOff <= 0) {
                throw new IllegalArgumentException("percentOff field should be bigger than 0. yours percentOff is : " + percentOff);
            }
            this.percentOff = percentOff;
            return this;
        }

        public int getMaxApplicableAmt() {
            return maxApplicableAmt;
        }

        public AdditionalDiscount setMaxApplicableAmt(int maxApplicableAmt) {
            if (maxApplicableAmt <= 0) {
                throw new IllegalArgumentException("maxApplicableAmt field should be bigger than 0. yours maxApplicableAmt is : " + maxApplicableAmt);
            }
            this.maxApplicableAmt = maxApplicableAmt;
            return this;
        }

        public void validRequired() {
            if (percentOff <= 0) {
                throw new IllegalArgumentException("percentOff field should be bigger than 0. yours percentOff is : " + percentOff);
            }
            if (maxApplicableAmt <= 0) {
                throw new IllegalArgumentException("maxApplicableAmt field should be bigger than 0. yours maxApplicableAmt is : " + maxApplicableAmt);
            }
        }
    }

    public static class Error implements Serializable, Element {
        private static final long serialVersionUID = 6825185155054570965L;
        private ErrorType type;
        private String description;

        public ErrorType getType() {
            return type;
        }

        public Error setType(ErrorType type) {
            this.type = type;
            return this;
        }

        public String getDescription() {
            return description;
        }

        public Error setDescription(String description) {
            this.description = description;
            return this;
        }

        public void validRequired() {
            if (type == null || description == null) {
                throw new IllegalArgumentException("Error object couldn't be with null fields type : " + type + ", description : " + description);
            }
        }
    }

    public static final class Loyalty implements Serializable, Element {
        private static final long serialVersionUID = -7486686462214115342L;
        private String id;
        private String userActionCode;
        private String name;
        private String subscriberId;
        private int balance;
        private int maxApplicableAmt;
        private int initialAppliedAmt;
        private int orderApplied;
        private AdditionalDiscount additionalDiscount;
        private Error error;
        private String exclusiveGroupId;
        private String exclusiveGroupName;

        public String getUserActionCode() {
            return userActionCode;
        }

        public Loyalty setUserActionCode(String userActionCode) {
            this.userActionCode = userActionCode;
            return this;
        }

        public String getExclusiveGroupId() {
            return exclusiveGroupId;
        }

        public Loyalty setExclusiveGroupId(String exclusiveGroupId) {
            this.exclusiveGroupId = exclusiveGroupId;
            return this;
        }

        public String getExclusiveGroupName() {
            return exclusiveGroupName;
        }

        public Loyalty setExclusiveGroupName(String exclusiveGroupName) {
            this.exclusiveGroupName = exclusiveGroupName;
            return this;
        }

        public String getId() {
            return id;
        }

        public Loyalty setId(String id) {
            this.id = id;
            return this;
        }

        public Loyalty setIdBy(LoyaltyId loyaltyId) {
            this.id = loyaltyId.getUrn();
            return this;
        }

        public String getName() {
            return name;
        }

        public Loyalty setName(String name) {
            this.name = name;
            return this;
        }

        public String getSubscriberId() {
            return subscriberId;
        }

        public Loyalty setSubscriberId(String subscriberId) {
            this.subscriberId = subscriberId;
            return this;
        }

        public int getBalance() {
            return balance;
        }

        public Loyalty setBalance(int balance) {
            if (balance <= 0) {
                throw new IllegalArgumentException("balance field should be bigger than 0. yours balance is : " + balance);
            }
            this.balance = balance;
            return this;
        }

        public int getMaxApplicableAmt() {
            return maxApplicableAmt;
        }

        public Loyalty setMaxApplicableAmt(int maxApplicableAmt) {
            if (maxApplicableAmt <= 0) {
                throw new IllegalArgumentException("maxApplicableAmt field should be bigger than 0. yours maxApplicableAmt is : " + maxApplicableAmt);
            }
            this.maxApplicableAmt = maxApplicableAmt;
            return this;
        }

        public int getInitialAppliedAmt() {
            return initialAppliedAmt;
        }

        public Loyalty setInitialAppliedAmt(int initialAppliedAmt) {
            this.initialAppliedAmt = initialAppliedAmt;
            return this;
        }

        public int getOrderApplied() {
            return orderApplied;
        }

        public Loyalty setOrderApplied(int orderApplied) {
            this.orderApplied = orderApplied;
            return this;
        }

        public AdditionalDiscount getAdditionalDiscount() {
            return additionalDiscount;
        }

        public Loyalty setAdditionalDiscount(AdditionalDiscount additionalDiscount) {
            this.additionalDiscount = additionalDiscount;
            return this;

        }

        public Error getError() {
            return error;
        }

        public Loyalty setError(Error error) {
            this.error = error;
            return this;
        }

        public void validRequired() {
            if (id == null || name == null || subscriberId == null) {
                throw new IllegalArgumentException("Loyalty object couldn't be with null fields id : " + id + ", name : " + name + ", subscriberId : " + subscriberId);
            }

            if (additionalDiscount != null) {
                additionalDiscount.validRequired();
            }

            if (error != null) {
                error.validRequired();
            }

            if (balance <= 0) {
                throw new IllegalArgumentException("balance field should be bigger than 0. yours balance is : " + balance);
            }
            if (maxApplicableAmt <= 0) {
                throw new IllegalArgumentException("maxApplicableAmt field should be bigger than 0. yours maxApplicableAmt is : " + maxApplicableAmt);
            }
        }
    }

    public static class MonthlyInstallment implements Serializable, Element {
        private static final long serialVersionUID = 503292756528706429L;
        private String cardCode;
        private List<Map<String, Object>> conditions = new ArrayList<Map<String, Object>>();

        public String getCardCode() {
            return cardCode;
        }

        public MonthlyInstallment setCardCode(String cardCode) {
            this.cardCode = cardCode;
            return this;
        }

        public List<Map<String, Object>> getConditions() {
            return Collections.unmodifiableList(conditions);
        }

        public MonthlyInstallment addCondition(int min, boolean includeMin, int max, boolean includeMax, String monthlyInstallmentInfo) {
            Map<String, Object> m = new HashMap<String, Object>();
            m.put("paymentAmtRange", (includeMin ? "[" : "(") + min + "-" + max + (includeMax ? "]" : ")"));
            m.put("monthlyInstallmentInfo", monthlyInstallmentInfo);
            this.conditions.add(m);
            return this;
        }

        public MonthlyInstallment addCondition(int min, boolean includeMin, String monthlyInstallmentInfo) {
            Map<String, Object> m = new HashMap<String, Object>();
            m.put("paymentAmtRange", (includeMin ? "[" : "(") + min + "-]");
            m.put("monthlyInstallmentInfo", monthlyInstallmentInfo);
            this.conditions.add(m);
            return this;
        }

        public void validRequired() {
            if (cardCode == null) {
                throw new IllegalArgumentException("MonthlyInstallment object couldn't be with null fields cardCode is null");
            }

            if (conditions.isEmpty()) {
                throw new IllegalArgumentException("Conditions of MonthlyInstallment object couldn't be empty. you should contain with conditions of MonthlyInstallment object by addCondition method.");
            }
        }
    }
}
