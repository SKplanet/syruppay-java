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
public class CancelEvent implements Serializable {
    private static final long serialVersionUID = -2981720138417285394L;

    public static class RequestCancel {
        private String mctRequestId;
        private long mctRequestTime;
        private String approvedMctRequestId;
        private long approvedMctRequestTime;

        @Deprecated
        public String getMctRequestId() {
            return mctRequestId;
        }

        @JsonIgnore
        public String getRequestIdIfMerchant() {
            return mctRequestId;
        }

        @JsonIgnore
        public RequestCancel setRequestIdIfMerchant(String requestIdIfMerchant) {
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
        public RequestCancel setRequestTimeOfMerchant(long requestTimeOfMerchant) {
            this.mctRequestTime = requestTimeOfMerchant;
            return this;
        }

        @Deprecated
        public String getApprovedMctRequestId() {
            return approvedMctRequestId;
        }

        @Deprecated
        public long getApprovedMctRequestTime() {
            return approvedMctRequestTime;
        }

        @JsonIgnore
        public String getApprovedRequestIdOfMerchant() {
            return this.approvedMctRequestId;
        }

        @JsonIgnore
        public long getApprovedRequestTimeOfMerchant() {
            return this.approvedMctRequestTime;
        }

        @JsonIgnore
        public RequestCancel setApprovedRequestIdOfMerchant(String approvedRequestIdOfMerchant) {
            this.approvedMctRequestId = approvedRequestIdOfMerchant;
            return this;
        }

        @JsonIgnore
        public RequestCancel setApprovedRequestTimeOfMerchant(long approvedRequestTimeOfMerchant) {
            this.approvedMctRequestTime = approvedRequestTimeOfMerchant;
            return this;
        }
    }

    public static class ResponseCancel extends RefundEvent.ResponseRefund {
    }
}
