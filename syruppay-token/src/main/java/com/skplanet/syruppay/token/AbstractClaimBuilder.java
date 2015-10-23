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

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * {@link com.skplanet.syruppay.token.ClaimBuilder}를 기반으로 객체 빌드를 단 한번만 수행할 수 있도록 보장한다.
 *
 * @param <O>
 *         빌드 된(또는 하고자 하는) 클래스 타입
 * @author 임형태
 * @since 1.0
 */
public abstract class AbstractClaimBuilder<O> implements ClaimBuilder<O> {
    private AtomicBoolean building = new AtomicBoolean();

    private O object;

    /**
     * {@inheritDoc}
     */
    public final O build() throws Exception {
        if (building.compareAndSet(false, true)) {
            object = doBuild();
            return object;
        }
        throw new AlreadyBuiltException("This object has already been built");
    }

    /**
     * 빌드된 객체 정보를 반환한다.
     * 만약 빌드 되지 않았을 경우 Exception 을 던진다.
     *
     * @return 빌드된 객체
     */
    public final O getObject() {
        if (!building.get()) {
            throw new IllegalStateException("This object has not been built");
        }
        return object;
    }

    /**
     * 하위 클래스에서 원하는 빌드 절차는 이 메소드를 통해 구현해야만(Should) 한다.
     *
     * @return {@link #build()} 메소드를 통해 반환된 객체를 반환한다.
     * @throws Exception
     *         내부에서 오류 발생 시 Exception
     */
    protected abstract O doBuild() throws Exception;
}
