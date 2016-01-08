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
import com.skplanet.syruppay.client.event.error.SyrupPayError;

import java.io.Serializable;

/**
 * 기 가입회원에 대한 Sso Credential 조회 DTO
 *
 * @author 임형태
 * @since 2015.12.02
 */
public class GetSsoCredentialEvent implements Serializable {
    private static final long serialVersionUID = 2914768354789305631L;

    public static class RequestGettingSso extends GetSsoCredentialEvent {
        private String iss;
        private SsoIdentifier ssoIdentifier;
        private SsoMappingType ssoMappingType;
        private String ssoMappingValue;

        public String getIss() {
            return iss;
        }

        public RequestGettingSso setIss(String iss) {
            this.iss = iss;
            return this;
        }

        public SsoIdentifier getSsoIdentifier() {
            return ssoIdentifier;
        }

        public RequestGettingSso setSsoIdentifier(SsoIdentifier ssoIdentifier) {
            this.ssoIdentifier = ssoIdentifier;
            return this;
        }

        public SsoMappingType getSsoMappingType() {
            return ssoMappingType;
        }

        public RequestGettingSso setSsoMappingType(SsoMappingType ssoMappingType) {
            this.ssoMappingType = ssoMappingType;
            return this;
        }

        public String getSsoMappingValue() {
            return ssoMappingValue;
        }

        public RequestGettingSso setSsoMappingValue(String ssoMappingValue) {
            this.ssoMappingValue = ssoMappingValue;
            return this;
        }
    }

    public static class SsoIdentifier {
        private String mctUserId;
        private String extraUserId;
        private String connectingInfo;

        @Deprecated
        public String getMctUserId() {
            return mctUserId;
        }

        @Deprecated
        public String getExtraUserId() {
            return extraUserId;
        }

        @JsonIgnore
        public String getUserIdOfMerchant() {
            return mctUserId;
        }

        @JsonIgnore
        public String getExtraUserIdOfMerchant() {
            return extraUserId;
        }

        public String getConnectingInfo() {
            return connectingInfo;
        }

        @JsonIgnore
        public SsoIdentifier setUserIdOfMerchant(String userIdOfMerchant) {
            this.mctUserId = userIdOfMerchant;
            return this;
        }

        @JsonIgnore
        public SsoIdentifier setExtraUserIdOfMerchant(String extraUserIdOfMerchant) {
            this.extraUserId = extraUserIdOfMerchant;
            return this;
        }

        public SsoIdentifier setConnectingInfo(String connectingInfo) {
            this.connectingInfo = connectingInfo;
            return this;
        }
    }

    public static enum SsoMappingType {
        CI_HASH, CI_MAPPED_KEY
    }

    public static class ResponseGettingSso extends GetSsoCredentialEvent {
        private String userSerialNumber;
        private String ssoCredential;
        private SyrupPayError syrupPayError;

        public SyrupPayError getSyrupPayError() {
            return syrupPayError;
        }

        public String getUserSerialNumber() {
            return userSerialNumber;
        }

        public String getSsoCredential() {
            return ssoCredential;
        }
    }

}
