package com.skplanet.syruppay.token.claims;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by 1002225 on 2016. 8. 12..
 */
public class PlanTest {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test(expected = AssertionError.class)
    public void testContsruct_with_null() {
        new SubscriptionConfigurer.Plan(null, null);

    }

    @Test(expected = AssertionError.class)
    public void testContsruct_with_empty() {
        new SubscriptionConfigurer.Plan(SubscriptionConfigurer.Interval.ONDEMAND, "");
    }

}