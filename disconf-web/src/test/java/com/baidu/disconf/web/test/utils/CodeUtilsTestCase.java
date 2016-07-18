package com.baidu.disconf.web.test.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
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

    @Test
    public void unicodeToUtf8Test2() {

        String src = "中中，，在经";
        testTransfer(src);

        //
        try {
            src = FileUtils.readFileToString(new File("src/test/resources/file2/utf-file.properties"), "utf8");
        } catch (IOException e) {
            Assert.assertTrue(false);
        }
        String dest = testTransfer(src);
        Assert.assertTrue(!src.equals(dest));

        //
        try {
            src = FileUtils.readFileToString(new File("src/test/resources/file2/utf-file.pml"), "utf8");
        } catch (IOException e) {
            Assert.assertTrue(false);
        }
        dest = testTransfer(src);
        Assert.assertEquals(src, dest);
    }

    private String testTransfer(String src) {

        String code =
                CodeUtils.utf8ToUnicode(src);
        System.out.println(code);

        code = CodeUtils.unicodeToUtf8(code);
        System.out.println(code);

        return code;
    }
}
