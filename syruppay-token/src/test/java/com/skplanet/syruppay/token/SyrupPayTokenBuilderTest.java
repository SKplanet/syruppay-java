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

package com.skplanet.syruppay.token;

import com.skplanet.syruppay.token.claims.MapToSyrupPayUserConfigurer;
import com.skplanet.syruppay.token.claims.PayConfigurer;
import com.skplanet.syruppay.token.claims.SubscriptionConfigurer;
import com.skplanet.syruppay.token.domain.Mocks;
import com.skplanet.syruppay.token.domain.TokenHistories;
import com.skplanet.syruppay.token.jwt.SyrupPayToken;
import com.skplanet.syruppay.token.jwt.Token;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Calendar;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class SyrupPayTokenBuilderTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    SyrupPayTokenBuilder syrupPayTokenBuilder;

    @Before
    public void setUp() throws Exception {
        syrupPayTokenBuilder = new SyrupPayTokenBuilder();
    }

    @Test
    public void 기본생성자_테스트() throws Exception {
        // Give
        SyrupPayTokenBuilder t = new SyrupPayTokenBuilder();

        // When

        // Then
        assertThat(t, is(notNullValue()));
        assertThat(t.getClass().getName(), is(SyrupPayTokenBuilder.class.getName()));
    }

    @Test
    public void MctAccToken_형식으로_회원정보_없이_생성() throws Exception {
        // Give
        syrupPayTokenBuilder.of("가맹점");

        // When
        String s = syrupPayTokenBuilder.generateTokenBy("keys");

        // Then
        assertThat(s, is(notNullValue()));
        assertThat(s.isEmpty(), is(false));
    }

    @Test
    public void MctAccToken_형식으로_회원정보_포함하여_생성() throws Exception {
        // Give
        syrupPayTokenBuilder.of("가맹점").login().withMerchantUserId("가맹점의 회원 ID 또는 식별자").withExtraMerchantUserId("핸드폰과 같이 회원 별 추가 ID 체계가 존재할 경우 입력").withSsoCredential("SSO 를 발급 받았을 경우 입력");

        // When
        String s = syrupPayTokenBuilder.generateTokenBy("keys");

        // Then
        assertThat(s, is(notNullValue()));
        assertThat(s.isEmpty(), is(false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void iss_미입력_후_빌드() throws Exception {
        // Give
        SyrupPayTokenBuilder t = new SyrupPayTokenBuilder();

        // When
        t.build();

        // Then
    }

    @Test
    public void 유효시간_기본값으로_입력_후_10분_여부_검증() throws Exception {
        // Give
        syrupPayTokenBuilder.of("test");

        // When

        // Then
        Token t = OBJECT_MAPPER.readValue(syrupPayTokenBuilder.toJson(), SyrupPayToken.class);
        assertThat(t, is(notNullValue()));
        assertThat(t.getExp() - t.getIat(), is((long) (10 * 60)));
    }

    @Test
    public void 유효시간_60분으로_입력_후_검증() throws Exception {
        // Give
        syrupPayTokenBuilder.of("test");

        // When
        syrupPayTokenBuilder.expiredMinutes(60);

        // Then
        Token t = OBJECT_MAPPER.readValue(syrupPayTokenBuilder.toJson(), SyrupPayToken.class);
        assertThat(t, is(notNullValue()));
        assertThat(t.getExp() - t.getIat(), is((long) (60 * 60)));
    }

    @Test
    public void 유효시간_0분으로_입력_후_검증() throws Exception {
        // Give
        syrupPayTokenBuilder.of("test");

        // When
        syrupPayTokenBuilder.expiredMinutes(0);

        // Then
        Token t = OBJECT_MAPPER.readValue(syrupPayTokenBuilder.toJson(), SyrupPayToken.class);
        assertThat(t, is(notNullValue()));
        assertThat(t.getExp(), is(t.getIat()));
    }

    @Test
    public void 유효시간_마이너스_1분으로_입력_후_만료여부_검증() throws Exception {
        // Give
        syrupPayTokenBuilder.of("test");

        // When
        syrupPayTokenBuilder.expiredMinutes(-1);

        // Then
        Token t = OBJECT_MAPPER.readValue(syrupPayTokenBuilder.toJson(), SyrupPayToken.class);
        assertThat(t, is(notNullValue()));
        assertThat(t.isValidInTime(), is(false));
    }

    @Test
    public void 시럽페이_사용자_매칭_정보_입력() throws Exception {
        // Give
        // @formatter:off
        syrupPayTokenBuilder.of("가맹점")
                .login()
                    .withMerchantUserId("가맹점의 회원 ID 또는 식별자")
                    .withExtraMerchantUserId("핸드폰과 같이 회원 별 추가 ID 체계가 존재할 경우 입력")
                    .withSsoCredential("SSO 를 발급 받았을 경우 입력")
                .and()
                .mapToSyrupPayUser()
                    .withType(MapToSyrupPayUserConfigurer.MappingType.CI_MAPPED_KEY)
                    .withValue("4987234");
        // @formatter:on
        // When
        String t = syrupPayTokenBuilder.generateTokenBy("가맹점에게 전달한 비밀키");

        // Then
        assertThat(t, is(notNullValue()));
        assertThat(t.isEmpty(), is(false));
    }

    @Test(expected = AlreadyBuiltException.class)
    public void 이미_생성한_토큰빌드를_재활용() throws Exception {
        // Give
        syrupPayTokenBuilder.of("가맹점").generateTokenBy("가맹점에게 전달한 비밀키");

        // When
        syrupPayTokenBuilder.generateTokenBy("가망점에게 전달한 비밀키");

        // Then
        // throw exception
    }

    @Test
    public void 구매를_위한_토큰_생성() throws Exception {
        // Give
        // @formatter:off
        syrupPayTokenBuilder.of("가맹점")
                            .pay()
                                .withOrderIdOfMerchant("가맹점에서 관리하는 주문 ID")
                                .withProductTitle("제품명")
                                .withLanguageForDisplay(PayConfigurer.Language.KO)
                                .withAmount(50000)
                                .withCurrency(PayConfigurer.Currency.KRW)
                                .withShippingAddress(new PayConfigurer.ShippingAddress("137-332", "서초구 잠원동 하나아파트", "1동 1호", "서울", "", "KR"))
                                .withDeliveryPhoneNumber("01011112222")
                                .withDeliveryName("배송 수신자")
                                .withInstallmentPerCardInformation(new PayConfigurer.CardInstallmentInformation("카드구분 코드", "할부정보. ex. NN1;NN2;YY3;YY4;YY5;NH6"))
                                .withBeAbleToExchangeToCash(false)
                                .withPayableRuleWithCard(PayConfigurer.PayableLocaleRule.ONLY_ALLOWED_KOR)
                ;
        // @formatter:on
        // When
        String t = syrupPayTokenBuilder.generateTokenBy("가맹점에게 전달한 비밀키");

        // Then
        assertThat(t, is(notNullValue()));
        assertThat(t.isEmpty(), is(false));
    }

    @Test
    public void 토큰_복호화() throws Exception {
        // Given
        // @formatter:off
        String t = syrupPayTokenBuilder.of("가맹점")
                .login()
                    .withMerchantUserId("가맹점의 회원 ID 또는 식별자")
                    .withExtraMerchantUserId("핸드폰과 같이 회원 별 추가 ID 체계가 존재할 경우 입력")
                    .withSsoCredential("SSO 를 발급 받았을 경우 입력")
                .and()
                .generateTokenBy("가맹점에게 전달한 비밀키");
        // @formatter:on

        // When
        Token token = SyrupPayTokenBuilder.verify(t, "가맹점에게 전달한 비밀키");

        // Then
        assertThat(token, is(notNullValue()));
        assertThat(token.isValidInTime(), is(true));
        assertThat(token.getIss(), is("가맹점"));
    }


    @Test(expected = IllegalArgumentException.class)
    public void 가맹점_회원ID_미입력_후_토큰_생성() throws Exception {
        // Given
        syrupPayTokenBuilder.of("가맹점").login().withSsoCredential("SSO 를 발급 받았을 경우 입력");

        // When
        String token = syrupPayTokenBuilder.generateTokenBy("가맹점에게 전달한 비밀키");

        // Then
        // throw Exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void 구매_시_구매_상품명_미입력_후_토큰_생성() throws Exception {
        // @formatter:off
        syrupPayTokenBuilder.of("가맹점")
                            .pay()
                                .withOrderIdOfMerchant("가맹점에서 관리하는 주문 ID")
                                //.withProductTitle("제품명")
                                .withLanguageForDisplay(PayConfigurer.Language.KO)
                                .withAmount(50000)
                                .withCurrency(PayConfigurer.Currency.KRW)
                                .withShippingAddress(new PayConfigurer.ShippingAddress("137-332", "서초구 잠원동 하나아파트", "1동 1호", "서울", "", "KR"))
                                .withDeliveryPhoneNumber("01011112222")
                                .withDeliveryName("배송 수신자")
                                .withInstallmentPerCardInformation(new PayConfigurer.CardInstallmentInformation("카드구분 코드", "할부정보. ex. NN1;NN2;YY3;YY4;YY5;NH6"))
                                .withBeAbleToExchangeToCash(false)
                                .withPayableRuleWithCard(PayConfigurer.PayableLocaleRule.ONLY_ALLOWED_KOR)
                ;
        // @formatter:on
        // When
        String t = syrupPayTokenBuilder.generateTokenBy("가맹점에게 전달한 비밀키");

        // Then
        // throw Exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void 구매_시_구매금액_미입력_후_토큰_생성() throws Exception {
        // @formatter:off
        syrupPayTokenBuilder.of("가맹점")
                            .pay()
                                .withOrderIdOfMerchant("가맹점에서 관리하는 주문 ID")
                                .withProductTitle("제품명")
                                .withLanguageForDisplay(PayConfigurer.Language.KO)
                                //.withAmount(50000)
                                .withCurrency(PayConfigurer.Currency.KRW)
                                .withShippingAddress(new PayConfigurer.ShippingAddress("137-332", "서초구 잠원동 하나아파트", "1동 1호", "서울", "", "KR"))
                                .withDeliveryPhoneNumber("01011112222")
                                .withDeliveryName("배송 수신자")
                                .withInstallmentPerCardInformation(new PayConfigurer.CardInstallmentInformation("카드구분 코드", "할부정보. ex. NN1;NN2;YY3;YY4;YY5;NH6"))
                                .withBeAbleToExchangeToCash(false)
                                .withPayableRuleWithCard(PayConfigurer.PayableLocaleRule.ONLY_ALLOWED_KOR)
                ;
        // @formatter:on
        // When
        String t = syrupPayTokenBuilder.generateTokenBy("가맹점에게 전달한 비밀키");

        // Then
        // throw Exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void 구매_시_구매금액_마이너스_입력_후_토큰_생성() throws Exception {
        // @formatter:off
        syrupPayTokenBuilder.of("가맹점")
                            .pay()
                                .withOrderIdOfMerchant("가맹점에서 관리하는 주문 ID")
                                .withProductTitle("제품명")
                                .withLanguageForDisplay(PayConfigurer.Language.KO)
                                .withAmount(-1)
                                .withCurrency(PayConfigurer.Currency.KRW)
                                .withShippingAddress(new PayConfigurer.ShippingAddress("137-332", "서초구 잠원동 하나아파트", "1동 1호", "서울", "", "KR"))
                                .withDeliveryPhoneNumber("01011112222")
                                .withDeliveryName("배송 수신자")
                                .withInstallmentPerCardInformation(new PayConfigurer.CardInstallmentInformation("카드구분 코드", "할부정보. ex. NN1;NN2;YY3;YY4;YY5;NH6"))
                                .withBeAbleToExchangeToCash(false)
                                .withPayableRuleWithCard(PayConfigurer.PayableLocaleRule.ONLY_ALLOWED_KOR)
                ;
        // @formatter:on
        // When
        String t = syrupPayTokenBuilder.generateTokenBy("가맹점에게 전달한 비밀키");

        // Then
        // throw Exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void 구매_시_구매금액_0_입력_후_토큰_생성() throws Exception {
        // @formatter:off
        syrupPayTokenBuilder.of("가맹점")
                            .pay()
                                .withOrderIdOfMerchant("가맹점에서 관리하는 주문 ID")
                                .withProductTitle("제품명")
                                .withLanguageForDisplay(PayConfigurer.Language.KO)
                                .withAmount(0)
                                .withCurrency(PayConfigurer.Currency.KRW)
                                .withShippingAddress(new PayConfigurer.ShippingAddress("137-332", "서초구 잠원동 하나아파트", "1동 1호", "서울", "", "KR"))
                                .withDeliveryPhoneNumber("01011112222")
                                .withDeliveryName("배송 수신자")
                                .withInstallmentPerCardInformation(new PayConfigurer.CardInstallmentInformation("카드구분 코드", "할부정보. ex. NN1;NN2;YY3;YY4;YY5;NH6"))
                                .withBeAbleToExchangeToCash(false)
                                .withPayableRuleWithCard(PayConfigurer.PayableLocaleRule.ONLY_ALLOWED_KOR)
                ;
        // @formatter:on
        // When
        String t = syrupPayTokenBuilder.generateTokenBy("가맹점에게 전달한 비밀키");

        // Then
        // throw Exception
    }

    @Test
    public void 구매_시_구매금액_통화단위_미입력_후_토큰_생성() throws Exception {
        // @formatter:off
        syrupPayTokenBuilder.of("가맹점")
                            .pay()
                                .withOrderIdOfMerchant("가맹점에서 관리하는 주문 ID")
                                .withProductTitle("제품명")
                                .withLanguageForDisplay(PayConfigurer.Language.KO)
                                .withAmount(500000)
                                //.withCurrency(PayConfigurer.Currency.KRW)
                                .withShippingAddress(new PayConfigurer.ShippingAddress("137-332", "서초구 잠원동 하나아파트", "1동 1호", "서울", "", "KR"))
                                .withDeliveryPhoneNumber("01011112222")
                                .withDeliveryName("배송 수신자")
                                .withInstallmentPerCardInformation(new PayConfigurer.CardInstallmentInformation("카드구분 코드", "할부정보. ex. NN1;NN2;YY3;YY4;YY5;NH6"))
                                .withBeAbleToExchangeToCash(false)
                                .withPayableRuleWithCard(PayConfigurer.PayableLocaleRule.ONLY_ALLOWED_KOR)
                ;
        // @formatter:on
        // When
        Token token = SyrupPayTokenBuilder.verify(syrupPayTokenBuilder.generateTokenBy("가맹점에게 전달한 비밀키"), "가맹점에게 전달한 비밀키");

        // Then
        assertThat(token.getTransactionInfo().getPaymentInfo().getCurrencyCode(), is("KRW"));
    }

    @Test
    public void 구매_시_언어_미입력_후_토큰_생성() throws Exception {
        // @formatter:off
        syrupPayTokenBuilder.of("가맹점")
                            .pay()
                                .withOrderIdOfMerchant("가맹점에서 관리하는 주문 ID")
                                .withProductTitle("제품명")
                                //.withLanguageForDisplay(PayConfigurer.Language.KO)
                                .withAmount(5550)
                                .withCurrency(PayConfigurer.Currency.KRW)
                                .withShippingAddress(new PayConfigurer.ShippingAddress("137-332", "서초구 잠원동 하나아파트", "1동 1호", "서울", "", "KR"))
                                .withDeliveryPhoneNumber("01011112222")
                                .withDeliveryName("배송 수신자")
                                .withInstallmentPerCardInformation(new PayConfigurer.CardInstallmentInformation("카드구분 코드", "할부정보. ex. NN1;NN2;YY3;YY4;YY5;NH6"))
                                .withBeAbleToExchangeToCash(false)
                                .withPayableRuleWithCard(PayConfigurer.PayableLocaleRule.ONLY_ALLOWED_KOR)
                ;
        // @formatter:on
        // When
        Token token = SyrupPayTokenBuilder.verify(syrupPayTokenBuilder.generateTokenBy("가맹점에게 전달한 비밀키"), "가맹점에게 전달한 비밀키");

        // Then
        assertThat(token.getTransactionInfo().getPaymentInfo().getLang(), is("KO"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void 구매_시_구매금액_배송지_입력_후_토큰_생성() throws Exception {
        // @formatter:off
        syrupPayTokenBuilder.of("가맹점")
                            .pay()
                                .withOrderIdOfMerchant("가맹점에서 관리하는 주문 ID")
                                .withProductTitle("제품명")
                                .withLanguageForDisplay(PayConfigurer.Language.KO)
                                .withAmount(0)
                                .withCurrency(PayConfigurer.Currency.KRW)
                                //.withShippingAddress(new PayConfigurer.ShippingAddress("137-332", "서초구 잠원동 하나아파트", "1동 1호", "서울", "", "KR"))
                                .withDeliveryPhoneNumber("01011112222")
                                .withDeliveryName("배송 수신자")
                                .withInstallmentPerCardInformation(new PayConfigurer.CardInstallmentInformation("카드구분 코드", "할부정보. ex. NN1;NN2;YY3;YY4;YY5;NH6"))
                                .withBeAbleToExchangeToCash(false)
                                .withPayableRuleWithCard(PayConfigurer.PayableLocaleRule.ONLY_ALLOWED_KOR)
                ;
        // @formatter:on
        // When
        String t = syrupPayTokenBuilder.generateTokenBy("가맹점에게 전달한 비밀키");

        // Then
        // throw Exception
    }

    @Test
    public void 구매_시_가맹점_주문ID_40자_입력_후_토큰_생성() throws Exception {
        // @formatter:off
        syrupPayTokenBuilder.of("가맹점")
                            .pay()
                                .withOrderIdOfMerchant("1234567890123456789012345678901234567890")
                                .withProductTitle("제품명")
                                .withLanguageForDisplay(PayConfigurer.Language.KO)
                                .withAmount(50000)
                                .withCurrency(PayConfigurer.Currency.KRW)
                                .withShippingAddress(new PayConfigurer.ShippingAddress("137-332", "서초구 잠원동 하나아파트", "1동 1호", "서울", "", "KR"))
                                .withDeliveryPhoneNumber("01011112222")
                                .withDeliveryName("배송 수신자")
                                .withInstallmentPerCardInformation(new PayConfigurer.CardInstallmentInformation("카드구분 코드", "할부정보. ex. NN1;NN2;YY3;YY4;YY5;NH6"))
                                .withBeAbleToExchangeToCash(false)
                                .withPayableRuleWithCard(PayConfigurer.PayableLocaleRule.ONLY_ALLOWED_KOR)
                ;
        // @formatter:on
        // When
        Token token = SyrupPayTokenBuilder.verify(syrupPayTokenBuilder.generateTokenBy("가맹점에게 전달한 비밀키"), "가맹점에게 전달한 비밀키");

        // Then
        assertThat(token.getTransactionInfo().getMctTransAuthId().length(), is(40));
    }

    @Test(expected = IllegalArgumentException.class)
    public void 구매_시_가맹점_주문ID_41자_입력_후_토큰_생성() throws Exception {
        // @formatter:off
        syrupPayTokenBuilder.of("가맹점")
                            .pay()
                                .withOrderIdOfMerchant("12345678901234567890123456789012345678901")
                                .withProductTitle("제품명")
                                .withLanguageForDisplay(PayConfigurer.Language.KO)
                                .withAmount(50000)
                                .withCurrency(PayConfigurer.Currency.KRW)
                                .withShippingAddress(new PayConfigurer.ShippingAddress("137-332", "서초구 잠원동 하나아파트", "1동 1호", "서울", "", "KR"))
                                .withDeliveryPhoneNumber("01011112222")
                                .withDeliveryName("배송 수신자")
                                .withInstallmentPerCardInformation(new PayConfigurer.CardInstallmentInformation("카드구분 코드", "할부정보. ex. NN1;NN2;YY3;YY4;YY5;NH6"))
                                .withBeAbleToExchangeToCash(false)
                                .withPayableRuleWithCard(PayConfigurer.PayableLocaleRule.ONLY_ALLOWED_KOR)
                ;
        // @formatter:on
        // When
        String t = syrupPayTokenBuilder.generateTokenBy("가맹점에게 전달한 비밀키");

        // Then
        // throw Exception
    }

    @Test
    public void 구매_시_제품_상세_URL_추가_토큰_생성() throws Exception {
        // @formatter:off
        syrupPayTokenBuilder.of("가맹점")
                            .pay()
                                .withOrderIdOfMerchant("1234567890123456789012345678901234567890")
                                .withProductTitle("제품명")
                                .withProductUrls("http://www.11st.co.kr/product/SellerProductDetail.tmall?method=getSellerProductDetail&prdNo=1354119088&trTypeCd=22&trCtgrNo=895019")
                                .withMerchantDefinedValue("{\n" +
                                                            "\"id_1\": \"value\",\n" +
                                                            "\"id_2\": 2\n" +
                                                            "}")
                                .withLanguageForDisplay(PayConfigurer.Language.KO)
                                .withAmount(50000)
                                .withCurrency(PayConfigurer.Currency.KRW)
                                .withShippingAddress(new PayConfigurer.ShippingAddress("137-332", "서초구 잠원동 하나아파트", "1동 1호", "서울", "", "KR"))
                                .withDeliveryPhoneNumber("01011112222")
                                .withDeliveryName("배송 수신자")
                                .withInstallmentPerCardInformation(new PayConfigurer.CardInstallmentInformation("카드구분 코드", "할부정보. ex. NN1;NN2;YY3;YY4;YY5;NH6"))
                                .withBeAbleToExchangeToCash(false)
                                .withPayableRuleWithCard(PayConfigurer.PayableLocaleRule.ONLY_ALLOWED_KOR)
                ;
        // @formatter:on
        // When
        Token token = SyrupPayTokenBuilder.verify(syrupPayTokenBuilder.generateTokenBy("가맹점에게 전달한 비밀키"), "가맹점에게 전달한 비밀키");

        // Then
        assertThat(token.getTransactionInfo().getPaymentInfo().getProductUrls().size(), is(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void 구매_시_제품_상세_URL에_HTTP가_아닌_값_입력_후_토큰_생성() throws Exception {
        // @formatter:off
        syrupPayTokenBuilder.of("가맹점")
                            .pay()
                                .withOrderIdOfMerchant("1234567890123456789012345678901234567890")
                                .withProductTitle("제품명")
                                .withProductUrls("h://www.11st.co.kr/product/SellerProductDetail.tmall?method=getSellerProductDetail&prdNo=1354119088&trTypeCd=22&trCtgrNo=895019")
                                .withLanguageForDisplay(PayConfigurer.Language.KO)
                                .withAmount(50000)
                                .withCurrency(PayConfigurer.Currency.KRW)
                                .withShippingAddress(new PayConfigurer.ShippingAddress("137-332", "서초구 잠원동 하나아파트", "1동 1호", "서울", "", "KR"))
                                .withDeliveryPhoneNumber("01011112222")
                                .withDeliveryName("배송 수신자")
                                .withInstallmentPerCardInformation(new PayConfigurer.CardInstallmentInformation("카드구분 코드", "할부정보. ex. NN1;NN2;YY3;YY4;YY5;NH6"))
                                .withBeAbleToExchangeToCash(false)
                                .withPayableRuleWithCard(PayConfigurer.PayableLocaleRule.ONLY_ALLOWED_KOR)
                ;
        // @formatter:on
        // When
        Token token = SyrupPayTokenBuilder.verify(syrupPayTokenBuilder.generateTokenBy("가맹점에게 전달한 비밀키"), "가맹점에게 전달한 비밀키");

        // Then
        // throw exception
    }

    @Test
    public void 토큰_시럽페이사용자연동_추가_후_복호화() throws Exception {
        // Given
        // @formatter:off
        String t = syrupPayTokenBuilder.of("가맹점")
                .login()
                    .withMerchantUserId("가맹점의 회원 ID 또는 식별자")
                    .withExtraMerchantUserId("핸드폰과 같이 회원 별 추가 ID 체계가 존재할 경우 입력")
                    .withSsoCredential("SSO 를 발급 받았을 경우 입력")
                .and()
                .mapToSyrupPayUser().withType(MapToSyrupPayUserConfigurer.MappingType.CI_HASH).withValue("asdkfjhsakdfj")
                .and()
                .generateTokenBy("가맹점에게 전달한 비밀키");
        // @formatter:on

        // When
        Token token = SyrupPayTokenBuilder.verify(t, "가맹점에게 전달한 비밀키");

        // Then
        assertThat(token, is(notNullValue()));
        assertThat(token.isValidInTime(), is(true));
        assertThat(token.getIss(), is("가맹점"));
        assertThat(token.getUserInfoMapper(), is(notNullValue()));
        assertThat(token.getUserInfoMapper().getMappingValue(), is("asdkfjhsakdfj"));
    }

    @Test
    public void 체크아웃을_이용한_인증_토큰_생성() throws Exception {
        // @formatter:off
        syrupPayTokenBuilder.of("가맹점")
                            .checkout()
                                .withProductPrice(5000)
                                .withOffers(Mocks.offerList)
                                .withLoyalties(Mocks.loyalList)
                                .withProductDeliveryInfo(Mocks.productDeliveryInfoList.get(0))
                                .withShippingAddresses(Mocks.shippingAddressList)
                                .withSubmallName("11번가")
                ;
        // @formatter:on
        // When
        Token token = SyrupPayTokenBuilder.verify(syrupPayTokenBuilder.generateTokenBy("가맹점에게 전달한 비밀키"), "가맹점에게 전달한 비밀키");

        // Then
        assertThat(token.getCheckoutInfo(), is(notNullValue()));
        assertThat(token.getCheckoutInfo().getProductPrice(), is(5000));
    }

    @Test
    public void 하위버전_1_2_30_호환_테스트() throws IOException, InvalidTokenException {
        SyrupPayTokenBuilder.uncheckValidationOfToken();
        Token t = SyrupPayTokenBuilder.verify(TokenHistories.VERSION_1_2_30.token, TokenHistories.VERSION_1_2_30.key);
        System.out.println(new ObjectMapper().writeValueAsString(t));
    }

    @Test
    public void C_샵버전_0_0_1_호환_테스트() throws IOException, InvalidTokenException {
        SyrupPayTokenBuilder.uncheckValidationOfToken();
        Token t = SyrupPayTokenBuilder.verify(TokenHistories.C_SHARP_0_0_1.token, TokenHistories.C_SHARP_0_0_1.key);
        System.out.println(new ObjectMapper().writeValueAsString(t));
    }

    @Test
    public void 라이브러리_적용_전_버전_11번가_테스트() throws Exception {
        SyrupPayTokenBuilder.uncheckValidationOfToken();
        Token t = SyrupPayTokenBuilder.verify(TokenHistories.BEFORE_11ST.token, TokenHistories.BEFORE_11ST.key);
        assertThat(t.getTransactionInfo().getMctTransAuthId(), is(notNullValue()));
        assertThat(t.getTransactionInfo().getPaymentRestrictions().getCardIssuerRegion(), is(notNullValue()));
    }

    @Test
    public void 하위버전_1_3_4_버전_CJOSHOPPING_테스트() throws Exception {
        SyrupPayTokenBuilder.uncheckValidationOfToken();
        Token t = SyrupPayTokenBuilder.verify(TokenHistories.VERSION_1_3_4_BY_CJOSHOPPING.token, TokenHistories.VERSION_1_3_4_BY_CJOSHOPPING.key);
        assertThat(t.getTransactionInfo().getMctTransAuthId(), is(notNullValue()));
        assertThat(t.getTransactionInfo().getPaymentRestrictions().getCardIssuerRegion(), is(notNullValue()));
    }

    @Test(expected = Exception.class)
    public void 체크아웃_잘못된_규격_테스트() throws IOException, InvalidTokenException {
        SyrupPayTokenBuilder.uncheckValidationOfToken();
        Token t = SyrupPayTokenBuilder.verify(TokenHistories.VERSION_1_3_5_INVALID.token, TokenHistories.VERSION_1_3_5_INVALID.key);
        assertThat(t.getTransactionInfo().getMctTransAuthId(), is(notNullValue()));
        assertThat(t.getTransactionInfo().getPaymentRestrictions().getCardIssuerRegion(), is(notNullValue()));
    }
}