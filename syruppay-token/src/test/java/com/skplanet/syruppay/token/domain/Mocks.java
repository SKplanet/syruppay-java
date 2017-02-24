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

package com.skplanet.syruppay.token.domain;

import com.skplanet.syruppay.token.claims.OrderClaim;
import com.skplanet.syruppay.token.claims.PayClaim;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 임형태
 * @since 2015.10.20
 */
public class Mocks {
    public static List<OrderClaim.Offer> offerList = new ArrayList<OrderClaim.Offer>() {{
        add(new OrderClaim.Offer().setId("Offer-01").setName("기본할인").setAmountOff(1000).setUserSelectable(false).setOrderApplied(1));
        add(new OrderClaim.Offer().setId("Offer-02").setName("복수구매할인").setAmountOff(500).setUserSelectable(false).setOrderApplied(2));
        add(new OrderClaim.Offer().setId("Offer-03").setName("추가할인").setAmountOff(300).setUserSelectable(false).setOrderApplied(3));
        add(new OrderClaim.Offer().setId("Offer-04").setName("보너스할인").setAmountOff(700).setUserSelectable(false).setOrderApplied(4));
        add(new OrderClaim.Offer().setId("Offer-05").setName("임직원할인").setAmountOff(100).setUserSelectable(false).setOrderApplied(5));
        add(new OrderClaim.Offer().setId("Offer-06").setName("카드사할인").setAmountOff(1000).setUserSelectable(true).setOrderApplied(6));
        add(new OrderClaim.Offer().setId("Offer-07").setName("플러스쿠폰").setAmountOff(500).setUserSelectable(true).setOrderApplied(7));
        add(new OrderClaim.Offer().setId("Offer-08").setType(OrderClaim.OfferType.DELIVERY_COUPON).setName("배송비쿠폰").setAmountOff(2500).setUserSelectable(true).setOrderApplied(8));
    }};
    public static List<OrderClaim.Loyalty> loyalList = new ArrayList<OrderClaim.Loyalty>() {{
        add(new OrderClaim.Loyalty().setIdBy(OrderClaim.LoyaltyId.MILEAGE_OF_11ST).setName("마일리지").setSubscriberId("Loyalty-Sub-Id-02").setBalance(10000).setMaxApplicableAmt(3000).setInitialAppliedAmt(500).setOrderApplied(1));
        add(new OrderClaim.Loyalty().setIdBy(OrderClaim.LoyaltyId.T_MEMBERSHIP).setName("T멤버쉽").setSubscriberId("Loyalty-Sub-Id-03").setBalance(10000).setMaxApplicableAmt(3000).setInitialAppliedAmt(500).setOrderApplied(2));
        add(new OrderClaim.Loyalty().setIdBy(OrderClaim.LoyaltyId.POINT_OF_11ST).setName("포인트").setSubscriberId("Loyalty-Sub-Id-04").setBalance(10000).setMaxApplicableAmt(3000).setInitialAppliedAmt(1000).setOrderApplied(3));
        add(new OrderClaim.Loyalty().setIdBy(OrderClaim.LoyaltyId.OK_CASHBAG).setName("OK캐쉬백").setSubscriberId("Loyalty-Sub-Id-05").setBalance(10000).setMaxApplicableAmt(3000).setInitialAppliedAmt(10).setOrderApplied(4));
        OrderClaim.Loyalty errorLoyalty = new OrderClaim.Loyalty().setIdBy(OrderClaim.LoyaltyId.T_MEMBERSHIP).setName("T멤버쉽-에러상황").setSubscriberId("Loyalty-Sub-Id-06").setBalance(10000).setMaxApplicableAmt(3000).setInitialAppliedAmt(1000).setOrderApplied(5);
        errorLoyalty.setError(new OrderClaim.Error().setType(OrderClaim.ErrorType.MAINTENACE).setDescription("T멤버쉽이 정기점검중이므로 일시적으로 서비스를 이용할 수 없습니다. 잠시 후에 다시 이용해 주세요."));
        add(errorLoyalty);

        OrderClaim.Loyalty addDiscLoyalty = new OrderClaim.Loyalty().setIdBy(OrderClaim.LoyaltyId.OK_CASHBAG).setName("OK캐쉬백-추가할인").setSubscriberId("Loyalty-Sub-Id-01").setBalance(10000).setMaxApplicableAmt(3000).setInitialAppliedAmt(10).setOrderApplied(6);
        addDiscLoyalty.setAdditionalDiscount(new OrderClaim.AdditionalDiscount().setPercentOff(10).setMaxApplicableAmt(500));
        add(addDiscLoyalty);
    }};

    public static List<PayClaim.ShippingAddress> shippingAddressList = new ArrayList<PayClaim.ShippingAddress>() {{
        add(new PayClaim.ShippingAddress().setId("Shipping-Address-01").setName("회사").setCountryCode("KR").setZipCode("12345").setMainAddress("경기도 성남시 분당구 판교로264").setDetailAddress("더플래닛").setCity("성남시").setState("경기도").setRecipientName("USER").setRecipientPhoneNumber("01012341234").setDeliveryRestriction(PayClaim.DeliveryRestriction.NOT_FAR_AWAY).setDefaultDeliveryCost(2500).setAdditionalDeliveryCost(0).setOrderApplied(1));
        add(new PayClaim.ShippingAddress().setId("Shipping-Address-02").setName("집").setCountryCode("KR").setZipCode("12345").setMainAddress("경기도 성남시 분당구 판교로123").setDetailAddress("SK플래닛 2사옥").setCity("성남시").setState("경기도").setRecipientName("USER").setRecipientPhoneNumber("01012341234").setDeliveryRestriction(PayClaim.DeliveryRestriction.NOT_FAR_AWAY).setDefaultDeliveryCost(2500).setAdditionalDeliveryCost(0).setOrderApplied(2));
        add(new PayClaim.ShippingAddress().setId("Shipping-Address-03").setName("시골").setCountryCode("KR").setZipCode("56789").setMainAddress("강원도 삼척시 산골면 시골읍").setDetailAddress("판자집").setCity("삼척").setState("강원도").setRecipientName("USER").setRecipientPhoneNumber("01012341234").setDeliveryRestriction(PayClaim.DeliveryRestriction.FAR_AWAY).setDefaultDeliveryCost(2500).setAdditionalDeliveryCost(2500).setOrderApplied(3));
        add(new PayClaim.ShippingAddress().setId("Shipping-Address-04").setName("섬나라").setCountryCode("KR").setZipCode("98765").setMainAddress("제주도 서귀포시 제주면 제주읍").setDetailAddress("돌담집").setCity("서귀포").setState("제주도").setRecipientName("USER").setRecipientPhoneNumber("01012341234").setDeliveryRestriction(PayClaim.DeliveryRestriction.FAR_FAR_AWAY).setDefaultDeliveryCost(2500).setAdditionalDeliveryCost(5000).setOrderApplied(4));
    }};

    public static List<OrderClaim.ProductDeliveryInfo> productDeliveryInfoList = new ArrayList<OrderClaim.ProductDeliveryInfo>() {{
        add(new OrderClaim.ProductDeliveryInfo().setDeliveryType(PayClaim.DeliveryType.PREPAID).setDeliveryName("선결제").setDefaultDeliveryCostApplied(true).setAdditionalDeliveryCostApplied(true));
        add(new OrderClaim.ProductDeliveryInfo().setDeliveryType(PayClaim.DeliveryType.FREE).setDeliveryName("무료배송").setDefaultDeliveryCostApplied(false).setAdditionalDeliveryCostApplied(true));
        add(new OrderClaim.ProductDeliveryInfo().setDeliveryType(PayClaim.DeliveryType.DIY).setDeliveryName("방문수령").setDefaultDeliveryCostApplied(false).setAdditionalDeliveryCostApplied(false));
        add(new OrderClaim.ProductDeliveryInfo().setDeliveryType(PayClaim.DeliveryType.QUICK).setDeliveryName("퀵서비스").setDefaultDeliveryCostApplied(false).setAdditionalDeliveryCostApplied(false));
        add(new OrderClaim.ProductDeliveryInfo().setDeliveryType(PayClaim.DeliveryType.PAYMENT_ON_DELIVERY).setDeliveryName("착불").setDefaultDeliveryCostApplied(false).setAdditionalDeliveryCostApplied(true));
    }};
}
