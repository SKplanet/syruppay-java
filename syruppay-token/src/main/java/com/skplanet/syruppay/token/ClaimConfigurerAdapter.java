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

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * 하위 클래스가 오직 자신의 관심사만 구현할 수 있도록 {@link com.skplanet.syruppay.token.ClaimConfigurer}의 베이스를 제공한다.
 * 또한 {@link com.skplanet.syruppay.token.ClaimConfigurer}을 사용하고 이미 설정된 {@link com.skplanet.syruppay.token.ClaimBuilder} 로의 접근을 확보하기 위한 매커니즘을 제공한다.
 *
 * @param <O>
 *         {@link com.skplanet.syruppay.token.ClaimBuilder} B 에 의해서 빌드된 객체.
 * @param <B>
 *         O 타입을 빌드 하는 {@link com.skplanet.syruppay.token.ClaimBuilder}의 타입이며 {@link com.skplanet.syruppay.token.ClaimBuilder}의해 설정되어지고 있는 타입
 * @author 임형태
 * @since 1.0
 */
public abstract class ClaimConfigurerAdapter<O, B extends ClaimBuilder<O>> implements ClaimConfigurer<O, B> {
    @JsonIgnore
    private B builder;

    public void init(B builder) throws Exception {
    }

    public void configure(B builder) throws Exception {
    }

    /**
     * {@link com.skplanet.syruppay.token.ClaimConfigurer}를 사용하고자 할 때 {@link com.skplanet.syruppay.token.ClaimBuilder}를 반환한다.
     * 이러한 방식은 메소드를 연결하여 사용할 때 매우 유용하다.
     *
     * @return B
     */
    public B and() {
        return getBuilder();
    }

    /**
     * {@link com.skplanet.syruppay.token.ClaimBuilder}를 반환하며 null 일 수 없다.
     *
     * @return the {@link com.skplanet.syruppay.token.ClaimBuilder}
     * @throws IllegalStateException
     *         {@link com.skplanet.syruppay.token.ClaimBuilder} is null
     */
    protected final B getBuilder() {
        if (builder == null) {
            throw new IllegalStateException("builder cannot be null");
        }
        return builder;
    }

    /**
     * {@link com.skplanet.syruppay.token.ClaimBuilder}를 세팅한다.
     *
     * @param builder
     *         세팅 하고자 하는 {@link com.skplanet.syruppay.token.ClaimBuilder}
     */
    public void setBuilder(B builder) {
        this.builder = builder;
    }
}
