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

/**
 * {@link com.skplanet.syruppay.token.Jwt}를 빌드할 수 있는 토큰 빌더의 인터페이스를 정의한다.
 *
 * @param <H>
 *         {@link Builder}를 통해 구현하고자는 클래스
 * @author 임형태
 * @since 1.0
 */
public interface Builder<H extends Builder<H>> extends ClaimBuilder<Jwt> {
    /**
     * 클래스 네임을 기준으로 {@link com.skplanet.syruppay.token.ClaimConfigurer}를 반환하거나 존재하지 않을 경우 <code>null</code> 을 반환할 수 있다.
     * 주의할 점은 객체 간의 상하위 관계(상속, 포함) 관계는 고려되지 않았다.
     *
     * @param clazz
     *         찾기를 시도하려는 {@link com.skplanet.syruppay.token.ClaimConfigurer} 클래스
     * @return 찾은 {@link com.skplanet.syruppay.token.ClaimConfigurer}을 반환하거나 찾지 못했을 경우 null 을 반환한다.
     */
    <C extends ClaimConfigurer<Jwt, H>> C getConfigurer(Class<C> clazz);

    /**
     * 클래스 이름을 기준으로 {@link com.skplanet.syruppay.token.ClaimConfigurer} 를 제거하여 반환하거나 존재하지 않을 경우 <code>null</code>을 반환한다.
     * 주의할 점은 객체 간의 상하위 관계(상속, 포함) 관계는 고려되지 않았다.
     * considered.
     *
     * @param clazz
     *         제거를 시도하려는 {@link com.skplanet.syruppay.token.ClaimConfigurer} 클래스
     * @return 제거된 {@link com.skplanet.syruppay.token.ClaimConfigurer}을 반환하거나 찾지 못했을 경우 null 을 반환한다.
     */
    <C extends ClaimConfigurer<Jwt, H>> C removeConfigurer(Class<C> clazz);
}
