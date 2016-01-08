/*
 * Copyright (c) 2015 SK PLANET. ALL Rights Reserved.
 *
 * Syrup Pay Client Library
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.skplanet.syruppay.client.event;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * @author 임형태
 * @since 2015.12.02
 */
public class RefundEvent implements Serializable {
    private static final long serialVersionUID = 5454969987099645585L;
    private String ocTransApproveNo;

    public String getOcTransApproveNo() {
        return ocTransApproveNo;
    }

    public RefundEvent setOcTransApproveNo(String ocTransApproveNo) {
        this.ocTransApproveNo = ocTransApproveNo;
        return this;
    }

    public static class RequestRefund extends RefundEvent {
        private String mctRequestId;
        private long mctRequestTime;
        private RefundType refundType;
        private int refundPaymentAmt;
        private int refundTaxFreeAmt;

        @Deprecated
        public String getMctRequestId() {
            return mctRequestId;
        }

        @JsonIgnore
        public String getRequestIdIfMerchant() {
            return mctRequestId;
        }

        @JsonIgnore
        public RequestRefund setRequestIdIfMerchant(String requestIdIfMerchant) {
            this.mctRequestId = requestIdIfMerchant;
            return this;
        }

        @Deprecated
        public long getMctRequestTime() {
            return mctRequestTime;
        }

        @JsonIgnore
        public long getRequestTimeOfMerchant() {
            return mctRequestTime;
        }

        @JsonIgnore
        public RequestRefund setRequestTimeOfMerchant(long requestTimeOfMerchant) {
            this.mctRequestTime = requestTimeOfMerchant;
            return this;
        }

        public RefundType getRefundType() {
            return refundType;
        }

        public RequestRefund setRefundType(RefundType refundType) {
            this.refundType = refundType;
            return this;
        }

        @Deprecated
        public int getRefundPaymentAmt() {
            return refundPaymentAmt;
        }

        @JsonIgnore
        public int getRefundPaymentAmount() {
            return refundPaymentAmt;
        }

        @JsonIgnore
        public RequestRefund setRefundPaymentAmount(int refundPaymentAmount) {
            this.refundPaymentAmt = refundPaymentAmount;
            return this;
        }

        @Deprecated
        public int getRefundTaxFreeAmt() {
            return refundTaxFreeAmt;
        }

        @JsonIgnore
        public int getRefundTaxFreeAmount() {
            return refundTaxFreeAmt;
        }

        @JsonIgnore
        public RequestRefund setRefundTaxFreeAmount(int refundTaxFreeAmount) {
            this.refundTaxFreeAmt = refundTaxFreeAmount;
            return this;
        }
    }

    public static class ResponseRefund extends RefundEvent {
        private long ocTransApproveTime;
        private PaymentInfo paymentInfo;

        public long getOcTransApproveTime() {
            return ocTransApproveTime;
        }

        public PaymentInfo getPaymentInfo() {
            return paymentInfo;
        }
    }

    public static enum RefundType {
        PARTIAL, ALL
    }

    public static class PaymentInfo {
        private String productTitle;
        private CurrencyCode currencyCode;
        private int paymentAmt;
        private String paymentMethod;

        public String getProductTitle() {
            return productTitle;
        }

        public CurrencyCode getCurrencyCode() {
            return currencyCode;
        }

        public int getPaymentAmt() {
            return paymentAmt;
        }

        public String getPaymentMethod() {
            return paymentMethod;
        }
    }

    public static enum CurrencyCode {
        KRW, USD
    }
}
