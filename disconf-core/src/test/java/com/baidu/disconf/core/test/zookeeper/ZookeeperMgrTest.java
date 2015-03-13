package com.baidu.disconf.core.test.zookeeper;

import java.util.List;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import com.baidu.disconf.core.common.zookeeper.ZookeeperMgr;
import com.baidu.disconf.core.common.zookeeper.inner.ResilientActiveKeyValueStore;
import com.baidu.disconf.core.test.zookeeper.mock.ResilientActiveKeyValueStoreMock;

import mockit.NonStrictExpectations;

/**
 * 使用Jmockit进行测试
 *
 * @author liaoqiqi
 * @version 2014-6-16
 */
public class ZookeeperMgrTest {

    /**
     * 测试获取Root子节点
     */
    @Test
    public final void testGetRootChildren() {

        final ZookeeperMgr obj = ZookeeperMgr.getInstance();

        //
        // 注入
        //
        new NonStrictExpectations(obj) {
            {
                ResilientActiveKeyValueStore store = new ResilientActiveKeyValueStoreMock();
                this.setField(obj, "store", store);
            }
        };

        List<String> list = ZookeeperMgr.getInstance().getRootChildren();

        for (String item : list) {

            System.out.println(item);
        }

        Assert.assertTrue(list.size() > 0);
    }

    /**
     * 写结点
     */
    @Test
    public final void testWritePersistentUrl() {

        try {

            Random random = new Random();
            int randomInt = random.nextInt();

            // 写
            String url = "/disconfserver/dan_dnwebbilling_1_0_online";
            ZookeeperMgr.getInstance().writePersistentUrl(url, String.valueOf(randomInt));

            // 读
            String readData = ZookeeperMgr.getInstance().readUrl(url, null);
            Assert.assertEquals(String.valueOf(randomInt), readData);

        } catch (Exception e) {
            Assert.assertTrue(false);
        }
    }
}
