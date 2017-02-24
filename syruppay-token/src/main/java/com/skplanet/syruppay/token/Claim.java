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


import java.io.Serializable;

/**
 * {@link com.skplanet.syruppay.token.ClaimBuilder}를 설정할 수 있도록 지원합니다.
 * 모든 {@link Claim} 는 가자 우선 {@link #init(com.skplanet.syruppay.token.ClaimBuilder)} 메소드가 호출되어야 한다.
 * 모든 {@link #init(com.skplanet.syruppay.token.ClaimBuilder)}호출 된 후에 각각의 {@link #configure(com.skplanet.syruppay.token.ClaimBuilder)} 메소드가 호출 된다.
 *
 * @param <O>
 *         {@link com.skplanet.syruppay.token.ClaimBuilder} B 에 의해서 빌드된 객체.
 * @param <B>
 *         O 타입의 객체를 빌드 하고 {@link com.skplanet.syruppay.token.ClaimBuilder}에 의해 설정 되어진 타입
 * @author 임형태
 * @since 1.0
 */
public interface Claim<O, B extends ClaimBuilder<O>> extends Serializable {

    String claimName();

    /**
     * {@link com.skplanet.syruppay.token.ClaimBuilder}를 초기화한다.
     * 여기에서는 상태만 특정 상태로 생성하거나 수정해야만 하며 Claim 객채를 빌드하기 위하여 {@link com.skplanet.syruppay.token.ClaimBuilder}에 등록해서는 안된다.
     * 이러한 방식은 {@link #configure(com.skplanet.syruppay.token.ClaimBuilder)} 메소드로 빌드를 수행하는 동안 객체 공유를 정확하게 사용하는 것을 보장하기 위해서이다.
     *
     * @param builder
     *         세팅 하고자 하는 {@link com.skplanet.syruppay.token.ClaimBuilder}
     * @throws Exception
     *         내부 오류 발생 시
     */
    void init(B builder) throws Exception;

    /**
     * {@link com.skplanet.syruppay.token.ClaimBuilder}에 필수 등록 요소를 등록할 때 {@link com.skplanet.syruppay.token.ClaimBuilder} 를 설정한다.
     *
     * @param builder
     *         세팅 하고자 하는 {@link com.skplanet.syruppay.token.ClaimBuilder}
     * @throws Exception
     *         내부 오류 발생 시
     */
    void configure(B builder) throws Exception;

    /**
     * {@link Claim} 에 대한 Required 사항에 대해 준수하였는지 여부를 검증해야 하며
     * 준수하지 않았을 경우 적절한 Exception 을 throw 해야 한다.
     *
     * @throws Exception
     *         Requried 에 대한 예외
     */
    void validRequired() throws Exception;
}
