package com.baidu.disconf.web.test.utils;

import org.junit.Assert;
import org.junit.Test;

import com.baidu.disconf.web.utils.CodeUtils;

/**
 * Created by knightliao on 15/1/7.
 */
public class CodeUtilsTestCase {

    @Test
    public void unicodeToUtf8Test() {

        String code =
                CodeUtils.unicodeToUtf8(
                        "syserror.paramtype=\\u8bf7\\u6c42\\u53c2\\u6570\\u89e3\\u6790\\u9519" + "\\u8bef");

        System.out.println(code);

        Assert.assertEquals("syserror.paramtype=请求参数解析错误", code);
    }
}
