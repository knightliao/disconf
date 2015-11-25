package com.baidu.disconf.core.test.utils;

import org.junit.Test;

import com.baidu.disconf.core.common.utils.MyStringUtils;

/**
 * @author knightliao
 */
public class MyStringUtilsTestCase {

    @Test
    public void getRandomName() {

        System.out.println(MyStringUtils.getRandomName("abc.properties"));
    }
}
