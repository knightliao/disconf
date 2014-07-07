package com.baidu.disconf.core.test.zookeeper;

import java.util.List;
import java.util.Random;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.baidu.disconf.core.common.zookeeper.ZookeeperMgr;

/**
 * 
 * @author liaoqiqi
 * @version 2014-6-16
 */
public class ZookeeperMgrTest {

    public static String hosts = "10.48.57.42:8581,10.48.57.42:8582,10.48.57.42:8583";

    @BeforeClass
    public static void beforeAllClass() throws Exception {

        ZookeeperMgr.getInstance().init(hosts, "disconf");
    }

    @AfterClass
    public static void destory() throws Exception {

        ZookeeperMgr.getInstance().release();
    }

    @Test
    public final void testGetRootChildren() {

        List<String> list = ZookeeperMgr.getInstance().getRootChildren();

        for (String item : list) {

            System.out.println(item);
        }

        Assert.assertTrue(list.size() > 0);
    }

    @Test
    public final void testWritePersistentUrl() {

        try {

            Random random = new Random();
            int randomInt = random.nextInt();

            // 写
            String url = "/disconfserver/dan_dnwebbilling_1_0_online";
            ZookeeperMgr.getInstance().writePersistentUrl(url,
                    String.valueOf(randomInt));

            // 读
            String readData = ZookeeperMgr.getInstance().readUrl(url, null);
            Assert.assertEquals(String.valueOf(randomInt), readData);

        } catch (Exception e) {
            Assert.assertTrue(false);
        }
    }
}
