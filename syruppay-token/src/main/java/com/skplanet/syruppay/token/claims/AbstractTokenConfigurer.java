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

package com.skplanet.syruppay.token.claims;

import com.skplanet.syruppay.token.ClaimConfigurerAdapter;
import com.skplanet.syruppay.token.Jwt;
import com.skplanet.syruppay.token.TokenBuilder;

/**
 * Syrup Pay Token 생성 시 필요한 {@link com.skplanet.syruppay.token.ClaimConfigurer} 기반의 인스턴스들에게 필요한 편리 기능을 추가한다.
 *
 * @param <T>
 *         {@link com.skplanet.syruppay.token.ClaimBuilder} B 에 의해서 빌드된 토큰 객체.
 * @param <B>
 *         {@link com.skplanet.syruppay.token.Jwt} 타입의 객체를 빌드 하고 {@link com.skplanet.syruppay.token.ClaimBuilder}에 의해 설정 되어진 타입
 * @author 임형태
 * @since 1.0
 */
abstract class AbstractTokenConfigurer<T extends AbstractTokenConfigurer<T, B>, B extends TokenBuilder<B>> extends ClaimConfigurerAdapter<Jwt, B> {

    /**
     * {@link com.skplanet.syruppay.token.claims.AbstractTokenConfigurer} 을 토큰 Claim 빌드 목록에서 제거한다.
     *
     * @return 수정된 {@link com.skplanet.syruppay.token.ClaimBuilder}를 반환한다.
     */
    @SuppressWarnings("unchecked")
    public B disable() {
        getBuilder().removeConfigurer(getClass());
        return getBuilder();
    }
}
