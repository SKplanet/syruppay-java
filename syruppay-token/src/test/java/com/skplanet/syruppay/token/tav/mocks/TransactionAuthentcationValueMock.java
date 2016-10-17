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

package com.skplanet.syruppay.token.tav.mocks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skplanet.syruppay.token.tav.TransactionAuthenticationValue;

import java.io.IOException;

/**
 * @author 임형태
 * @since 1.3
 */
public class TransactionAuthentcationValueMock {
    private static ObjectMapper objectMapper = new ObjectMapper();
    public static String json = "{\n" +
            "\t\"cardToken\": \"dT2bT-P5dvK0-3zCi9VByf_SUsXxLEmITJGLsWm_oYE\",\n" +
            "\t\"mctTransAuthId\": \"0f2e781e-1d38-4766-a635-8d906d3fdff7\",\n" +
            "\t\"ocTransAuthId\": \"TA20151130000000000020008\",\n" +
            "\t\"paymentAuthenticationDetail\": {\n" +
            "\t\t\"payMethod\": \"10\",\n" +
            "\t\t\"payAmount\": 1000,\n" +
            "\t\t\"offerAmount\": 0,\n" +
            "\t\t\"loyaltyAmount\": 0,\n" +
            "\t\t\"payInstallment\": \"00\",\n" +
            "\t\t\"payCurrency\": \"KRW\",\n" +
            "\t\t\"payFinanceCode\": \"17\",\n" +
            "\t\t\"isCardPointApplied\": false\n" +
            "\t}\n" +
            "}";

    public static TransactionAuthenticationValue getTransactionAuthenticationValue() throws IOException {
        return objectMapper.readValue(json, TransactionAuthenticationValue.class);
    }
}
