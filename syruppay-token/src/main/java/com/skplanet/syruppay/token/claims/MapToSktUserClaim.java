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

import com.skplanet.syruppay.token.Builder;

/**
 * 가맹점 사용자의 SKT 가입 정보와 관련된 Claim 을 설정한다.
 *
 * @author 임형태
 * @since 1.0
 */
@Deprecated
public class MapToSktUserClaim<H extends Builder<H>> extends AbstractTokenClaim<MapToSktUserClaim<H>, H> {
    private String lineNumber;
    private String svcMgmtNumber;

    public String claimName() {
        return "lineInfo";
    }

    public void validRequired() throws Exception {
        if(lineNumber.contains("-")) {
            throw new IllegalArgumentException("line number should be without '-' mark. ex) 01011112222");
        }
    }

    public String getLineNumber() {
        return lineNumber;
    }

    public String getSvcMgmtNumber() {
        return svcMgmtNumber;
    }

    public MapToSktUserClaim<H> withLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
        return this;
    }

    public MapToSktUserClaim<H> withServiceManagementNumber(String serviceManagementNumber) {
        this.svcMgmtNumber = serviceManagementNumber;
        return this;
    }
}
