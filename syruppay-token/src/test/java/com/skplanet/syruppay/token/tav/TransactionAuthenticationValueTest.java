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

import com.skplanet.syruppay.token.tav.mocks.TransactionAuthentcationValueMock;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.*;

public class TransactionAuthenticationValueTest {
    TransactionAuthenticationValue transactionAuthenticationValue;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testGetChecksumBy() throws Exception {
        // Give
        transactionAuthenticationValue = TransactionAuthentcationValueMock.getTransactionAuthenticationValue();

        // When
        String cs = transactionAuthenticationValue.getChecksumBy("KEY");

        // Then
        assertThat(cs, is(notNullValue()));
        assertThat(cs.length(), is(greaterThan(0)));
    }

    @Test
    public void testIsValidBy() throws Exception {
        // Give
        transactionAuthenticationValue = TransactionAuthentcationValueMock.getTransactionAuthenticationValue();

        // When
        String cs = transactionAuthenticationValue.getChecksumBy("KEY");
        boolean b = transactionAuthenticationValue.isValidBy("KEY", cs);

        // Then
        assertThat(b, is(notNullValue()));
        assertThat(b, is(true));
    }

    @Test
    public void testIsValidBy_잘못된_체크섬() throws Exception {
        // Give
        transactionAuthenticationValue = TransactionAuthentcationValueMock.getTransactionAuthenticationValue();

        // When
        boolean b = transactionAuthenticationValue.isValidBy("KEY", "WRONG CHECKSUM");

        // Then
        assertThat(b, is(notNullValue()));
        assertThat(b, is(false));
    }

    @Test
    public void testIsValidBy_잘못된_키() throws Exception {
        // Give
        transactionAuthenticationValue = TransactionAuthentcationValueMock.getTransactionAuthenticationValue();

        // When
        // When
        String cs = transactionAuthenticationValue.getChecksumBy("KEY");
        boolean b = transactionAuthenticationValue.isValidBy("WRONG_KEY", cs);

        // Then
        assertThat(b, is(notNullValue()));
        assertThat(b, is(false));
    }

    @Test
    public void testIsValidBy_잘못된_키와_잘못된_체크섬() throws Exception {
        // Give
        transactionAuthenticationValue = TransactionAuthentcationValueMock.getTransactionAuthenticationValue();

        // When
        // When
        boolean b = transactionAuthenticationValue.isValidBy("WRONG_KEY", "WRONG CHECKSUM");

        // Then
        assertThat(b, is(notNullValue()));
        assertThat(b, is(false));
    }
}