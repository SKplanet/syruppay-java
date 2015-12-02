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

package com.skplanet.syruppay.client.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skplanet.syruppay.client.event.error.SyrupPayError;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * 거래 승인 요청와 응답 DTO
 *
 * @author 임형태
 * @since 0.1
 */
@XmlRootElement
public class ApproveEvent implements Serializable {
    private static final long serialVersionUID = -8558152399133908526L;
    private String mctRequestId;
    private String mctTransAuthId;
    private String ocTransAuthId;

    @Deprecated
    public String getMctTransAuthId() {
        return mctTransAuthId;
    }

    @Deprecated
    public String getMctRequestId() {
        return mctRequestId;
    }

    @Deprecated
    public String getOcTransAuthId() {
        return ocTransAuthId;
    }

    @JsonIgnore
    public String getRequestIdOfMerchant() {
        return mctRequestId;
    }

    @JsonIgnore
    public String getOrderIdOfMerchant() {
        return mctTransAuthId;
    }

    @JsonIgnore
    public String getTransactionIdOfOneClick() {
        return ocTransAuthId;
    }

    @JsonIgnore
    public ApproveEvent setRequestIdOfMerchant(String requestIdOfMerchant) {
        this.mctRequestId = requestIdOfMerchant;
        return this;
    }

    @JsonIgnore
    public ApproveEvent setOrderIdOfMerchant(String orderIdOfMerchant) {
        this.mctTransAuthId = orderIdOfMerchant;
        return this;
    }

    @JsonIgnore
    public ApproveEvent setTransactionIdOfOneClick(String transactionIdOfOneClick) {
        this.ocTransAuthId = transactionIdOfOneClick;
        return this;
    }

    @XmlRootElement
    public static class RequestApprove extends ApproveEvent {
        private static final long serialVersionUID = 324247355311107194L;
        private long mctRequestTime;
        private int paymentAmt;
        private int taxFreeAmt;
        private String tranAuthValue;
        private SubmallInfo submallInfo;

        @Deprecated
        public long getMctRequestTime() {
            return mctRequestTime;
        }

        @Deprecated
        public int getPaymentAmt() {
            return paymentAmt;
        }

        @Deprecated
        public int getTaxFreeAmt() {
            return taxFreeAmt;
        }

        @Deprecated
        public String getTranAuthValue() {
            return tranAuthValue;
        }

        @JsonIgnore
        public long getRequestTimeOfMerchant() {
            return mctRequestTime;
        }

        @JsonIgnore
        public int getPaymentAmount() {
            return paymentAmt;
        }

        @JsonIgnore
        public int getTaxFreeAmount() {
            return taxFreeAmt;
        }

        @JsonIgnore
        public String getTransactionAuthenticationValue() {
            return tranAuthValue;
        }

        public SubmallInfo getSubmallInfo() {
            return submallInfo;
        }

        @JsonIgnore
        public RequestApprove setRequestTimeOfMerchant(long requestTimeOfMerchant) {
            mctRequestTime = requestTimeOfMerchant;
            return this;
        }

        @JsonIgnore
        public RequestApprove setPaymentAmount(int paymentAmount) {
            paymentAmt = paymentAmount;
            return this;
        }

        @JsonIgnore
        public RequestApprove setTaxFreeAmount(int taxFreeAmount) {
            taxFreeAmt = taxFreeAmount;
            return this;
        }

        @JsonIgnore
        public RequestApprove setTransactionAuthenticationValue(String transactionAuthenticationValue) {
            tranAuthValue = transactionAuthenticationValue;
            return this;
        }

        public RequestApprove setSubmallInfo(SubmallInfo submallInfo) {
            this.submallInfo = submallInfo;
            return this;
        }
    }

    @XmlRootElement
    public static class SubmallInfo implements Serializable {
        private static final long serialVersionUID = 7388439447463148569L;

        private String id;
        private String officeName;
        private String businessRegNumber;
        private String lineNumber;
        private String address;
        private String owner;

        public void setId(String id) {
            this.id = id;
        }

        public void setOfficeName(String officeName) {
            this.officeName = officeName;
        }

        public void setBusinessRegNumber(String businessRegNumber) {
            this.businessRegNumber = businessRegNumber;
        }

        public void setLineNumber(String lineNumber) {
            this.lineNumber = lineNumber;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }
    }

    @XmlRootElement
    public static class ResponseApprove extends ApproveEvent {
        private String ocTransApproveNo;
        private long ocTransApproveTime;
        private PaymentInfo paymentInfo;
        private List<DiscountInfo> discountInfoList;
        private CardApprovalInfo cardApprovalInfo;
        private BankApprovalInfo bankApprovalInfo;
        private SyrupPayError syrupPayError;

        public SyrupPayError getSyrupPayError() {
            return syrupPayError;
        }

        public void setSyrupPayError(SyrupPayError syrupPayError) {
            this.syrupPayError = syrupPayError;
        }

        public String getOcTransApproveNo() {
            return ocTransApproveNo;
        }

        public long getOcTransApproveTime() {
            return ocTransApproveTime;
        }

        public PaymentInfo getPaymentInfo() {
            return paymentInfo;
        }

        public List<DiscountInfo> getDiscountInfoList() {
            return discountInfoList;
        }

        public CardApprovalInfo getCardApprovalInfo() {
            return cardApprovalInfo;
        }

        public BankApprovalInfo getBankApprovalInfo() {
            return bankApprovalInfo;
        }
    }

    public static class PaymentInfo {
        private String productTitle;
        private String currencyCode;
        private int productAmt;
        private int paymentAmt;
        private int discountAmt;
        private PaymentMethod paymentMethod;

        public String getProductTitle() {
            return productTitle;
        }

        public String getCurrencyCode() {
            return currencyCode;
        }

        public int getProductAmt() {
            return productAmt;
        }

        public int getPaymentAmt() {
            return paymentAmt;
        }

        public int getDiscountAmt() {
            return discountAmt;
        }

        public PaymentMethod getPaymentMethod() {
            return paymentMethod;
        }
    }

    public static enum PaymentMethod {
        CARD, BANK
    }

    public static enum DiscountMethod {
        COUPON, OCB, T_MEMBERSHIP
    }

    public static class DiscountInfo {
        private DiscountMethod discountMethod;
        private int discountAmt;
        private String discountCode;
        private String discountName;

        public DiscountMethod getDiscountMethod() {
            return discountMethod;
        }

        public int getDiscountAmt() {
            return discountAmt;
        }

        public String getDiscountCode() {
            return discountCode;
        }

        public String getDiscountName() {
            return discountName;
        }
    }

    public static class CardApprovalInfo {
        private String cardNumber;
        private String cardName;
        private CardType cardType;
        private String cardApprovalNo;
        private String cardApprovalTime;
        private boolean cardPointApplied;
        private int payInstallment;

        public String getCardNumber() {
            return cardNumber;
        }

        public String getCardName() {
            return cardName;
        }

        public CardType getCardType() {
            return cardType;
        }

        public String getCardApprovalNo() {
            return cardApprovalNo;
        }

        public String getCardApprovalTime() {
            return cardApprovalTime;
        }

        public boolean isCardPointApplied() {
            return cardPointApplied;
        }

        public int getPayInstallment() {
            return payInstallment;
        }
    }

    public static class BankApprovalInfo {
        private String bankAccountNumber;
        private String bankName;
        private String bankApprovalNo;
        private String bankApprovalTime;

        public String getBankAccountNumber() {
            return bankAccountNumber;
        }

        public String getBankName() {
            return bankName;
        }

        public String getBankApprovalNo() {
            return bankApprovalNo;
        }

        public String getBankApprovalTime() {
            return bankApprovalTime;
        }
    }

    public static enum CardType {
        CC, CH
    }
}
