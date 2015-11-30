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

package com.skplanet.syruppay.client;


import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

/**
 * 시럽페이 서버로의 연결을 주관한다.
 *
 * @author 임형태
 * @since 0.1
 */
public class SyrupPayConnector {
    private final Client client;
    private final SyrupPayEnvironment syrupPayEnvironment;

    public SyrupPayConnector(SyrupPayEnvironment syrupPayEnvironment) {
        this.syrupPayEnvironment = syrupPayEnvironment;
        this.client = ClientBuilder.newClient();
    }
}
