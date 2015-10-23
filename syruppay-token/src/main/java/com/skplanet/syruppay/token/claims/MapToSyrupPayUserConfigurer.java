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

import com.skplanet.syruppay.token.TokenBuilder;

/**
 * 시럽페이 사용자 정보 검색을 위한 Claim 을 정의한다.
 * <p>
 * 기존 시럽페이 토큰의 하위 호화선을 보장하기 위하여 맴버 변수와 메소드 명 간의 차이가 발생할 수 있으니 주의해야 한다.<br>
 * 시럽페이 사용자를 맵핑하는 방식은 {@code MappingType.CI_HASH}와 {@code MappingType.CI_MAPPED_KEY} 를 지원한다.
 * {@code MappingType.CI_HASH} 를 사용하는 경우 사용자 CI에 대한 SHA256 해쉬로 시럽페이 사용자를 검색하여 {@code MappingType.CI_MAPPED_KEY} 를 이용하는 경우 SK Planet 의 CI 인프라 자원을 활용하여 지정된 Key 값을 기준으로 시럽페이 사용자를 맵핑한다.
 *
 * @param <H>
 *         {@link com.skplanet.syruppay.token.TokenBuilder}
 * @author 임형태
 * @see com.skplanet.syruppay.token.claims.MapToSyrupPayUserConfigurer.MappingType
 * @since 1.0
 */
public class MapToSyrupPayUserConfigurer<H extends TokenBuilder<H>> extends AbstractTokenConfigurer<MapToSyrupPayUserConfigurer<H>, H> {
    private MappingType mappingType;
    private String mappingValue;

    /**
     * 시럽페이 사용자 정보를 맵핑하는 방식을 반환한다.
     *
     * @return {@link com.skplanet.syruppay.token.claims.MapToSyrupPayUserConfigurer.MappingType}
     * @see com.skplanet.syruppay.token.claims.MapToSyrupPayUserConfigurer.MappingType
     */
    public MappingType getMappingType() {
        return mappingType;
    }

    /**
     * 시럽페이 사용자 정보를 맵핑하기 위한 값을 반환한다.
     * 해당 값은 {@link #getMappingType()}에 의존적이므로 주의해야 한다.
     *
     * @return the mapping value
     */
    public String getMappingValue() {
        return mappingValue;
    }

    /**
     * 시럽페이 사용자 정보를 맵핑하기 위한 방식을 지정한다.
     *
     * @param type
     *         {@link com.skplanet.syruppay.token.claims.MapToSyrupPayUserConfigurer.MappingType}
     * @return <code>this</code>
     */
    public MapToSyrupPayUserConfigurer<H> withType(MappingType type) {
        this.mappingType = type;
        return this;
    }

    /**
     * 시럽페이 사용자 정보를 매핑하기 위한 값을 지정한다.
     *
     * @param value
     *         {@link com.skplanet.syruppay.token.claims.MapToSyrupPayUserConfigurer.MappingType}의 값
     * @return <code>this</code>
     */
    public MapToSyrupPayUserConfigurer<H> withValue(String value) {
        this.mappingValue = value;
        return this;
    }

    /**
     * JOSE 빌드 시 지정된 Claim 이름
     *
     * @return the string
     */
    public String claimName() {
        return "userInfoMapper";
    }

    public void validRequired() throws Exception {
        if (this.mappingType == null || this.mappingValue == null) {
            throw new IllegalArgumentException("fields to map couldn't be null. type : " + this.mappingType + "value : " + this.mappingValue);
        }
    }

    /**
     * 시럽페이 사용자 정보를 맵핑하기 위한 방식을 정의한다.
     */
    public static enum MappingType {
        CI_HASH, CI_MAPPED_KEY
    }
}
