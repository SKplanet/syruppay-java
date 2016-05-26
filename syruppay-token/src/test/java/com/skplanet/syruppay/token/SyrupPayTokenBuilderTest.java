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

import com.skplanet.jose.Jose;
import com.skplanet.jose.JoseBuilders;
import com.skplanet.syruppay.token.claims.MapToSyrupPayUserConfigurer;
import com.skplanet.syruppay.token.claims.PayConfigurer;
import com.skplanet.syruppay.token.domain.Mocks;
import com.skplanet.syruppay.token.domain.TokenHistories;
import com.skplanet.syruppay.token.jwt.SyrupPayToken;
import com.skplanet.syruppay.token.jwt.Token;
import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.spec.KeySpec;

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
        SyrupPayTokenBuilder.uncheckHeaderOfToken();
        Token t = SyrupPayTokenBuilder.verify(TokenHistories.VERSION_1_2_30.token, TokenHistories.VERSION_1_2_30.key);
        System.out.println(new ObjectMapper().writeValueAsString(t));
    }

    @Test
    public void C_샵버전_0_0_1_호환_테스트() throws IOException, InvalidTokenException {
        SyrupPayTokenBuilder.uncheckHeaderOfToken();
        Token t = SyrupPayTokenBuilder.verify(TokenHistories.C_SHARP_0_0_1.token, TokenHistories.C_SHARP_0_0_1.key);
        System.out.println(new ObjectMapper().writeValueAsString(t));
    }

    @Test
    public void 라이브러리_적용_전_버전_11번가_테스트() throws Exception {
        SyrupPayTokenBuilder.uncheckHeaderOfToken();
        Token t = SyrupPayTokenBuilder.verify(TokenHistories.BEFORE_11ST.token, TokenHistories.BEFORE_11ST.key);
        assertThat(t.getTransactionInfo().getMctTransAuthId(), is(notNullValue()));
        assertThat(t.getTransactionInfo().getPaymentRestrictions().getCardIssuerRegion(), is(notNullValue()));
    }

    @Test
    public void 하위버전_1_3_4_버전_CJOSHOPPING_테스트() throws Exception {
        SyrupPayTokenBuilder.uncheckHeaderOfToken();
        Token t = SyrupPayTokenBuilder.verify(TokenHistories.VERSION_1_3_4_BY_CJOSHOPPING.token, TokenHistories.VERSION_1_3_4_BY_CJOSHOPPING.key);
        assertThat(t.getTransactionInfo().getMctTransAuthId(), is(notNullValue()));
        assertThat(t.getTransactionInfo().getPaymentRestrictions().getCardIssuerRegion(), is(notNullValue()));
    }

    @Test(expected = Exception.class)
    public void 체크아웃_잘못된_규격_테스트() throws IOException, InvalidTokenException {
        SyrupPayTokenBuilder.uncheckHeaderOfToken();
        Token t = SyrupPayTokenBuilder.verify(TokenHistories.VERSION_1_3_5_INVALID.token, TokenHistories.VERSION_1_3_5_INVALID.key);
        assertThat(t.getTransactionInfo().getMctTransAuthId(), is(notNullValue()));
        assertThat(t.getTransactionInfo().getPaymentRestrictions().getCardIssuerRegion(), is(notNullValue()));
    }

    @Test
    public void PHP_버전_사용자로그인_토큰_호환성_테스트() throws IOException, InvalidTokenException {
        SyrupPayTokenBuilder.uncheckHeaderOfToken();
        Token t = SyrupPayTokenBuilder.verify(TokenHistories.PHP_TO_LOGIN_VERSION_1_0_0.token, TokenHistories.PHP_TO_LOGIN_VERSION_1_0_0.key);
        System.out.println(new ObjectMapper().writeValueAsString(t));
    }

    @Test
    public void PHP_버전_결제_토큰_호환성_테스트() throws IOException, InvalidTokenException {
        SyrupPayTokenBuilder.uncheckHeaderOfToken();
        Token t = SyrupPayTokenBuilder.verify(TokenHistories.PHP_TO_PAY_VERSION_1_0_0.token, TokenHistories.PHP_TO_PAY_VERSION_1_0_0.key);
        System.out.println(new ObjectMapper().writeValueAsString(t));
    }

    @Test
    public void 자동결제를_위한_인증_토큰_생성() throws Exception {
        // Given
        // @formatter:off
        String t = syrupPayTokenBuilder.of("가맹점")
                .login()
                    .withMerchantUserId("가맹점의 회원 ID 또는 식별자")
                    .withExtraMerchantUserId("핸드폰과 같이 회원 별 추가 ID 체계가 존재할 경우 입력")
                    .withSsoCredential("SSO 를 발급 받았을 경우 입력")
                .and()
                .subscription()
                    .withAutoPaymentId("시럽페이로부터 발급받은 자동결제 ID") // Optional
                    .withRestrictionOf(PayConfigurer.MatchedUser.CI_MATCHED_ONLY) // Optional
                .and()
                .generateTokenBy("가맹점에게 전달한 비밀키");
        // @formatter:on
        System.out.printf(t);
        // When
        Token token = SyrupPayTokenBuilder.verify(t, "가맹점에게 전달한 비밀키");

        // Then
        assertThat(token, is(notNullValue()));
        assertThat(token.isValidInTime(), is(true));
        assertThat(token.getIss(), is("가맹점"));
    }

    @Test
    public void OCB_복호화() throws Exception {
        String t = "eyJhbGciOiJBMTI4S1ciLCJlbmMiOiJBMTI4Q0JDLUhTMjU2Iiwia2lkIjoic2t0bWFsbF9zMDAyIiwidmVyIjoiMS4zLjIifQ.RTsmoCwCgsBViSsQUzYy4iNM3MYVTmpbf9J5be_Kpf9pw5s8xrdGAQ.7wrdUYUgLnTXfK-B_Bjlhg.WiffmU_pgYtsPWi95sfRmGNyM8bIBWNN7YTUPW6LX14zWgOu8O43YIADhgZtG5b_65iBxq--vS1D_cKEPp523okH4cDLwaubsw8xkkh1ke_E9mxpXhEvjdPRyK4Kv7khYeBn3T515cby3fzHX5Ubv5qx3uJ_1u9udkCJ8LqdZEzyIfWeTIHQZTt2C3NAVWBK2s8dGSGsQLsRzKks-7BAMMVyS-IE_UYoQ4OQ5q41kyMIxqbstEiheYx8A-BU32KvBU_EqcS_zeRE-LgwgVIjh35KezLmdQQnD1Sp3aDMrV0.cYCigQ6r-1hv0oDtxqs0dA";

        String json = new Jose().configuration(
                JoseBuilders.compactDeserializationBuilder()
                        .serializedSource(t)
                        .key("crTKa9RY5Re0J0UYSin9cFap6x5UDsib")
        ).deserialization();
        System.out.printf(json);
    }

    @Test
    public void AES_MODE_별_테스트_ERROR() throws Exception {
        final String keyFactorySalt = "65594821073030071593";
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
        SecretKeySpec secretKeySpec;
        try {
            KeySpec spec = new PBEKeySpec("7244798e1fab1a9175f752a8a7e12beafe2cd27b208f9f2f7ab43173358153fc5eae2499afa66f7386d74cb8cf4765133c513ae2e6acd521acde4f80d747".toCharArray(), keyFactorySalt.getBytes(), 1, 256);
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            SecretKey secretKey = secretKeyFactory.generateSecret(spec);
            secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");
        } catch (Exception e) {
            throw e;
        }
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
        System.out.println(new String(cipher.doFinal(Base64.decodeBase64("yMvtcFwlhwBg22GF-biF4A".getBytes())), "UTF-8"));
    }

    @Test
    public void AES_MODE_별_테스트() throws Exception {
        final String keyFactorySalt = "65594821073030071593";
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
        SecretKeySpec secretKeySpec;
        try {
            KeySpec spec = new PBEKeySpec("56c32e32c94a9fbde34cbc25a3b232bf25e91ec3a8a67159cffef70b924a67dcca13fc364cdb10a7a265f5520469997709ce542e6644b861c585c7ccd2f3".toCharArray(), keyFactorySalt.getBytes(), 1, 256);
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            SecretKey secretKey = secretKeyFactory.generateSecret(spec);
            secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");
        } catch (Exception e) {
            throw e;
        }
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
        System.out.println(new String(cipher.doFinal(Base64.decodeBase64("yMvtcFwlhwBg22GF-biF4A".getBytes())), "UTF-8"));
    }

    @Test
    public void 자동결제를_위한_인증_토큰_생성_후_클래임_확인_테스트() throws Exception {
        // Given
        // @formatter:off
        String t = syrupPayTokenBuilder.of("가맹점")
                .login()
                    .withMerchantUserId("가맹점의 회원 ID 또는 식별자")
                    .withExtraMerchantUserId("핸드폰과 같이 회원 별 추가 ID 체계가 존재할 경우 입력")
                    .withSsoCredential("SSO 를 발급 받았을 경우 입력")
                .and()
                .subscription()
                    .withAutoPaymentId("시럽페이로부터 발급받은 자동결제 ID") // Optional
                    .withRestrictionOf(PayConfigurer.MatchedUser.CI_MATCHED_ONLY) // Optional
                .and()
                .generateTokenBy("가맹점에게 전달한 비밀키");
        // @formatter:on
        System.out.printf(t);
        // When
        Token token = SyrupPayTokenBuilder.verify(t, "가맹점에게 전달한 비밀키");

        // Then
        assertThat(token, is(notNullValue()));
        assertThat(token.getClaims(SyrupPayToken.Claim.TO_SUBSCRIPTION).size(), is(1));
        assertThat(token.getClaims(SyrupPayToken.Claim.TO_LOGIN).size(), is(1));
        assertThat(token.getClaims(SyrupPayToken.Claim.TO_PAY).size(), is(0));
    }

    @Test
    public void 자동결제를_위한_인증_토큰_생성_후_개별_클래임_확인_테스트() throws Exception {
        // Given
        // @formatter:off
        String t = syrupPayTokenBuilder.of("가맹점")
                .login()
                    .withMerchantUserId("가맹점의 회원 ID 또는 식별자")
                    .withExtraMerchantUserId("핸드폰과 같이 회원 별 추가 ID 체계가 존재할 경우 입력")
                    .withSsoCredential("SSO 를 발급 받았을 경우 입력")
                .and()
                .subscription()
                    .withAutoPaymentId("시럽페이로부터 발급받은 자동결제 ID") // Optional
                    .withRestrictionOf(PayConfigurer.MatchedUser.CI_MATCHED_ONLY) // Optional
                .and()
                .generateTokenBy("가맹점에게 전달한 비밀키");
        // @formatter:on
        System.out.printf(t);
        // When
        Token token = SyrupPayTokenBuilder.verify(t, "가맹점에게 전달한 비밀키");

        // Then
        assertThat(token, is(notNullValue()));
        assertThat(token.getClaim(SyrupPayToken.Claim.TO_SUBSCRIPTION), is(notNullValue()));
        assertThat(token.getClaim(SyrupPayToken.Claim.TO_LOGIN), is(notNullValue()));
        assertThat(token.getClaim(SyrupPayToken.Claim.TO_PAY), is(nullValue()));
    }

    @Test
    public void 개인정보_전달을위한_토큰규격_테스트() throws Exception {
        // Given
        // @formatter:off
        String t = syrupPayTokenBuilder.of("가맹점")
                .login()
                    .withMerchantUserId("가맹점의 회원 ID 또는 식별자")
                    .withExtraMerchantUserId("핸드폰과 같이 회원 별 추가 ID 체계가 존재할 경우 입력")
                    .withSsoCredential("SSO 를 발급 받았을 경우 입력")
                .and()
                .mapToSyrupPayUser()
                    .withType(MapToSyrupPayUserConfigurer.MappingType.ENCRYPTED_PERSONAL_INFO)
                    .withValue(new MapToSyrupPayUserConfigurer.Personal("사용자", "1234567", "휴대폰번호"), "가맹점 ID", "가맹점에게 전달한 비밀")
                .and()
                .generateTokenBy("가맹점에게 전달한 비밀키");
        // @formatter:on
        System.out.printf(t);
        // When
        Token token = SyrupPayTokenBuilder.verify(t, "가맹점에게 전달한 비밀키");

        // Then
        assertThat(token, is(notNullValue()));
        assertThat(token.getClaim(SyrupPayToken.Claim.TO_LOGIN), is(notNullValue()));
        assertThat(token.getClaim(SyrupPayToken.Claim.TO_MAP_USER), is(notNullValue()));
        assertThat(token.getUserInfoMapper().getPersonalIfNotExistThenNullWith("가맹점에게 전달한 비밀"), is(notNullValue()));
    }

    @Test
    public void 원클릭_토큰_유효성검증_테스트() throws Exception {
        // Given
        // @formatter:off
        String t = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJza3RtYWxsX3MwMDIiLCJhdWQiOiJodHRwczovL3BheS5zeXJ1cC5jby5rciIsInR5cCI6Impvc2UiLCJpYXQiOjE0NjQxNjI3NjYsImV4cCI6MTQ2NDE2NjM2NiwibG9naW5JbmZvIjp7Im1jdFVzZXJJZCI6IjIxNjA2MDU1IiwiU1NPQ3JlZGVudGlhbCI6InBSaGZhbzZBd0RLTGZURjFVYmdBRlNxYjgzX1VhSXZ4c2RWZDJiLTBpWGciLCJkZXZpY2VJZGVudGlmaWVyIjoicFJoZmFvNkF3REtMZlRGMVViZ0FGU3FiODNfVWFJdnhzZFZkMmItMGlYZyJ9LCJ1c2VySW5mb01hcHBlciI6eyJtYXBwaW5nVHlwZSI6IkNJX01BUFBFRF9LRVkiLCJtYXBwaW5nVmFsdWUiOiIyMTYwNjA1NSJ9LCJ0cmFuc2FjdGlvbkluZm8iOnsibWN0VHJhbnNBdXRoSWQiOiIyMDE2MDUyNTAxMTgyNzYiLCJwYXltZW50SW5mbyI6eyJwcm9kdWN0VGl0bGUiOiJbUUFf6rmA66qF7ZmYXSDsoJXquLDqtazrp6Rf7ISg7YOdK-yeheugpe2YleyYteyFmCIsImxhbmciOiJrbyIsImN1cnJlbmN5Q29kZSI6IktSVyIsInBheW1lbnRBbXQiOjEwMDAwLCJzaGlwcGluZ0FkZHJlc3MiOiJhMjpLUnx8dW5rbm93bnx8fCIsImlzRXhjaGFuZ2VhYmxlIjpudWxsLCJkZWxpdmVyeU5hbWUiOiIiLCJkZWxpdmVyeVBob25lTnVtYmVyIjoiIiwicHJvZHVjdFVybHMiOlsiaHR0cDovL20uMTFzdC5jby5rci9NVy9Qcm9kdWN0L3Byb2R1Y3RCYXNpY0luZm8udG1hbGw_cHJkTm89MTI0NDg3NTU3MSJdfSwicGF5bWVudFJlc3RyaWN0aW9ucyI6eyJjYXJkSXNzdWVyUmVnaW9uIjoiQUxMT1dFRDprb3IifX0sImNoZWNrb3V0SW5mbyI6eyJwcm9kdWN0UHJpY2UiOjEwMDAwLCJzdWJtYWxsTmFtZSI6IlvsmKztkohdIiwicHJpdmFjeVBvbGljeVJlcXVpcmVtZW50cyI6IiIsIm1haW5TaGlwcGluZ0FkZHJlc3NTZXR0aW5nRGlzYWJsZWQiOmZhbHNlLCJwcm9kdWN0RGVsaXZlcnlJbmZvIjp7ImRlbGl2ZXJ5VHlwZSI6IkZSRUUiLCJkZWxpdmVyeU5hbWUiOiLrrLTro4zrsLDshqEiLCJkZWZhdWx0RGVsaXZlcnlDb3N0QXBwbGllZCI6ZmFsc2UsImFkZGl0aW9uYWxEZWxpdmVyeUNvc3RBcHBsaWVkIjpmYWxzZSwic2hpcHBpbmdBZGRyZXNzRGlzcGxheSI6dHJ1ZX0sImxveWFsdHlMaXN0IjpbeyJpZCI6InVzZU1hbGxwb2ludCIsInVzZXJBY3Rpb25Db2RlIjoiU1BBWUEwMzA2OlNQQVlBMDMxMCIsIm5hbWUiOiLtj6zsnbjtirgiLCJzdWJzY3JpYmVySWQiOm51bGwsImJhbGFuY2UiOjM5ODAwLCJtYXhBcHBsaWNhYmxlQW10IjozOTgwMCwiaW5pdGlhbEFwcGxpZWRBbXQiOjEwMDAwLCJvcmRlckFwcGxpZWQiOjMsImFwcGxpY2FibGVGb3JOb3RNYXRjaGVkVXNlciI6ZmFsc2V9LHsiaWQiOiJ1c2VPQ0IiLCJ1c2VyQWN0aW9uQ29kZSI6IlNQQVlBMDMwNzpTUEFZQTAzMTEiLCJuYW1lIjoiT0vsupDsiazrsLEiLCJzdWJzY3JpYmVySWQiOm51bGwsImJhbGFuY2UiOjUwMDAwMCwibWF4QXBwbGljYWJsZUFtdCI6NTAwMDAwLCJpbml0aWFsQXBwbGllZEFtdCI6MCwib3JkZXJBcHBsaWVkIjo0LCJhcHBsaWNhYmxlRm9yTm90TWF0Y2hlZFVzZXIiOmZhbHNlfV0sInNoaXBwaW5nQWRkcmVzc0xpc3QiOlt7ImlkIjoiYmFzaWNfMDAxXzAiLCJuYW1lIjoi6riw67O467Cw7Iah7KeAIiwiY291bnRyeUNvZGUiOiJrciIsInppcENvZGUiOiI0MDcwNjAiLCJzdGF0ZSI6bnVsbCwiY2l0eSI6bnVsbCwibWFpbkFkZHJlc3MiOiLsnbjsspzqtJHsl63si5wg6rOE7JaR6rWsIOyekeyghOuPmSAiLCJkZXRhaWxBZGRyZXNzIjoi7Jyg7Zi47JWE7YyM7Yq4IDEwMeuPmSIsInJlY2lwaWVudE5hbWUiOiLthYzsiqTtirgg7YWM7Iqk7Yq4MiIsInJlY2lwaWVudFBob25lTnVtYmVyIjoiMDEwNTA5OTQ1NTYiLCJkZWxpdmVyeVJlc3RyaWN0aW9uIjoiTk9UX0ZBUl9BV0FZIiwiZGVmYXVsdERlbGl2ZXJ5Q29zdCI6MCwiYWRkaXRpb25hbERlbGl2ZXJ5Q29zdCI6MCwib3JkZXJBcHBsaWVkIjoxfSx7ImlkIjoicmVjZW50XzAwMV8wIiwibmFtZSI6Iuy1nOq3vOuwsOyGoeyngCIsImNvdW50cnlDb2RlIjoia3IiLCJ6aXBDb2RlIjoiNDA3MDYwIiwic3RhdGUiOm51bGwsImNpdHkiOm51bGwsIm1haW5BZGRyZXNzIjoi7J247LKc6rSR7Jet7IucIOqzhOyWkeq1rCDsnpHsoITrj5kgIiwiZGV0YWlsQWRkcmVzcyI6IuycoO2YuOyVhO2MjO2KuCAxMDHrj5kiLCJyZWNpcGllbnROYW1lIjoi7YWM7Iqk7Yq4IO2FjOyKpO2KuDIiLCJyZWNpcGllbnRQaG9uZU51bWJlciI6IjAxMDUwOTk0NTU2IiwiZGVsaXZlcnlSZXN0cmljdGlvbiI6Ik5PVF9GQVJfQVdBWSIsImRlZmF1bHREZWxpdmVyeUNvc3QiOjAsImFkZGl0aW9uYWxEZWxpdmVyeUNvc3QiOjAsIm9yZGVyQXBwbGllZCI6Mn0seyJpZCI6IjQxXzBfMzEyMDAxMjEwMDEwNjI2MDAwMDAwMDAwNSIsIm5hbWUiOiLthYzsiqTtirgyIiwiY291bnRyeUNvZGUiOiJrciIsInppcENvZGUiOiI0NDI1MSIsInN0YXRlIjpudWxsLCJjaXR5IjpudWxsLCJtYWluQWRkcmVzcyI6IuyauOyCsOq0keyXreyLnCDrtoHqtawgIOynhOyepTEz6ri4IDI5ICjsp4TsnqXrj5kpICIsImRldGFpbEFkZHJlc3MiOiLthYzsiqTtirgyIiwicmVjaXBpZW50TmFtZSI6Iu2FjOyKpO2KuDIiLCJyZWNpcGllbnRQaG9uZU51bWJlciI6IjAxMDU0Mzk0MDUiLCJkZWxpdmVyeVJlc3RyaWN0aW9uIjoiTk9UX0ZBUl9BV0FZIiwiZGVmYXVsdERlbGl2ZXJ5Q29zdCI6MCwiYWRkaXRpb25hbERlbGl2ZXJ5Q29zdCI6MCwib3JkZXJBcHBsaWVkIjozfSx7ImlkIjoiNDJfMDA3XzAiLCJuYW1lIjoi66GcIiwiY291bnRyeUNvZGUiOiJrciIsInppcENvZGUiOiIxNTE3MDUiLCJzdGF0ZSI6bnVsbCwiY2l0eSI6bnVsbCwibWFpbkFkZHJlc3MiOiLshJzsmrjtirnrs4Tsi5wg6rSA7JWF6rWsIOyhsOybkOuPmSAgIEtU6rWs66Gc7KeA7IKsICIsImRldGFpbEFkZHJlc3MiOiLthYzsiqTtirgiLCJyZWNpcGllbnROYW1lIjoi7Jy8IiwicmVjaXBpZW50UGhvbmVOdW1iZXIiOiIwMTA1MDk5NTY2NiIsImRlbGl2ZXJ5UmVzdHJpY3Rpb24iOiJOT1RfRkFSX0FXQVkiLCJkZWZhdWx0RGVsaXZlcnlDb3N0IjowLCJhZGRpdGlvbmFsRGVsaXZlcnlDb3N0IjowLCJvcmRlckFwcGxpZWQiOjR9LHsiaWQiOiI0M18wMDJfMCIsIm5hbWUiOiJ4cHRteG0iLCJjb3VudHJ5Q29kZSI6ImtyIiwiemlwQ29kZSI6IjE1ODc1MSIsInN0YXRlIjpudWxsLCJjaXR5IjpudWxsLCJtYWluQWRkcmVzcyI6IuyEnOyauO2KueuzhOyLnCDslpHsspzqtawg66qpNeuPmSAgIOuqqeuPmTHri6jsp4DslYTtjIztirggIiwiZGV0YWlsQWRkcmVzcyI6InhwdG14bSIsInJlY2lwaWVudE5hbWUiOiJzb25nIiwicmVjaXBpZW50UGhvbmVOdW1iZXIiOiIwMTAyMjIyMzMzMyIsImRlbGl2ZXJ5UmVzdHJpY3Rpb24iOiJOT1RfRkFSX0FXQVkiLCJkZWZhdWx0RGVsaXZlcnlDb3N0IjowLCJhZGRpdGlvbmFsRGVsaXZlcnlDb3N0IjowLCJvcmRlckFwcGxpZWQiOjV9LHsiaWQiOiI5NjVfMDAxXzAiLCJuYW1lIjoiMTExMTEiLCJjb3VudHJ5Q29kZSI6ImtyIiwiemlwQ29kZSI6IjE1NzAzMCIsInN0YXRlIjpudWxsLCJjaXR5IjpudWxsLCJtYWluQWRkcmVzcyI6IuyEnOyauO2KueuzhOyLnCDqsJXshJzqtawg65Ox7LSM64-ZICIsImRldGFpbEFkZHJlc3MiOiIxMTExMTExMTExMTIyMjIyMiIsInJlY2lwaWVudE5hbWUiOiIxMTExIiwicmVjaXBpZW50UGhvbmVOdW1iZXIiOiIwMTAxMTEyMjI0IiwiZGVsaXZlcnlSZXN0cmljdGlvbiI6Ik5PVF9GQVJfQVdBWSIsImRlZmF1bHREZWxpdmVyeUNvc3QiOjAsImFkZGl0aW9uYWxEZWxpdmVyeUNvc3QiOjAsIm9yZGVyQXBwbGllZCI6Nn0seyJpZCI6Ijk2Nl8wMDFfMCIsIm5hbWUiOiIxMTExMTEiLCJjb3VudHJ5Q29kZSI6ImtyIiwiemlwQ29kZSI6IjE1NzAzMCIsInN0YXRlIjpudWxsLCJjaXR5IjpudWxsLCJtYWluQWRkcmVzcyI6IuyEnOyauO2KueuzhOyLnCDqsJXshJzqtawg65Ox7LSM64-ZICIsImRldGFpbEFkZHJlc3MiOiLthYzsiqTtirjso7zshowgIiwicmVjaXBpZW50TmFtZSI6IjIyMjIyMiIsInJlY2lwaWVudFBob25lTnVtYmVyIjoiMDEwNTA5OTQ1NjYiLCJkZWxpdmVyeVJlc3RyaWN0aW9uIjoiTk9UX0ZBUl9BV0FZIiwiZGVmYXVsdERlbGl2ZXJ5Q29zdCI6MCwiYWRkaXRpb25hbERlbGl2ZXJ5Q29zdCI6MCwib3JkZXJBcHBsaWVkIjo3fSx7ImlkIjoiOTY3XzAwMV8wIiwibmFtZSI6InNvbmciLCJjb3VudHJ5Q29kZSI6ImtyIiwiemlwQ29kZSI6IjQwNzA2MCIsInN0YXRlIjpudWxsLCJjaXR5IjpudWxsLCJtYWluQWRkcmVzcyI6IuyduOyynOq0keyXreyLnCDqs4TslpHqtawg7J6R7KCE64-ZICIsImRldGFpbEFkZHJlc3MiOiLsnKDtmLjslYTtjIztirggMTAx64-ZIDQwNu2YuCIsInJlY2lwaWVudE5hbWUiOiJzb25nIiwicmVjaXBpZW50UGhvbmVOdW1iZXIiOiIwMTA1MDk5NDU2NiIsImRlbGl2ZXJ5UmVzdHJpY3Rpb24iOiJOT1RfRkFSX0FXQVkiLCJkZWZhdWx0RGVsaXZlcnlDb3N0IjowLCJhZGRpdGlvbmFsRGVsaXZlcnlDb3N0IjowLCJvcmRlckFwcGxpZWQiOjh9LHsiaWQiOiI5NjhfMDIxXzI4MjQ1MTAzMDAxMDc3ODAwMDYxNzA4MzgiLCJuYW1lIjoidGhkcnVkYWwiLCJjb3VudHJ5Q29kZSI6ImtyIiwiemlwQ29kZSI6IjQwNzgxNiIsInN0YXRlIjpudWxsLCJjaXR5IjpudWxsLCJtYWluQWRkcmVzcyI6IuyduOyynOq0keyXreyLnCDqs4TslpHqtawg7J6R7KCEMuuPmSAiLCJkZXRhaWxBZGRyZXNzIjoieHB0bXht7KO87IaMICYjMzg7IzM4OyMzODsjMzk7MSYjMzg7IzM4OyMzODsjMzk7JCUjQD8gKOyngOuyiDrsnpHsoIQy64-ZICkiLCJyZWNpcGllbnROYW1lIjoidGhkcnVkYWwiLCJyZWNpcGllbnRQaG9uZU51bWJlciI6IjAxMDUwOTk0NTY2IiwiZGVsaXZlcnlSZXN0cmljdGlvbiI6Ik5PVF9GQVJfQVdBWSIsImRlZmF1bHREZWxpdmVyeUNvc3QiOjAsImFkZGl0aW9uYWxEZWxpdmVyeUNvc3QiOjAsIm9yZGVyQXBwbGllZCI6OX0seyJpZCI6Ijk3MF8wMDFfMjg3MjAzNTAyODIwMDA0MDAwMDAwMjE4NiIsIm5hbWUiOiLrj4TshJzsgrDqsIQiLCJjb3VudHJ5Q29kZSI6ImtyIiwiemlwQ29kZSI6IjQwOTg5MSIsInN0YXRlIjpudWxsLCJjaXR5IjpudWxsLCJtYWluQWRkcmVzcyI6IuyduOyynOq0keyXreyLnCDsmLnsp4TqtbAg642V7KCB66m0IOq1tOyXheumrCAiLCJkZXRhaWxBZGRyZXNzIjoieHB0eG1kbHFzbGVrLiAo7KeA67KIOuuNleyggeuptCDqtbTsl4XrpqwgKSIsInJlY2lwaWVudE5hbWUiOiLrj4TshJzsgrDqsIQiLCJyZWNpcGllbnRQaG9uZU51bWJlciI6IjAxMDUwOTk0NTY2IiwiZGVsaXZlcnlSZXN0cmljdGlvbiI6IkZBUl9BV0FZIiwiZGVmYXVsdERlbGl2ZXJ5Q29zdCI6MCwiYWRkaXRpb25hbERlbGl2ZXJ5Q29zdCI6MCwib3JkZXJBcHBsaWVkIjoxMH1dLCJtb250aGx5SW5zdGFsbG1lbnRMaXN0IjpbeyJjYXJkQ29kZSI6Ijk3IiwiY29uZGl0aW9ucyI6W3sicGF5bWVudEFtdFJhbmdlIjoiWzAtNTAwMDApIiwibW9udGhseUluc3RhbGxtZW50SW5mbyI6IllOMSJ9LHsicGF5bWVudEFtdFJhbmdlIjoiWzUwMDAwLV0iLCJtb250aGx5SW5zdGFsbG1lbnRJbmZvIjoiWU4xO1lOMjtZTjM7WU40O1lONTtZTjY7WU43O1lOODtZTjk7WU4xMDtZTjExO1lOMTIifV19LHsiY2FyZENvZGUiOiIwOCIsImNvbmRpdGlvbnMiOlt7InBheW1lbnRBbXRSYW5nZSI6IlswLTUwMDAwKSIsIm1vbnRobHlJbnN0YWxsbWVudEluZm8iOiJZTjEifSx7InBheW1lbnRBbXRSYW5nZSI6Ils1MDAwMC1dIiwibW9udGhseUluc3RhbGxtZW50SW5mbyI6IllOMTtZTjI7WU4zO1lONDtZTjU7WU42O1lONztZTjg7WU45O1lOMTA7WU4xMTtZTjEyIn1dfSx7ImNhcmRDb2RlIjoiMTciLCJjb25kaXRpb25zIjpbeyJwYXltZW50QW10UmFuZ2UiOiJbNTAwMDAtXSIsIm1vbnRobHlJbnN0YWxsbWVudEluZm8iOiJOTjE7Tk4yO05OMztOTjQ7Tk41O05ONjtOTjc7Tk44O05OOTtOTjEwO05OMTE7TlkxMiJ9LHsicGF5bWVudEFtdFJhbmdlIjoiWzAtNTAwMDApIiwibW9udGhseUluc3RhbGxtZW50SW5mbyI6Ik5OMSJ9XX0seyJjYXJkQ29kZSI6IjIyIiwiY29uZGl0aW9ucyI6W3sicGF5bWVudEFtdFJhbmdlIjoiWzAtNTAwMDApIiwibW9udGhseUluc3RhbGxtZW50SW5mbyI6IllOMSJ9LHsicGF5bWVudEFtdFJhbmdlIjoiWzUwMDAwLV0iLCJtb250aGx5SW5zdGFsbG1lbnRJbmZvIjoiWU4xO1lOMjtZTjM7WU40O1lONTtZTjY7WU43O1lOODtZTjk7WU4xMDtZTjExO1lOMTIifV19LHsiY2FyZENvZGUiOiIwNCIsImNvbmRpdGlvbnMiOlt7InBheW1lbnRBbXRSYW5nZSI6IlswLTUwMDAwKSIsIm1vbnRobHlJbnN0YWxsbWVudEluZm8iOiJOTjEifSx7InBheW1lbnRBbXRSYW5nZSI6Ils1MDAwMC1dIiwibW9udGhseUluc3RhbGxtZW50SW5mbyI6Ik5OMTtOTjI7Tk4zO05ONDtOTjU7Tk42O05ONztOTjg7Tk45O05OMTA7Tk4xMTtOTjEyIn1dfSx7ImNhcmRDb2RlIjoiMzUiLCJjb25kaXRpb25zIjpbeyJwYXltZW50QW10UmFuZ2UiOiJbMC01MDAwMCkiLCJtb250aGx5SW5zdGFsbG1lbnRJbmZvIjoiWU4xIn0seyJwYXltZW50QW10UmFuZ2UiOiJbNTAwMDAtXSIsIm1vbnRobHlJbnN0YWxsbWVudEluZm8iOiJZTjE7WU4yO1lOMztZTjQ7WU41O1lONjtZTjc7WU44O1lOOTtZTjEwO1lOMTE7WU4xMiJ9XX0seyJjYXJkQ29kZSI6IjM2IiwiY29uZGl0aW9ucyI6W3sicGF5bWVudEFtdFJhbmdlIjoiWzAtNTAwMDApIiwibW9udGhseUluc3RhbGxtZW50SW5mbyI6IllOMSJ9LHsicGF5bWVudEFtdFJhbmdlIjoiWzUwMDAwLV0iLCJtb250aGx5SW5zdGFsbG1lbnRJbmZvIjoiWU4xO1lOMjtZTjM7WU40O1lONTtZTjY7WU43O1lOODtZTjk7WU4xMDtZTjExO1lOMTIifV19LHsiY2FyZENvZGUiOiIzMyIsImNvbmRpdGlvbnMiOlt7InBheW1lbnRBbXRSYW5nZSI6IlswLTUwMDAwKSIsIm1vbnRobHlJbnN0YWxsbWVudEluZm8iOiJZTjEifSx7InBheW1lbnRBbXRSYW5nZSI6Ils1MDAwMC1dIiwibW9udGhseUluc3RhbGxtZW50SW5mbyI6IllOMTtZTjI7WU4zO1lONDtZTjU7WU42O1lONztZTjg7WU45O1lOMTA7WU4xMTtZTjEyIn1dfSx7ImNhcmRDb2RlIjoiMDYiLCJjb25kaXRpb25zIjpbeyJwYXltZW50QW10UmFuZ2UiOiJbMC01MDAwMCkiLCJtb250aGx5SW5zdGFsbG1lbnRJbmZvIjoiTk4xIn0seyJwYXltZW50QW10UmFuZ2UiOiJbNTAwMDAtXSIsIm1vbnRobHlJbnN0YWxsbWVudEluZm8iOiJOTjE7Tk4yO05OMztOTjQ7Tk41O05ONjtOTjc7Tk44O05OOTtOTjEwO05OMTE7Tk4xMiJ9XX0seyJjYXJkQ29kZSI6IjA3IiwiY29uZGl0aW9ucyI6W3sicGF5bWVudEFtdFJhbmdlIjoiWzAtNTAwMDApIiwibW9udGhseUluc3RhbGxtZW50SW5mbyI6IllOMSJ9LHsicGF5bWVudEFtdFJhbmdlIjoiWzUwMDAwLV0iLCJtb250aGx5SW5zdGFsbG1lbnRJbmZvIjoiWU4xO1lOMjtZTjM7WU40O1lONTtZTjY7WU43O1lOODtZTjk7WU4xMDtZTjExO1lOMTIifV19LHsiY2FyZENvZGUiOiIzNCIsImNvbmRpdGlvbnMiOlt7InBheW1lbnRBbXRSYW5nZSI6IlswLTUwMDAwKSIsIm1vbnRobHlJbnN0YWxsbWVudEluZm8iOiJZTjEifSx7InBheW1lbnRBbXRSYW5nZSI6Ils1MDAwMC1dIiwibW9udGhseUluc3RhbGxtZW50SW5mbyI6IllOMTtZTjI7WU4zO1lONDtZTjU7WU42O1lONztZTjg7WU45O1lOMTA7WU4xMTtZTjEyIn1dfSx7ImNhcmRDb2RlIjoiMTYiLCJjb25kaXRpb25zIjpbeyJwYXltZW50QW10UmFuZ2UiOiJbNTAwMDAtXSIsIm1vbnRobHlJbnN0YWxsbWVudEluZm8iOiJOTjE7Tk4yO05OMztOTjQ7Tkg1O05ONjtOTjc7Tk44O05OOTtOTjEwO05OMTE7Tk4xMiJ9LHsicGF5bWVudEFtdFJhbmdlIjoiWzAtNTAwMDApIiwibW9udGhseUluc3RhbGxtZW50SW5mbyI6Ik5OMSJ9XX0seyJjYXJkQ29kZSI6IjE0IiwiY29uZGl0aW9ucyI6W3sicGF5bWVudEFtdFJhbmdlIjoiWzAtNTAwMDApIiwibW9udGhseUluc3RhbGxtZW50SW5mbyI6IllOMSJ9LHsicGF5bWVudEFtdFJhbmdlIjoiWzUwMDAwLV0iLCJtb250aGx5SW5zdGFsbG1lbnRJbmZvIjoiWU4xO1lOMjtZTjM7WU40O1lONTtZTjY7WU43O1lOODtZTjk7WU4xMDtZTjExO1lOMTIifV19LHsiY2FyZENvZGUiOiIwMSIsImNvbmRpdGlvbnMiOlt7InBheW1lbnRBbXRSYW5nZSI6Ils1MDAwMC1dIiwibW9udGhseUluc3RhbGxtZW50SW5mbyI6IllOMTtZTjI7WU4zO1lONDtZTjU7WU42O1lONztZTjg7WU45O1lOMTA7WU4xMTtZTjEyIn0seyJwYXltZW50QW10UmFuZ2UiOiJbMC01MDAwMCkiLCJtb250aGx5SW5zdGFsbG1lbnRJbmZvIjoiWU4xIn1dfSx7ImNhcmRDb2RlIjoiMjciLCJjb25kaXRpb25zIjpbeyJwYXltZW50QW10UmFuZ2UiOiJbNTAwMDAtXSIsIm1vbnRobHlJbnN0YWxsbWVudEluZm8iOiJZTjE7WVkyO1lOMztZTjQ7WU41O1lONjtZTjc7WU44O1lOOTtZTjEwO1lOMTE7WU4xMiJ9LHsicGF5bWVudEFtdFJhbmdlIjoiWzAtNTAwMDApIiwibW9udGhseUluc3RhbGxtZW50SW5mbyI6IllOMSJ9XX0seyJjYXJkQ29kZSI6IjExIiwiY29uZGl0aW9ucyI6W3sicGF5bWVudEFtdFJhbmdlIjoiWzAtNTAwMDApIiwibW9udGhseUluc3RhbGxtZW50SW5mbyI6Ik5OMSJ9LHsicGF5bWVudEFtdFJhbmdlIjoiWzUwMDAwLV0iLCJtb250aGx5SW5zdGFsbG1lbnRJbmZvIjoiTk4xO05OMjtOTjM7Tk40O05ONTtOTjY7Tk43O05OODtOTjk7Tk4xMDtOTjExO05OMTIifV19LHsiY2FyZENvZGUiOiIwMiIsImNvbmRpdGlvbnMiOlt7InBheW1lbnRBbXRSYW5nZSI6IlswLTUwMDAwKSIsIm1vbnRobHlJbnN0YWxsbWVudEluZm8iOiJZTjEifSx7InBheW1lbnRBbXRSYW5nZSI6Ils1MDAwMC1dIiwibW9udGhseUluc3RhbGxtZW50SW5mbyI6IllOMTtZTjI7WU4zO1lONDtZTjU7WU42O1lONztZTjg7WU45O1lOMTA7WU4xMTtZTjEyIn1dfSx7ImNhcmRDb2RlIjoiMzciLCJjb25kaXRpb25zIjpbeyJwYXltZW50QW10UmFuZ2UiOiJbMC01MDAwMCkiLCJtb250aGx5SW5zdGFsbG1lbnRJbmZvIjoiWU4xIn0seyJwYXltZW50QW10UmFuZ2UiOiJbNTAwMDAtXSIsIm1vbnRobHlJbnN0YWxsbWVudEluZm8iOiJZTjE7WU4yO1lOMztZTjQ7WU41O1lONjtZTjc7WU44O1lOOTtZTjEwO1lOMTE7WU4xMiJ9XX0seyJjYXJkQ29kZSI6IjAzIiwiY29uZGl0aW9ucyI6W3sicGF5bWVudEFtdFJhbmdlIjoiWzAtNTAwMDApIiwibW9udGhseUluc3RhbGxtZW50SW5mbyI6IllOMSJ9LHsicGF5bWVudEFtdFJhbmdlIjoiWzUwMDAwLV0iLCJtb250aGx5SW5zdGFsbG1lbnRJbmZvIjoiWU4xO1lOMjtZTjM7WU40O1lONTtZTjY7WU43O1lOODtZTjk7WU4xMDtZTjExO1lOMTIifV19LHsiY2FyZENvZGUiOiIyMSIsImNvbmRpdGlvbnMiOlt7InBheW1lbnRBbXRSYW5nZSI6IlswLTUwMDAwKSIsIm1vbnRobHlJbnN0YWxsbWVudEluZm8iOiJZTjEifSx7InBheW1lbnRBbXRSYW5nZSI6Ils1MDAwMC1dIiwibW9udGhseUluc3RhbGxtZW50SW5mbyI6IllOMTtZTjI7WU4zO1lONDtZTjU7WU42O1lONztZTjg7WU45O1lOMTA7WU4xMTtZTjEyIn1dfSx7ImNhcmRDb2RlIjoiMjAiLCJjb25kaXRpb25zIjpbeyJwYXltZW50QW10UmFuZ2UiOiJbMC01MDAwMCkiLCJtb250aGx5SW5zdGFsbG1lbnRJbmZvIjoiWU4xIn0seyJwYXltZW50QW10UmFuZ2UiOiJbNTAwMDAtXSIsIm1vbnRobHlJbnN0YWxsbWVudEluZm8iOiJZTjE7WU4yO1lOMztZTjQ7WU41O1lONjtZTjc7WU44O1lOOTtZTjEwO1lOMTE7WU4xMiJ9XX0seyJjYXJkQ29kZSI6IjMyIiwiY29uZGl0aW9ucyI6W3sicGF5bWVudEFtdFJhbmdlIjoiWzAtNTAwMDApIiwibW9udGhseUluc3RhbGxtZW50SW5mbyI6IllOMSJ9LHsicGF5bWVudEFtdFJhbmdlIjoiWzUwMDAwLV0iLCJtb250aGx5SW5zdGFsbG1lbnRJbmZvIjoiWU4xO1lOMjtZTjM7WU40O1lONTtZTjY7WU43O1lOODtZTjk7WU4xMDtZTjExO1lOMTIifV19LHsiY2FyZENvZGUiOiIzMSIsImNvbmRpdGlvbnMiOlt7InBheW1lbnRBbXRSYW5nZSI6IlswLTUwMDAwKSIsIm1vbnRobHlJbnN0YWxsbWVudEluZm8iOiJZTjEifSx7InBheW1lbnRBbXRSYW5nZSI6Ils1MDAwMC1dIiwibW9udGhseUluc3RhbGxtZW50SW5mbyI6IllOMTtZTjI7WU4zO1lONDtZTjU7WU42O1lONztZTjg7WU45O1lOMTA7WU4xMTtZTjEyIn1dfV19fQ.m-mTozYRKlzcW4nvAn56r-DlpH_zf9orrrDqS8KnO5c";
        // @formatter:on
        SyrupPayTokenBuilder.uncheckHeaderOfToken();
        // When
        Token token = SyrupPayTokenBuilder.verify(t, "G3aIW7hYmlTjag3FDc63OGLNWwvagVUU");

        // Then
        assertThat(token, is(notNullValue()));
        assertThat(token.getClaim(SyrupPayToken.Claim.TO_SUBSCRIPTION), is(notNullValue()));
        assertThat(token.getClaim(SyrupPayToken.Claim.TO_LOGIN), is(notNullValue()));
        assertThat(token.getClaim(SyrupPayToken.Claim.TO_PAY), is(nullValue()));
    }


    @Test
    public void README_테스트_코드() throws Exception {
        String token = new SyrupPayTokenBuilder().of("가맹점 ID")
                .login()
                    .withMerchantUserId("가맹점의 회원 ID 또는 식별자")
                    .withExtraMerchantUserId("핸드폰과 같이 회원 별 추가 ID 체계가 존재할 경우 입력") // Optional
                    .withSsoCredential("발급 받은 SSO")
                .and()
                .mapToSyrupPayUser() // Optional 사용자 개인정보를 이용하여 시럽페이 사용자와 동일 여부 검증 시 사용
                    .withType(MapToSyrupPayUserConfigurer.MappingType.ENCRYPTED_PERSONAL_INFO)
                    .withValue(new MapToSyrupPayUserConfigurer.Personal("홍길동", "8110281", "10122223333"), "가맹점 ID", "가맹점에 전달한 비밀키")
                .and()
                .pay()
                    .withOrderIdOfMerchant("가맹점에서 관리하는 주문 ID") // 가맹점 Transaction Id = mctTransAuthId
                    .withProductTitle("제품명")
                    .withProductUrls(
                            "http://deal.11st.co.kr/product/SellerProductDetail.tmall?method=getSellerProductDetail&prdNo=1122841340",
                            "http://deal.11st.co.kr/product/SellerProductDetail.tmall?method=getSellerProductDetail&prdNo=1265508741"
                    ) // Optional
                    .withLanguageForDisplay(PayConfigurer.Language.KO)
                    .withAmount(50000)
                    .withCurrency(PayConfigurer.Currency.KRW)
                    .withShippingAddress(new PayConfigurer.ShippingAddress("137-332", "서초구 잠원동 하나아파트", "1동 1호", "서울", "", "kr")) // Optional
                    .withDeliveryPhoneNumber("01011112222") // Optional
                    .withDeliveryName("배송 수신자") // Optional
                    .withInstallmentPerCardInformation(new PayConfigurer.CardInstallmentInformation("카드구분 코드", "할부정보. ex. NN1;NN2;YY3;YY4;YY5;NH6")) // Optional
                    .withBeAbleToExchangeToCash(false) // Optional
                    .withRestrictionOf(PayConfigurer.PayableLocaleRule.ONLY_ALLOWED_KOR) // Optional
                    .withMerchantDefinedValue("{" +
                            "\"id_1\": \"value\"," +
                            "\"id_2\": 2" +
                            "}") // Optional, JSON 포맷 이용 시 Escape(\) 입력에 주의 필요, 1k 제한
                    .withRestrictionOf(PayConfigurer.MatchedUser.CI_MATCHED_ONLY) // Optional. 가맹점과 시럽페이 사용자 동일 여부 확인 시에만 필요
                .and()
                .generateTokenBy("가맹점의 전달한 비밀키");
        // @formatter:on
        System.out.printf(token);
        // When
        Token t = SyrupPayTokenBuilder.verify(token, "가맹점의 전달한 비밀키");

        // Then
        assertThat(t, is(notNullValue()));
        assertThat(t.getClaim(SyrupPayToken.Claim.TO_LOGIN), is(notNullValue()));
        assertThat(t.getClaim(SyrupPayToken.Claim.TO_MAP_USER), is(notNullValue()));
        assertThat(t.getUserInfoMapper().getPersonalIfNotExistThenNullWith("가맹점에 전달한 비밀키"), is(notNullValue()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void 개인정보_전달을위한_토큰규격_주민번호입력_6자리오류_테스트() throws Exception {
        // Given
        // @formatter:off
        String t = syrupPayTokenBuilder.of("가맹점")
                .mapToSyrupPayUser()
                .withType(MapToSyrupPayUserConfigurer.MappingType.ENCRYPTED_PERSONAL_INFO)
                .withValue(new MapToSyrupPayUserConfigurer.Personal("사용자", "123456", "휴대폰번호"), "가맹점 ID", "가맹점에게 전달한 비밀")
                .and()
                .generateTokenBy("가맹점에게 전달한 비밀키");
        // @formatter:on
        System.out.printf(t);
        // When
        Token token = SyrupPayTokenBuilder.verify(t, "가맹점에게 전달한 비밀키");

        // Then
        assertThat(token, is(notNullValue()));
        assertThat(token.getClaim(SyrupPayToken.Claim.TO_LOGIN), is(notNullValue()));
        assertThat(token.getClaim(SyrupPayToken.Claim.TO_MAP_USER), is(notNullValue()));
        assertThat(token.getUserInfoMapper().getPersonalIfNotExistThenNullWith("가맹점에게 전달한 비밀"), is(notNullValue()));
    }
}