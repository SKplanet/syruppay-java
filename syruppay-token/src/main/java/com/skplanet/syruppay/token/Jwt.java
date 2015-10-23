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
import java.util.UUID;

/**
 * JWT 에 기본적으로 입력되어야 하는 Claim 을 정의한다.
 *
 * @author 임형태
 * @since 1.0
 */
public class Jwt implements Serializable {
    private static final long serialVersionUID = -3656001354175582948L;
    private final String aud = "https://pay.syrup.co.kr";
    private final String typ = "jose";
    private String iss;
    private long exp;
    private long iat;
    private String jti = UUID.randomUUID().toString();
    private Long nbf;
    private String sub;

    public void setSub(String sub) {
        this.sub = sub;
    }

    void setIss(String iss) {
        this.iss = iss;
    }

    long getIat() {
        return iat;
    }

    void setIat(long iat) {
        this.iat = iat;
    }

    public long getExp() {
        return exp;
    }

    public void setExp(long exp) {
        this.exp = exp;
    }

    void setNbf(long nbf) {
        this.nbf = nbf;
    }
}
