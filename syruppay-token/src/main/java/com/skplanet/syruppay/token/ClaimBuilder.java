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
 * 토큰의 개별 Claim 을 빌드 할 수 있는 기능을 정의한다.
 *
 * @param <O>
 *         빌드 된(또는 하고자 하는) 클래스 타입
 * @author 임형태
 * @since 1.0
 */
public interface ClaimBuilder<O> {

    /**
     * Claim 객채를 빌드하고 반환한다. 만약 객체가 존재하지 않을 경우 null 을 반환할 수도 있다.
     *
     * @return 빌드 된 Claim 객체
     * @throws Exception
     *         Claim 빌드 시 내부에서 발생하는 오류
     */
    O build() throws Exception;
}
