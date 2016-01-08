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

package com.skplanet.syruppay.client.mocks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skplanet.syruppay.client.event.ApproveEvent;

import java.io.IOException;

/**
 * @author 임형태
 * @since 2015.12.01
 */
public class ApproveMocks {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String approve = "{\n" +
            "  \"mctRequestId\":\"4e0f618e9603497f8aa40ec182c36b12\",\n" +
            "  \"mctRequestTime\":1448870110, \n" +
            "  \"mctTransAuthId\":\"68e4ec50-1cb6-4fcd-8615-a1c5f0615d68\", \n" +
            "  \"paymentAmt\":10000, \n" +
            "  \"taxFreeAmt\":0, \n" +
            "  \"ocTransAuthId\":\"TA20151130000000000020083\", \n" +
            "  \"tranAuthValue\":\"y7we9C6TA_k-nEiYGnkeCUN8INuVCeyNJWcxbNmaKSI\", \n" +
            "  \"submallInfo\":null\n" +
            "}\n";


    public static ApproveEvent.RequestApprove getRequestApprove() throws IOException {
        return OBJECT_MAPPER.readValue(approve, ApproveEvent.RequestApprove.class);
    }

    public static ApproveEvent.RequestApprove getRequestApproveForDoc() throws IOException {
        ApproveEvent.RequestApprove request = new ApproveEvent.RequestApprove();
        request.setRequestIdOfMerchant("가맹점 거래 승인요청 ID");
        request.setRequestTimeOfMerchant(1448870110);
        request.setOrderIdOfMerchant("가맹점 거래인증 ID");
        request.setPaymentAmount(10000);
        request.setTaxFreeAmount(0);
        request.setOcTransAuthId("TA20151130000000000020083");
        request.setTransactionAuthenticationValue("y7we9C6TA_k-nEiYGnkeCUN8INuVCeyNJWcxbNmaKSI");
        return request;
    }
}
