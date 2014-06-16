package com.baidu.disconf2.client.test.common;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 
 * @author liaoqiqi
 * @version 2014-6-11
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class BaseTestCase {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(BaseTestCase.class);

    @Before
    public void init() {

    }

    @Test
    public void pass() {

    }
}
