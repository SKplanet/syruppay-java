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

package com.skplanet.syruppay.token.tav;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.ObjectMapper;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * 거래 인증에 대한 POJO를 정의하고 이에 대한 Signing 값을 제공하며 기존의 TAV 값으로 통용된다.<br>
 * 거래 인증에 대한 무결성은 HAMC으로 인증하며 이에 대한 검증은 해당 클래스에서 지원한다.
 *
 * @author 임형태
 * @since 1.3
 */
public class TransactionAuthenticationValue implements Serializable {
    private String cardToken;
    private String mctTransAuthId;
    private String ocTransAuthId;
    private PaymentAuthenticationDetail paymentAuthenticationDetail;

    public String getCardToken() {
        return cardToken;
    }

    @JsonIgnore
    public String getOrderIdOfMerchant() {
        return mctTransAuthId;
    }

    @Deprecated
    public String getMctTransAuthId() {
        return mctTransAuthId;
    }

    @Deprecated
    public String getOcTransAuthId() {
        return ocTransAuthId;
    }

    @JsonIgnore
    public String getTransactionIdOfOneClick() {
        return ocTransAuthId;
    }

    public PaymentAuthenticationDetail getPaymentAuthenticationDetail() {
        return paymentAuthenticationDetail;
    }

    public String getChecksumBy(final String key) throws IOException, InvalidKeyException, NoSuchAlgorithmException {
        final Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(key.getBytes("UTF-8"), mac.getAlgorithm()));
        mac.update((cardToken + mctTransAuthId + ocTransAuthId + new ObjectMapper().writeValueAsString(paymentAuthenticationDetail)).getBytes("UTF-8"));
        return Base64.encodeBase64URLSafeString(mac.doFinal());
    }

    public boolean isValidBy(final String key, final String checksum) throws NoSuchAlgorithmException, IOException, InvalidKeyException {
        final Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(key.getBytes("UTF-8"), mac.getAlgorithm()));
        mac.update((cardToken + mctTransAuthId + ocTransAuthId + new ObjectMapper().writeValueAsString(paymentAuthenticationDetail)).getBytes("UTF-8"));
        return Base64.encodeBase64URLSafeString(mac.doFinal()).equals(checksum);
    }

    public static class PaymentAuthenticationDetail implements Serializable {
        private String payMethod;
        private int payAmount;
        private int offerAmount;
        private int loyaltyAmount;
        private String payInstallment;
        private String payCurrency;
        private String payFinanceCode;
        private boolean isCardPointApplied;

        public String getPayMethod() {
            return payMethod;
        }

        public void setPayMethod(String payMethod) {
            this.payMethod = payMethod;
        }

        public int getPayAmount() {
            return payAmount;
        }

        public void setPayAmount(int payAmount) {
            this.payAmount = payAmount;
        }

        public int getOfferAmount() {
            return offerAmount;
        }

        public void setOfferAmount(int offerAmount) {
            this.offerAmount = offerAmount;
        }

        public int getLoyaltyAmount() {
            return loyaltyAmount;
        }

        public void setLoyaltyAmount(int loyaltyAmount) {
            this.loyaltyAmount = loyaltyAmount;
        }

        public String getPayInstallment() {
            return payInstallment;
        }

        public void setPayInstallment(String payInstallment) {
            this.payInstallment = payInstallment;
        }

        public String getPayCurrency() {
            return payCurrency;
        }

        public void setPayCurrency(String payCurrency) {
            this.payCurrency = payCurrency;
        }

        public String getPayFinanceCode() {
            return payFinanceCode;
        }

        public void setPayFinanceCode(String payFinanceCode) {
            this.payFinanceCode = payFinanceCode;
        }

        public boolean getIsCardPointApplied() {
            return isCardPointApplied;
        }

        public void setIsCardPointApplied(boolean isCardPointApplied) {
            this.isCardPointApplied = isCardPointApplied;
        }
    }
}