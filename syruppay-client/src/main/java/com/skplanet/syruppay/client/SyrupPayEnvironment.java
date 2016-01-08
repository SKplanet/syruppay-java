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

package com.skplanet.syruppay.client;

/**
 * 시럽페이 인프라에 대한 환경을 정의한다.
 *
 * @author 임형태
 * @since 2015.12.02
 */
public final class SyrupPayEnvironment {
    private final String authenticationServerUrl;
    private final String transactionServerUrl;

    public static final SyrupPayEnvironment LOCALHOST = new SyrupPayEnvironment("http://localhost:8080", "http://localhost:8080");
    public static final SyrupPayEnvironment DEVELOPMENT = new SyrupPayEnvironment("https://devpay.syrup.co.kr", "https://api-devpay.syrup.co.kr");
    public static final SyrupPayEnvironment STAGING_DEVELOPMENT = new SyrupPayEnvironment("https://devqapay.syrup.co.kr", "https://api-devqapay.syrup.co.kr");
    public static final SyrupPayEnvironment STAGING_SERVICE = new SyrupPayEnvironment("https://stgpay.syrup.co.kr", "https://api-stgpay.syrup.co.kr");
    public static final SyrupPayEnvironment SERVICE = new SyrupPayEnvironment("https://pay.syrup.co.kr", "https://api-pay.syrup.co.kr");

    private SyrupPayEnvironment(String authenticationServerUrl, String transactionServerUrl) {
        this.authenticationServerUrl = authenticationServerUrl;
        this.transactionServerUrl = transactionServerUrl;
    }

    public String getAuthenticationServerUrl() {
        return authenticationServerUrl;
    }

    public String getTransactionServerUrl() {
        return transactionServerUrl;
    }
}
