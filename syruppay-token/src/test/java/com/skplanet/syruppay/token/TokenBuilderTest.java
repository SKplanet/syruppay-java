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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skplanet.syruppay.token.claims.MapToUserClaim;
import com.skplanet.syruppay.token.claims.PayClaim;
import com.skplanet.syruppay.token.claims.SubscriptionClaim;
import com.skplanet.syruppay.token.domain.Mocks;
import com.skplanet.syruppay.token.domain.TokenHistories;
import com.skplanet.syruppay.token.jwt.SyrupPayToken;
import com.skplanet.syruppay.token.jwt.Token;
import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.security.spec.KeySpec;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

public class TokenBuilderTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    TokenBuilder tokenBuilder;

    @Before
    public void setUp() throws Exception {
        tokenBuilder = new TokenBuilder();
    }

    @Test
    public void 기본생성자_테스트() throws Exception {
        // Give
        TokenBuilder t = new TokenBuilder();

        // When

        // Then
        assertThat(t, is(notNullValue()));
        assertThat(t.getClass().getName(), is(TokenBuilder.class.getName()));
    }

    @Test
    public void MctAccToken_형식으로_회원정보_없이_생성() throws Exception {
        // Give
        tokenBuilder.of("가맹점");

        // When
        String s = tokenBuilder.generateTokenBy("keys");

        // Then
        assertThat(s, is(notNullValue()));
        assertThat(s.length(), is(not(0)));
    }

    @Test
    public void MctAccToken_형식으로_회원정보_포함하여_생성() throws Exception {
        // Give
        tokenBuilder.of("가맹점").login().withMerchantUserId("가맹점의 회원 ID 또는 식별자").withExtraMerchantUserId("핸드폰과 같이 회원 별 추가 ID 체계가 존재할 경우 입력").withSsoCredential("SSO 를 발급 받았을 경우 입력");

        // When
        String s = tokenBuilder.generateTokenBy("keys");

        // Then
        assertThat(s, is(notNullValue()));
        assertThat(s.length(), is(not(0)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void iss_미입력_후_빌드() throws Exception {
        // Give
        TokenBuilder t = new TokenBuilder();

        // When
        t.build();

        // Then
    }

    @Test
    public void 유효시간_기본값으로_입력_후_10분_여부_검증() throws Exception {
        // Give
        tokenBuilder.of("test");

        // When

        // Then
        Token t = OBJECT_MAPPER.readValue(tokenBuilder.toJson(), SyrupPayToken.class);
        assertThat(t, is(notNullValue()));
        assertThat(t.getExp() - t.getIat(), is((long) (10 * 60)));
    }

    @Test
    public void 유효시간_60분으로_입력_후_검증() throws Exception {
        // Give
        tokenBuilder.of("test");

        // When
        tokenBuilder.expiredMinutes(60);

        // Then
        Token t = OBJECT_MAPPER.readValue(tokenBuilder.toJson(), SyrupPayToken.class);
        assertThat(t, is(notNullValue()));
        assertThat(t.getExp() - t.getIat(), is((long) (60 * 60)));
    }

    @Test
    public void 유효시간_0분으로_입력_후_검증() throws Exception {
        // Give
        tokenBuilder.of("test");

        // When
        tokenBuilder.expiredMinutes(0);

        // Then
        Token t = OBJECT_MAPPER.readValue(tokenBuilder.toJson(), SyrupPayToken.class);
        assertThat(t, is(notNullValue()));
        assertThat(t.getExp(), is(t.getIat()));
    }

    @Test
    public void 유효시간_마이너스_1분으로_입력_후_만료여부_검증() throws Exception {
        // Give
        tokenBuilder.of("test");

        // When
        tokenBuilder.expiredMinutes(-1);

        // Then
        Token t = OBJECT_MAPPER.readValue(tokenBuilder.toJson(), SyrupPayToken.class);
        assertThat(t, is(notNullValue()));
        assertThat(t.isValidInTime(), is(false));
    }

    @Test
    public void 시럽페이_사용자_매칭_정보_입력() throws Exception {
        // Give
        // @formatter:off
        tokenBuilder.of("가맹점")
                .login()
                    .withMerchantUserId("가맹점의 회원 ID 또는 식별자")
                    .withExtraMerchantUserId("핸드폰과 같이 회원 별 추가 ID 체계가 존재할 경우 입력")
                    .withSsoCredential("SSO 를 발급 받았을 경우 입력")
                .and()
                .mapToUser()
                    .withType(MapToUserClaim.MappingType.CI_MAPPED_KEY)
                    .withValue("4987234")
                    .withIdentityAuthenticationId("bddb74b0-981f-4070-8c02-0cdf324f46f6"); // Optional
        // @formatter:on
        // When
        String t = tokenBuilder.generateTokenBy("가맹점에게 전달한 비밀키");
        System.out.println(t);
        // Then
        assertThat(t, is(notNullValue()));
        assertThat(t.length(), is(not(0)));
    }

    @Test(expected = AlreadyBuiltException.class)
    public void 이미_생성한_토큰빌드를_재활용() throws Exception {
        // Give
        tokenBuilder.of("가맹점").generateTokenBy("가맹점에게 전달한 비밀키");

        // When
        tokenBuilder.generateTokenBy("가망점에게 전달한 비밀키");

        // Then
        // throw exception
    }

    @Test
    public void 구매를_위한_토큰_생성() throws Exception {
        // Give
        // @formatter:off
        tokenBuilder.of("가맹점")
                            .pay()
                                .withOrderIdOfMerchant("가맹점에서 관리하는 주문 ID")
                                .withProductTitle("제품명")
                                .withLanguageForDisplay(PayClaim.Language.KO)
                                .withAmount(50000)
                                .withCurrency(PayClaim.Currency.KRW)
                                .withShippingAddress(new PayClaim.ShippingAddress("137-332", "서초구 잠원동 하나아파트", "1동 1호", "서울", "", "KR"))
                                .withDeliveryPhoneNumber("01011112222")
                                .withDeliveryName("배송 수신자")
                                .withInstallmentPerCardInformation(new PayClaim.CardInstallmentInformation("카드구분 코드", "할부정보. ex. NN1;NN2;YY3;YY4;YY5;NH6"))
                                .withBeAbleToExchangeToCash(false)
                                .withPayableRuleWithCard(PayClaim.PayableLocaleRule.ONLY_ALLOWED_KOR)
                ;
        // @formatter:on
        // When
        String t = tokenBuilder.generateTokenBy("가맹점에게 전달한 비밀키");

        // Then
        assertThat(t, is(notNullValue()));
        assertThat(t.length(), is(not(0)));
    }

    @Test
    public void 토큰_복호화() throws Exception {
        // Given
        // @formatter:off
        String t = tokenBuilder.of("가맹점")
                .login()
                    .withMerchantUserId("가맹점의 회원 ID 또는 식별자")
                    .withExtraMerchantUserId("핸드폰과 같이 회원 별 추가 ID 체계가 존재할 경우 입력")
                    .withSsoCredential("SSO 를 발급 받았을 경우 입력")
                .and()
                .generateTokenBy("가맹점에게 전달한 비밀키");
        // @formatter:on

        // When
        Token token = TokenBuilder.verify(t, "가맹점에게 전달한 비밀키");

        // Then
        assertThat(token, is(notNullValue()));
        assertThat(token.isValidInTime(), is(true));
        assertThat(token.getIss(), is("가맹점"));
    }


    @Test(expected = IllegalArgumentException.class)
    public void 가맹점_회원ID_미입력_후_토큰_생성() throws Exception {
        // Given
        tokenBuilder.of("가맹점").login().withSsoCredential("SSO 를 발급 받았을 경우 입력");

        // When
        String token = tokenBuilder.generateTokenBy("가맹점에게 전달한 비밀키");

        // Then
        // throw Exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void 구매_시_구매_상품명_미입력_후_토큰_생성() throws Exception {
        // @formatter:off
        tokenBuilder.of("가맹점")
                            .pay()
                                .withOrderIdOfMerchant("가맹점에서 관리하는 주문 ID")
                                //.withProductTitle("제품명")
                                .withLanguageForDisplay(PayClaim.Language.KO)
                                .withAmount(50000)
                                .withCurrency(PayClaim.Currency.KRW)
                                .withShippingAddress(new PayClaim.ShippingAddress("137-332", "서초구 잠원동 하나아파트", "1동 1호", "서울", "", "KR"))
                                .withDeliveryPhoneNumber("01011112222")
                                .withDeliveryName("배송 수신자")
                                .withInstallmentPerCardInformation(new PayClaim.CardInstallmentInformation("카드구분 코드", "할부정보. ex. NN1;NN2;YY3;YY4;YY5;NH6"))
                                .withBeAbleToExchangeToCash(false)
                                .withPayableRuleWithCard(PayClaim.PayableLocaleRule.ONLY_ALLOWED_KOR)
                ;
        // @formatter:on
        // When
        String t = tokenBuilder.generateTokenBy("가맹점에게 전달한 비밀키");

        // Then
        // throw Exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void 구매_시_구매금액_미입력_후_토큰_생성() throws Exception {
        // @formatter:off
        tokenBuilder.of("가맹점")
                            .pay()
                                .withOrderIdOfMerchant("가맹점에서 관리하는 주문 ID")
                                .withProductTitle("제품명")
                                .withLanguageForDisplay(PayClaim.Language.KO)
                                //.withAmount(50000)
                                .withCurrency(PayClaim.Currency.KRW)
                                .withShippingAddress(new PayClaim.ShippingAddress("137-332", "서초구 잠원동 하나아파트", "1동 1호", "서울", "", "KR"))
                                .withDeliveryPhoneNumber("01011112222")
                                .withDeliveryName("배송 수신자")
                                .withInstallmentPerCardInformation(new PayClaim.CardInstallmentInformation("카드구분 코드", "할부정보. ex. NN1;NN2;YY3;YY4;YY5;NH6"))
                                .withBeAbleToExchangeToCash(false)
                                .withPayableRuleWithCard(PayClaim.PayableLocaleRule.ONLY_ALLOWED_KOR)
                ;
        // @formatter:on
        // When
        String t = tokenBuilder.generateTokenBy("가맹점에게 전달한 비밀키");

        // Then
        // throw Exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void 구매_시_구매금액_마이너스_입력_후_토큰_생성() throws Exception {
        // @formatter:off
        tokenBuilder.of("가맹점")
                            .pay()
                                .withOrderIdOfMerchant("가맹점에서 관리하는 주문 ID")
                                .withProductTitle("제품명")
                                .withLanguageForDisplay(PayClaim.Language.KO)
                                .withAmount(-1)
                                .withCurrency(PayClaim.Currency.KRW)
                                .withShippingAddress(new PayClaim.ShippingAddress("137-332", "서초구 잠원동 하나아파트", "1동 1호", "서울", "", "KR"))
                                .withDeliveryPhoneNumber("01011112222")
                                .withDeliveryName("배송 수신자")
                                .withInstallmentPerCardInformation(new PayClaim.CardInstallmentInformation("카드구분 코드", "할부정보. ex. NN1;NN2;YY3;YY4;YY5;NH6"))
                                .withBeAbleToExchangeToCash(false)
                                .withPayableRuleWithCard(PayClaim.PayableLocaleRule.ONLY_ALLOWED_KOR)
                ;
        // @formatter:on
        // When
        String t = tokenBuilder.generateTokenBy("가맹점에게 전달한 비밀키");

        // Then
        // throw Exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void 구매_시_구매금액_0_입력_후_토큰_생성() throws Exception {
        // @formatter:off
        tokenBuilder.of("가맹점")
                            .pay()
                                .withOrderIdOfMerchant("가맹점에서 관리하는 주문 ID")
                                .withProductTitle("제품명")
                                .withLanguageForDisplay(PayClaim.Language.KO)
                                .withAmount(0)
                                .withCurrency(PayClaim.Currency.KRW)
                                .withShippingAddress(new PayClaim.ShippingAddress("137-332", "서초구 잠원동 하나아파트", "1동 1호", "서울", "", "KR"))
                                .withDeliveryPhoneNumber("01011112222")
                                .withDeliveryName("배송 수신자")
                                .withInstallmentPerCardInformation(new PayClaim.CardInstallmentInformation("카드구분 코드", "할부정보. ex. NN1;NN2;YY3;YY4;YY5;NH6"))
                                .withBeAbleToExchangeToCash(false)
                                .withPayableRuleWithCard(PayClaim.PayableLocaleRule.ONLY_ALLOWED_KOR)
                ;
        // @formatter:on
        // When
        String t = tokenBuilder.generateTokenBy("가맹점에게 전달한 비밀키");

        // Then
        // throw Exception
    }

    @Test
    public void 구매_시_구매금액_통화단위_미입력_후_토큰_생성() throws Exception {
        // @formatter:off
        tokenBuilder.of("가맹점")
                            .pay()
                                .withOrderIdOfMerchant("가맹점에서 관리하는 주문 ID")
                                .withProductTitle("제품명")
                                .withLanguageForDisplay(PayClaim.Language.KO)
                                .withAmount(500000)
                                //.withCurrency(PayClaim.Currency.KRW)
                                .withShippingAddress(new PayClaim.ShippingAddress("137-332", "서초구 잠원동 하나아파트", "1동 1호", "서울", "", "KR"))
                                .withDeliveryPhoneNumber("01011112222")
                                .withDeliveryName("배송 수신자")
                                .withInstallmentPerCardInformation(new PayClaim.CardInstallmentInformation("카드구분 코드", "할부정보. ex. NN1;NN2;YY3;YY4;YY5;NH6"))
                                .withBeAbleToExchangeToCash(false)
                                .withPayableRuleWithCard(PayClaim.PayableLocaleRule.ONLY_ALLOWED_KOR)
                ;
        // @formatter:on
        // When
        Token token = TokenBuilder.verify(tokenBuilder.generateTokenBy("가맹점에게 전달한 비밀키"), "가맹점에게 전달한 비밀키");

        // Then
        assertThat(token.getTransactionInfo().getPaymentInfo().getCurrencyCode(), is("KRW"));
    }

    @Test
    public void 구매_시_언어_미입력_후_토큰_생성() throws Exception {
        // @formatter:off
        tokenBuilder.of("가맹점")
                            .pay()
                                .withOrderIdOfMerchant("가맹점에서 관리하는 주문 ID")
                                .withProductTitle("제품명")
                                //.withLanguageForDisplay(PayClaim.Language.KO)
                                .withAmount(5550)
                                .withCurrency(PayClaim.Currency.KRW)
                                .withShippingAddress(new PayClaim.ShippingAddress("137-332", "서초구 잠원동 하나아파트", "1동 1호", "서울", "", "KR"))
                                .withDeliveryPhoneNumber("01011112222")
                                .withDeliveryName("배송 수신자")
                                .withInstallmentPerCardInformation(new PayClaim.CardInstallmentInformation("카드구분 코드", "할부정보. ex. NN1;NN2;YY3;YY4;YY5;NH6"))
                                .withBeAbleToExchangeToCash(false)
                                .withPayableRuleWithCard(PayClaim.PayableLocaleRule.ONLY_ALLOWED_KOR)
                ;
        // @formatter:on
        // When
        Token token = TokenBuilder.verify(tokenBuilder.generateTokenBy("가맹점에게 전달한 비밀키"), "가맹점에게 전달한 비밀키");

        // Then
        assertThat(token.getTransactionInfo().getPaymentInfo().getLang(), is("KO"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void 구매_시_구매금액_배송지_입력_후_토큰_생성() throws Exception {
        // @formatter:off
        tokenBuilder.of("가맹점")
                            .pay()
                                .withOrderIdOfMerchant("가맹점에서 관리하는 주문 ID")
                                .withProductTitle("제품명")
                                .withLanguageForDisplay(PayClaim.Language.KO)
                                .withAmount(0)
                                .withCurrency(PayClaim.Currency.KRW)
                                //.withShippingAddress(new PayClaim.ShippingAddress("137-332", "서초구 잠원동 하나아파트", "1동 1호", "서울", "", "KR"))
                                .withDeliveryPhoneNumber("01011112222")
                                .withDeliveryName("배송 수신자")
                                .withInstallmentPerCardInformation(new PayClaim.CardInstallmentInformation("카드구분 코드", "할부정보. ex. NN1;NN2;YY3;YY4;YY5;NH6"))
                                .withBeAbleToExchangeToCash(false)
                                .withPayableRuleWithCard(PayClaim.PayableLocaleRule.ONLY_ALLOWED_KOR)
                ;
        // @formatter:on
        // When
        String t = tokenBuilder.generateTokenBy("가맹점에게 전달한 비밀키");

        // Then
        // throw Exception
    }

    @Test
    public void 구매_시_가맹점_주문ID_40자_입력_후_토큰_생성() throws Exception {
        // @formatter:off
        tokenBuilder.of("가맹점")
                            .pay()
                                .withOrderIdOfMerchant("1234567890123456789012345678901234567890")
                                .withProductTitle("제품명")
                                .withLanguageForDisplay(PayClaim.Language.KO)
                                .withAmount(50000)
                                .withCurrency(PayClaim.Currency.KRW)
                                .withShippingAddress(new PayClaim.ShippingAddress("137-332", "서초구 잠원동 하나아파트", "1동 1호", "서울", "", "KR"))
                                .withDeliveryPhoneNumber("01011112222")
                                .withDeliveryName("배송 수신자")
                                .withInstallmentPerCardInformation(new PayClaim.CardInstallmentInformation("카드구분 코드", "할부정보. ex. NN1;NN2;YY3;YY4;YY5;NH6"))
                                .withBeAbleToExchangeToCash(false)
                                .withPayableRuleWithCard(PayClaim.PayableLocaleRule.ONLY_ALLOWED_KOR)
                ;
        // @formatter:on
        // When
        Token token = TokenBuilder.verify(tokenBuilder.generateTokenBy("가맹점에게 전달한 비밀키"), "가맹점에게 전달한 비밀키");

        // Then
        assertThat(token.getTransactionInfo().getMctTransAuthId().length(), is(40));
    }

    @Test(expected = IllegalArgumentException.class)
    public void 구매_시_가맹점_주문ID_41자_입력_후_토큰_생성() throws Exception {
        // @formatter:off
        tokenBuilder.of("가맹점")
                            .pay()
                                .withOrderIdOfMerchant("12345678901234567890123456789012345678901")
                                .withProductTitle("제품명")
                                .withLanguageForDisplay(PayClaim.Language.KO)
                                .withAmount(50000)
                                .withCurrency(PayClaim.Currency.KRW)
                                .withShippingAddress(new PayClaim.ShippingAddress("137-332", "서초구 잠원동 하나아파트", "1동 1호", "서울", "", "KR"))
                                .withDeliveryPhoneNumber("01011112222")
                                .withDeliveryName("배송 수신자")
                                .withInstallmentPerCardInformation(new PayClaim.CardInstallmentInformation("카드구분 코드", "할부정보. ex. NN1;NN2;YY3;YY4;YY5;NH6"))
                                .withBeAbleToExchangeToCash(false)
                                .withPayableRuleWithCard(PayClaim.PayableLocaleRule.ONLY_ALLOWED_KOR)
                ;
        // @formatter:on
        // When
        String t = tokenBuilder.generateTokenBy("가맹점에게 전달한 비밀키");

        // Then
        // throw Exception
    }

    @Test
    public void 구매_시_제품_상세_URL_추가_토큰_생성() throws Exception {
        // @formatter:off
        tokenBuilder.of("가맹점")
                            .pay()
                                .withOrderIdOfMerchant("1234567890123456789012345678901234567890")
                                .withProductTitle("제품명")
                                .withProductUrls("http://www.11st.co.kr/product/SellerProductDetail.tmall?method=getSellerProductDetail&prdNo=1354119088&trTypeCd=22&trCtgrNo=895019")
                                .withMerchantDefinedValue("{\n" +
                                                            "\"id_1\": \"value\",\n" +
                                                            "\"id_2\": 2\n" +
                                                            "}")
                                .withLanguageForDisplay(PayClaim.Language.KO)
                                .withAmount(50000)
                                .withCurrency(PayClaim.Currency.KRW)
                                .withShippingAddress(new PayClaim.ShippingAddress("137-332", "서초구 잠원동 하나아파트", "1동 1호", "서울", "", "KR"))
                                .withDeliveryPhoneNumber("01011112222")
                                .withDeliveryName("배송 수신자")
                                .withInstallmentPerCardInformation(new PayClaim.CardInstallmentInformation("카드구분 코드", "할부정보. ex. NN1;NN2;YY3;YY4;YY5;NH6"))
                                .withBeAbleToExchangeToCash(false)
                                .withPayableRuleWithCard(PayClaim.PayableLocaleRule.ONLY_ALLOWED_KOR)
                ;
        // @formatter:on
        // When
        Token token = TokenBuilder.verify(tokenBuilder.generateTokenBy("가맹점에게 전달한 비밀키"), "가맹점에게 전달한 비밀키");

        // Then
        assertThat(token.getTransactionInfo().getPaymentInfo().getProductUrls().size(), is(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void 구매_시_제품_상세_URL에_HTTP가_아닌_값_입력_후_토큰_생성() throws Exception {
        // @formatter:off
        tokenBuilder.of("가맹점")
                            .pay()
                                .withOrderIdOfMerchant("1234567890123456789012345678901234567890")
                                .withProductTitle("제품명")
                                .withProductUrls("h://www.11st.co.kr/product/SellerProductDetail.tmall?method=getSellerProductDetail&prdNo=1354119088&trTypeCd=22&trCtgrNo=895019")
                                .withLanguageForDisplay(PayClaim.Language.KO)
                                .withAmount(50000)
                                .withCurrency(PayClaim.Currency.KRW)
                                .withShippingAddress(new PayClaim.ShippingAddress("137-332", "서초구 잠원동 하나아파트", "1동 1호", "서울", "", "KR"))
                                .withDeliveryPhoneNumber("01011112222")
                                .withDeliveryName("배송 수신자")
                                .withInstallmentPerCardInformation(new PayClaim.CardInstallmentInformation("카드구분 코드", "할부정보. ex. NN1;NN2;YY3;YY4;YY5;NH6"))
                                .withBeAbleToExchangeToCash(false)
                                .withPayableRuleWithCard(PayClaim.PayableLocaleRule.ONLY_ALLOWED_KOR)
                ;
        // @formatter:on
        // When
        Token token = TokenBuilder.verify(tokenBuilder.generateTokenBy("가맹점에게 전달한 비밀키"), "가맹점에게 전달한 비밀키");

        // Then
        // throw exception
    }

    @Test
    public void 토큰_시럽페이사용자연동_추가_후_복호화() throws Exception {
        // Given
        // @formatter:off
        String t = tokenBuilder.of("가맹점")
                .login()
                    .withMerchantUserId("가맹점의 회원 ID 또는 식별자")
                    .withExtraMerchantUserId("핸드폰과 같이 회원 별 추가 ID 체계가 존재할 경우 입력")
                    .withSsoCredential("SSO 를 발급 받았을 경우 입력")
                .and()
                .mapToUser().withType(MapToUserClaim.MappingType.CI_HASH).withValue("asdkfjhsakdfj")
                .and()
                .generateTokenBy("가맹점에게 전달한 비밀키");
        // @formatter:on

        // When
        Token token = TokenBuilder.verify(t, "가맹점에게 전달한 비밀키");

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
        tokenBuilder.of("가맹점")
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
        Token token = TokenBuilder.verify(tokenBuilder.generateTokenBy("가맹점에게 전달한 비밀키"), "가맹점에게 전달한 비밀키");

        // Then
        assertThat(token.getCheckoutInfo(), is(notNullValue()));
        assertThat(token.getCheckoutInfo().getProductPrice(), is(5000));
    }

    @Test
    public void 하위버전_1_2_30_호환_테스트() throws IOException, InvalidTokenException {
        TokenBuilder.uncheckHeaderOfToken();
        Token t = TokenBuilder.verify(TokenHistories.VERSION_1_2_30.token, TokenHistories.VERSION_1_2_30.key);
        System.out.println(new ObjectMapper().writeValueAsString(t));
    }

    @Test
    public void C_샵버전_0_0_1_호환_테스트() throws IOException, InvalidTokenException {
        TokenBuilder.uncheckHeaderOfToken();
        Token t = TokenBuilder.verify(TokenHistories.C_SHARP_0_0_1.token, TokenHistories.C_SHARP_0_0_1.key);
        System.out.println(new ObjectMapper().writeValueAsString(t));
    }

    @Test
    public void 하위버전_1_3_4_버전_CJOSHOPPING_테스트() throws Exception {
        TokenBuilder.uncheckHeaderOfToken();
        Token t = TokenBuilder.verify(TokenHistories.VERSION_1_3_4_BY_CJOSHOPPING.token, TokenHistories.VERSION_1_3_4_BY_CJOSHOPPING.key);
        assertThat(t.getTransactionInfo().getMctTransAuthId(), is(notNullValue()));
        assertThat(t.getTransactionInfo().getPaymentRestrictions().getCardIssuerRegion(), is(notNullValue()));
    }

    @Test(expected = Exception.class)
    public void 체크아웃_잘못된_규격_테스트() throws IOException, InvalidTokenException {
        TokenBuilder.uncheckHeaderOfToken();
        Token t = TokenBuilder.verify(TokenHistories.VERSION_1_3_5_INVALID.token, TokenHistories.VERSION_1_3_5_INVALID.key);
        assertThat(t.getTransactionInfo().getMctTransAuthId(), is(notNullValue()));
        assertThat(t.getTransactionInfo().getPaymentRestrictions().getCardIssuerRegion(), is(notNullValue()));
    }

    @Test
    public void PHP_버전_사용자로그인_토큰_호환성_테스트() throws IOException, InvalidTokenException {
        TokenBuilder.uncheckHeaderOfToken();
        Token t = TokenBuilder.verify(TokenHistories.PHP_TO_LOGIN_VERSION_1_0_0.token, TokenHistories.PHP_TO_LOGIN_VERSION_1_0_0.key);
        System.out.println(new ObjectMapper().writeValueAsString(t));
    }

    @Test
    public void PHP_버전_결제_토큰_호환성_테스트() throws IOException, InvalidTokenException {
        TokenBuilder.uncheckHeaderOfToken();
        Token t = TokenBuilder.verify(TokenHistories.PHP_TO_PAY_VERSION_1_0_0.token, TokenHistories.PHP_TO_PAY_VERSION_1_0_0.key);
        System.out.println(new ObjectMapper().writeValueAsString(t));
    }

    @Test
    public void 자동결제를_위한_인증_토큰_생성() throws Exception {
        // Given
        // @formatter:off
        String t = tokenBuilder.of("가맹점")
                .login()
                    .withMerchantUserId("가맹점의 회원 ID 또는 식별자")
                    .withExtraMerchantUserId("핸드폰과 같이 회원 별 추가 ID 체계가 존재할 경우 입력")
                    .withSsoCredential("SSO 를 발급 받았을 경우 입력")
                .and()
                .subscription()
                    .withAutoPaymentId("시럽페이로부터 발급받은 자동결제 ID") // Optional
                    .withRestrictionOf(PayClaim.MatchedUser.CI_MATCHED_ONLY) // Optional
                    .withMerchantSubscriptionRequestId("가맹점에서 다시 전달받을 ID 문자열") // Optional
                    .with(new SubscriptionClaim.Plan(SubscriptionClaim.Interval.WEEKLY, "결제명"))
                    .withPromotionCode("PROMOTION_CODE_001")
                .and()
                .generateTokenBy("가맹점에게 전달한 비밀키");
        // @formatter:on
        System.out.printf(t);
        // When
        Token token = TokenBuilder.verify(t, "가맹점에게 전달한 비밀키");

        // Then
        assertThat(token, is(notNullValue()));
        assertThat(token.isValidInTime(), is(true));
        assertThat(token.getIss(), is("가맹점"));
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
    public void 자동결제를_위한_인증_토큰_생성_후_클래임_확인_테스트() throws Exception {
        // Given
        // @formatter:off
        String t = tokenBuilder.of("가맹점")
                .login()
                    .withMerchantUserId("가맹점의 회원 ID 또는 식별자")
                    .withExtraMerchantUserId("핸드폰과 같이 회원 별 추가 ID 체계가 존재할 경우 입력")
                    .withSsoCredential("SSO 를 발급 받았을 경우 입력")
                .and()
                .subscription()
                    .withAutoPaymentId("시럽페이로부터 발급받은 자동결제 ID") // Optional
                    .withRestrictionOf(PayClaim.MatchedUser.CI_MATCHED_ONLY) // Optional
                .and()
                .generateTokenBy("가맹점에게 전달한 비밀키");
        // @formatter:on
        System.out.printf(t);
        // When
        Token token = TokenBuilder.verify(t, "가맹점에게 전달한 비밀키");

        // Then
        assertThat(token, is(notNullValue()));
        assertThat(token.getClaims(Token.Claim.TO_SUBSCRIPTION).size(), is(1));
        assertThat(token.getClaims(Token.Claim.TO_LOGIN).size(), is(1));
        assertThat(token.getClaims(Token.Claim.TO_PAY).size(), is(0));
    }

    @Test
    public void 자동결제를_위한_인증_토큰_생성_후_개별_클래임_확인_테스트() throws Exception {
        // Given
        // @formatter:off
        String t = tokenBuilder.of("가맹점")
                .login()
                    .withMerchantUserId("가맹점의 회원 ID 또는 식별자")
                    .withExtraMerchantUserId("핸드폰과 같이 회원 별 추가 ID 체계가 존재할 경우 입력")
                    .withSsoCredential("SSO 를 발급 받았을 경우 입력")
                .and()
                .subscription()
                    .withAutoPaymentId("시럽페이로부터 발급받은 자동결제 ID") // Optional
                    .withRestrictionOf(PayClaim.MatchedUser.CI_MATCHED_ONLY) // Optional
                .and()
                .generateTokenBy("가맹점에게 전달한 비밀키");
        // @formatter:on
        System.out.printf(t);
        // When
        Token token = TokenBuilder.verify(t, "가맹점에게 전달한 비밀키");

        // Then
        assertThat(token, is(notNullValue()));
        assertThat(token.getClaim(Token.Claim.TO_SUBSCRIPTION), is(notNullValue()));
        assertThat(token.getClaim(Token.Claim.TO_LOGIN), is(notNullValue()));
        assertThat(token.getClaim(Token.Claim.TO_PAY), is(nullValue()));
    }

    @Test
    public void 개인정보_전달을위한_토큰규격_테스트() throws Exception {
        // Given
        // @formatter:off
        String t = tokenBuilder.of("가맹점")
                .login()
                    .withMerchantUserId("가맹점의 회원 ID 또는 식별자")
                    .withExtraMerchantUserId("핸드폰과 같이 회원 별 추가 ID 체계가 존재할 경우 입력")
                    .withSsoCredential("SSO 를 발급 받았을 경우 입력")
                .and()
                .mapToUser()
                    .withType(MapToUserClaim.MappingType.ENCRYPTED_PERSONAL_INFO)
                    .withValue(new MapToUserClaim.Personal("사용자", "1234567", "휴대폰번호"), "가맹점 ID", "가맹점에게 전달한 비밀")
                .and()
                .generateTokenBy("가맹점에게 전달한 비밀키");
        // @formatter:on
        System.out.printf(t);
        // When
        Token token = TokenBuilder.verify(t, "가맹점에게 전달한 비밀키");

        // Then
        assertThat(token, is(notNullValue()));
        assertThat(token.getClaim(Token.Claim.TO_LOGIN), is(notNullValue()));
        assertThat(token.getClaim(Token.Claim.TO_MAP_USER), is(notNullValue()));
        assertThat(token.getUserInfoMapper().getPersonalIfNotExistThenNullWith("가맹점에게 전달한 비밀"), is(notNullValue()));
    }

    @Test
    public void 원클릭_토큰_유효성검증_테스트() throws Exception {
        // Given
        // @formatter:off
        String t = TokenHistories.VALIDATION_BACKWARD_OF_11ST.token;
        // @formatter:on

        TokenBuilder.uncheckHeaderOfToken();

        // When
        Token token = TokenBuilder.verify(t, TokenHistories.VALIDATION_BACKWARD_OF_11ST.key);

        // Then
        assertThat(token, is(notNullValue()));
        assertThat(token.getClaim(Token.Claim.TO_SUBSCRIPTION), is(nullValue()));
        assertThat(token.getClaim(Token.Claim.TO_LOGIN), is(notNullValue()));
        assertThat(token.getClaim(Token.Claim.TO_CHECKOUT), is(notNullValue()));
    }


    @Test
    public void README_테스트_코드() throws Exception {
        // @formatter:off
        String token =
        new TokenBuilder().of("가맹점 ID")
            .login()
                .withMerchantUserId("가맹점의 회원 ID 또는 식별자")
                .withExtraMerchantUserId("핸드폰과 같이 회원 별 추가 ID 체계가 존재할 경우 입력") // Optional
                .withSsoCredential("발급 받은 SSO")
            .and()
            .mapToUser() // Optional 사용자 개인정보를 이용하여 시럽페이 사용자와 동일 여부 검증 시 사용
                .withType(MapToUserClaim.MappingType.ENCRYPTED_PERSONAL_INFO)
                .withValue(new MapToUserClaim.Personal()
                        .setUsername("홍길동")
                        .setSsnFirst7Digit("8011221")
                        .setLineNumber("01022223333")
                        .setOperatorCode(MapToUserClaim.OperatorCode.SKT)
                        .setCiHash("HHHHHHAAAAAAAAAAAASSSSSSSSSSSSSSHHHHHHHHHHH")
                        .setEmail("test@mail.com")
                        .setPayableCard(
                                new MapToUserClaim.PayableCard()
                                        .setCardNo("카드번호")
                                        .setExpireDate("202012")
                                        .setCardName("카드이름")
                                        .setCardIssuerName("발급사명")
                                        .setCardIssuer("발급사코드")
                                        .setCardAcquirer("매입사코드")
                                        .setCardType(MapToUserClaim.CardType.CREDIT))
                        , "가맹점 ID", "가맹점에 전달한 비밀키")
            .and()
            .pay()
                .withOrderIdOfMerchant("가맹점에서 관리하는 주문 ID") // 가맹점 Transaction Id = mctTransAuthId
                .withProductTitle("제품명")
                .withProductUrls(
                        "http://deal.11st.co.kr/product/SellerProductDetail.tmall?method=getSellerProductDetail&prdNo=1122841340",
                        "http://deal.11st.co.kr/product/SellerProductDetail.tmall?method=getSellerProductDetail&prdNo=1265508741"
                ) // Optional
                .withLanguageForDisplay(PayClaim.Language.KO)
                .withAmount(50000)
                .withCurrency(PayClaim.Currency.KRW)
                .withShippingAddress(new PayClaim.ShippingAddress("137-332", "서초구 잠원동 하나아파트", "1동 1호", "서울", "", "kr")) // Optional
                .withDeliveryPhoneNumber("01011112222") // Optional
                .withDeliveryName("배송 수신자") // Optional
                .withInstallmentPerCardInformation(new PayClaim.CardInstallmentInformation("카드구분 코드", "할부정보. ex. NN1;NN2;YY3;YY4;YY5;NH6")) // Optional
                .withBeAbleToExchangeToCash(false) // Optional
                .withRestrictionOf(PayClaim.PayableLocaleRule.ONLY_ALLOWED_KOR) // Optional
                .withRestrictionPaymentTypeOf("CARD;MOBILE;BANK") // Optional
                .withMerchantDefinedValue("{" +
                        "\"id_1\": \"value\"," +
                        "\"id_2\": 2" +
                        "}") // Optional, JSON 포맷 이용 시 Escape(\) 입력에 주의 필요, 1k 제한
                .withRestrictionOf(PayClaim.MatchedUser.CI_MATCHED_ONLY) // Optional. 가맹점과 시럽페이 사용자 동일 여부 확인 시에만 필요
            .and()
            .generateTokenBy("가맹점의 전달한 비밀키");
        // @formatter:on
        System.out.printf(token);
        // When
        Token t = TokenBuilder.verify(token, "가맹점의 전달한 비밀키");

        // Then
        assertThat(t, is(notNullValue()));
        assertThat(t.getClaim(Token.Claim.TO_LOGIN), is(notNullValue()));
        assertThat(t.getClaim(Token.Claim.TO_MAP_USER), is(notNullValue()));
        assertThat(t.getUserInfoMapper().getPersonalIfNotExistThenNullWith("가맹점에 전달한 비밀키"), is(notNullValue()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void 개인정보_전달을위한_토큰규격_주민번호입력_6자리오류_테스트() throws Exception {
        // Given
        // @formatter:off
        String t = tokenBuilder.of("가맹점")
                .mapToUser()
                .withType(MapToUserClaim.MappingType.ENCRYPTED_PERSONAL_INFO)
                .withValue(new MapToUserClaim.Personal("사용자", "123456", "휴대폰번호"), "가맹점 ID", "가맹점에게 전달한 비밀")
                .and()
                .generateTokenBy("가맹점에게 전달한 비밀키");
        // @formatter:on
        System.out.printf(t);
        // When
        Token token = TokenBuilder.verify(t, "가맹점에게 전달한 비밀키");

        // Then
        assertThat(token, is(notNullValue()));
        assertThat(token.getClaim(Token.Claim.TO_LOGIN), is(notNullValue()));
        assertThat(token.getClaim(Token.Claim.TO_MAP_USER), is(notNullValue()));
        assertThat(token.getUserInfoMapper().getPersonalIfNotExistThenNullWith("가맹점에게 전달한 비밀"), is(notNullValue()));
    }

    @Test
    public void 토큰_GETTER_테스트() throws Exception {
        // Given
        // @formatter:off
        String t = tokenBuilder.of("가맹점")
                .login()
                .withMerchantUserId("가맹점의 회원 ID 또는 식별자")
                .withExtraMerchantUserId("핸드폰과 같이 회원 별 추가 ID 체계가 존재할 경우 입력")
                .withSsoCredential("SSO 를 발급 받았을 경우 입력")
                .and()
                .generateTokenBy("가맹점에게 전달한 비밀키");
        // @formatter:on
        System.out.printf(t);
        // When
        Token token = TokenBuilder.verify(t, "가맹점에게 전달한 비밀키");

        // Then
        assertThat(token, is(notNullValue()));
        assertThat(token.getClaim(Token.Claim.TO_LOGIN), is(notNullValue()));
        assertThat(token.getClaim(Token.Claim.TO_PAY), is(nullValue()));
    }

    @Test
    public void OCB_토큰_일반결제_1_3_8_백워드_테스트() throws IOException, InvalidTokenException {
        String t = TokenHistories.OCB_BACKWARE_VERSION_1_3_8.token;


        TokenBuilder.uncheckHeaderOfToken();

        // When
        Token token = TokenBuilder.verify(t, TokenHistories.OCB_BACKWARE_VERSION_1_3_8.key);

        // Then
        assertThat(token, is(notNullValue()));
        assertThat(token.getClaim(Token.Claim.TO_PAY), is(notNullValue()));
        assertThat(token.getClaim(Token.Claim.TO_LOGIN), is(notNullValue()));
    }

    @Test(expected = AssertionError.class)
    public void 사용자_정보_Assert_테스트_SET_NULL_TO_사용자명() {
        MapToUserClaim.Personal personal = new MapToUserClaim.Personal();
        personal.setUsername(null);
    }

    @Test(expected = AssertionError.class)
    public void 사용자_정보_Assert_테스트_SET_BLANK_TO_사용자명() {
        MapToUserClaim.Personal personal = new MapToUserClaim.Personal();
        personal.setUsername("");
    }

    @Test
    public void 사용자_정보_Assert_테스트_SET_HASH_TO_사용자명() {
        MapToUserClaim.Personal personal = new MapToUserClaim.Personal();
        personal.setUsername("홍길동");
    }

    @Test
    public void 시럽페이_사용자_정보에_SSO정책_추가입력_v1_3_11() throws Exception {
        // Give
        // @formatter:off
        tokenBuilder.of("가맹점")
                .login()
                    .withMerchantUserId("가맹점의 회원 ID 또는 식별자")
                    .withExtraMerchantUserId("핸드폰과 같이 회원 별 추가 ID 체계가 존재할 경우 입력")
                    .withSsoCredential("SSO 를 발급 받았을 경우 입력")
                    .isNotApplicableSso()
        ;

        // @formatter:on
        // When
        String t = tokenBuilder.generateTokenBy("가맹점에게 전달한 비밀키");
        System.out.println(t);
        // Then
        assertThat(t, is(notNullValue()));
        assertThat(t.length(), is(not(0)));

        assertThat(TokenBuilder.verify(t, "가맹점에게 전달한 비밀키").getLoginInfo().getSsoPolicy(), is(notNullValue()));
    }

    @Test
    public void 자동결제토큰_Serialization_동작여부() throws Exception {
        // Give
        // @formatter:off
        tokenBuilder.of("가맹점")
                .subscription()
                .withRestrictionOf(PayClaim.MatchedUser.CI_MATCHED_ONLY)
                .and()
                .login()
                    .withMerchantUserId("가맹점의 회원 ID 또는 식별자")
                    .withExtraMerchantUserId("핸드폰과 같이 회원 별 추가 ID 체계가 존재할 경우 입력")
                    .withSsoCredential("SSO 를 발급 받았을 경우 입력")
                    .isNotApplicableSso()
        .and()
        .mapToUser()
        .withType(MapToUserClaim.MappingType.CI_MAPPED_KEY)
        .withValue("21o39812093")
        ;

        // @formatter:on
        // When
        String t = tokenBuilder.generateTokenBy("가맹점에게 전달한 비밀키");
        System.out.println(t);

        Token token = TokenBuilder.verify(t, "가맹점에게 전달한 비밀키");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(bos);
        out.writeObject(token);
        final byte[] bytes = bos.toByteArray();

        // Then
        assertThat(bytes, is(notNullValue()));

        assertThat(bytes.length, greaterThan(0));
    }

    @Test
    public void 결제수단인증_일반결제_테스트() throws Exception {
        // Given
        // @formatter:off
        String t = tokenBuilder.of("가맹점")
                .login()
                    .withMerchantUserId("가맹점의 회원 ID 또는 식별자")
                .and()
                .pay()
                    .forAuthenticating(PayClaim.AuthenticatableMeans.CREDIT_CARD)
                    .withOrderIdOfMerchant("가맹점에서 관리하는 주문 ID")
                    .withProductTitle("쇼핑몰")
                    .withAmount(50000)
                    .withInstallmentPerCardInformation(new PayClaim.CardInstallmentInformation("07", "NN1"))
                .and()
                .generateTokenBy("가맹점에게 전달한 비밀키");
        // @formatter:on
        System.out.printf(t);
        // When
        Token token = TokenBuilder.verify(t, "가맹점에게 전달한 비밀키");

        // Then
        assertThat(token, is(notNullValue()));
        assertThat(token.getClaim(Token.Claim.TO_LOGIN), is(notNullValue()));
        assertThat(token.getClaim(Token.Claim.TO_PAY), is(notNullValue()));
        assertThat(token.getTransactionInfo().getPaymentType(), is(PayClaim.PaymentType.AUTH_MEANS));
        assertThat(token.getTransactionInfo().getAuthenticatableMeans(), is(PayClaim.AuthenticatableMeans.CREDIT_CARD));
    }
}