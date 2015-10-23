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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * 시럽페이 토큰 빌드에 필요한 설정 내역 및 Claim 내역을 관리한다.
 *
 * @author 임형태
 * @since 1.0
 */
public abstract class AbstractConfiguredTokenBuilder<O, B extends ClaimBuilder<O>> extends AbstractClaimBuilder<O> {
    private final LinkedHashMap<Class<? extends ClaimConfigurer<O, B>>, List<ClaimConfigurer<O, B>>> configurers = new LinkedHashMap<Class<? extends ClaimConfigurer<O, B>>, List<ClaimConfigurer<O, B>>>();

    /**
     * 등록되어 있는 {@link com.skplanet.syruppay.token.ClaimConfigurer}를 해당 구현 클래스 이름으로 조회하여 반환한다.
     * 만약 존재하지 않을 경우 <code>null</code> 을 반환할 수 있다.
     * <p>
     * 객체에 대한 포함관계는 고려되지 않았으니 주의하기 바란다.
     *
     * @param clazz
     *         구현 클래스 이름
     * @return {@link com.skplanet.syruppay.token.ClaimConfigurer}
     */
    @SuppressWarnings("unchecked")
    public <C extends ClaimConfigurer<O, B>> C getConfigurer(Class<C> clazz) {
        List<ClaimConfigurer<O, B>> configs = this.configurers.get(clazz);
        if (configs == null) {
            return null;
        }
        if (configs.size() != 1) {
            throw new IllegalStateException("Only one configurer expected for type " + clazz + ", but got " + configs);
        }
        return (C) configs.get(0);
    }

    /**
     * 등록되어 있는 {@link com.skplanet.syruppay.token.ClaimConfigurer}를 해당 구현 클래스 이름으로 제외할 후 반환한다.
     * 만약 존재하지 않을 경우 <code>null</code> 을 반환할 수 있다.
     * <p>
     * 객체에 대한 포함관계는 고려되지 않았으니 주의하기 바란다.
     *
     * @param clazz
     *         구현 클래스 이름
     * @return {@link com.skplanet.syruppay.token.ClaimConfigurer}
     */
    @SuppressWarnings("unchecked")
    public <C extends ClaimConfigurer<O, B>> C removeConfigurer(Class<C> clazz) {
        List<ClaimConfigurer<O, B>> configs = this.configurers.remove(clazz);
        if (configs == null) {
            return null;
        }
        if (configs.size() != 1) {
            throw new IllegalStateException("Only one configurer expected for type " + clazz + ", but got " + configs);
        }
        return (C) configs.get(0);
    }

    /**
     * {@link com.skplanet.syruppay.token.ClaimConfigurerAdapter}에 {@link com.skplanet.syruppay.token.ClaimBuilder}를 적용하고
     * 어떠한 {@link com.skplanet.syruppay.token.ClaimConfigurerAdapter#setBuilder(com.skplanet.syruppay.token.ClaimBuilder)}를 호출한다.
     * <p>
     * 객체에 대한 포함관계는 고려되지 않았으니 주의하기 바란다.
     *
     * @param configurer
     *         {@link com.skplanet.syruppay.token.ClaimConfigurer}
     * @return {@link com.skplanet.syruppay.token.ClaimConfigurer}
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public <C extends ClaimConfigurerAdapter<O, B>> C apply(C configurer)
            throws Exception {
        add(configurer);
        configurer.setBuilder((B) this);
        return configurer;
    }

    /**
     * 필요하다면 바로 {@link com.skplanet.syruppay.token.ClaimConfigurer#init(com.skplanet.syruppay.token.ClaimBuilder)} 를 호출하고 이러한 것이 허용된
     * {@link com.skplanet.syruppay.token.ClaimConfigurer}를 추가한다.
     *
     * @param configurer
     *         추가 하기 위한 {@link com.skplanet.syruppay.token.ClaimConfigurer}
     * @throws Exception
     *         내부 오류 발생 시
     */
    @SuppressWarnings("unchecked")
    private <C extends ClaimConfigurer<O, B>> void add(C configurer) throws Exception {
        Class<? extends ClaimConfigurer<O, B>> clazz = (Class<? extends ClaimConfigurer<O, B>>) configurer.getClass();
        synchronized (configurers) {
            List<ClaimConfigurer<O, B>> configs = this.configurers.get(clazz);
            if (configs == null) {
                configs = new ArrayList<ClaimConfigurer<O, B>>(1);
            }
            configs.add(configurer);
            this.configurers.put(clazz, configs);
        }
    }

    public Set<Class<? extends ClaimConfigurer<O, B>>> getClasses() {
        return this.configurers.keySet();
    }
}
