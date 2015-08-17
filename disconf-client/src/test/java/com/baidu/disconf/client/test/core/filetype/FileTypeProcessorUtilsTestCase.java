package com.baidu.disconf.client.test.core.filetype;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.baidu.disconf.client.common.constants.SupportFileTypeEnum;
import com.baidu.disconf.client.core.filetype.FileTypeProcessorUtils;

/**
 * @author knightliao
 */
public class FileTypeProcessorUtilsTestCase {

    @Test
    public void getKvMapTest() {

        try {

            Map<String, Object> map =
                FileTypeProcessorUtils.getKvMap(SupportFileTypeEnum.PROPERTIES, "testProperties" + ".properties");

            System.out.println(map.toString());
            Assert.assertEquals(map.get("staticvar2"), "100");
            Assert.assertEquals(map.get("staticvar"), "50");

        } catch (Exception e) {
            Assert.assertTrue(false);
        }

        try {

            Map<String, Object> map = FileTypeProcessorUtils.getKvMap(SupportFileTypeEnum.XML, "testXml.xml");

            System.out.println(map.toString());
            Assert.assertEquals(0, map.keySet().size());

        } catch (Exception e) {
            Assert.assertTrue(false);
        }

        try {

            Map<String, Object> map = FileTypeProcessorUtils.getKvMap(SupportFileTypeEnum.ANY, "testJson.json");

            System.out.println(map.toString());
            Assert.assertEquals(0, map.keySet().size());

        } catch (Exception e) {
            Assert.assertTrue(false);
        }
    }
}
