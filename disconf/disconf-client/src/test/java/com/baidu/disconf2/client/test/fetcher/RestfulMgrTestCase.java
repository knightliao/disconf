package com.baidu.disconf2.client.test.fetcher;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.baidu.disconf2.client.DisconfMgr;
import com.baidu.disconf2.client.fetcher.inner.restful.RestfulMgr;
import com.baidu.disconf2.client.fetcher.inner.restful.core.RemoteUrl;
import com.baidu.disconf2.client.test.common.BaseTestCase;
import com.baidu.disconf2.core.common.constants.DisConfigTypeEnum;
import com.baidu.disconf2.core.common.path.DisconfWebPathMgr;

public class RestfulMgrTestCase extends BaseTestCase {

    @BeforeClass
    public static void beforeAllClass() throws Exception {

        DisconfMgr.start("");
    }

    @AfterClass
    public static void destory() throws Exception {

        RestfulMgr.getInstance().close();
    }

    @Test
    public final void testInit() {

    }

    public void testDownloadFromServer() {

        List<String> hostList = new ArrayList<String>();
        hostList.add("http://127.0.0.1:8089");
        hostList.add("http://127.0.0.1:8089");

        try {

            String curUrl = DisconfWebPathMgr.getRemoteUrlParameter(
                    "/api/store", "app", "version", "env", "a.conf",
                    DisConfigTypeEnum.FILE);

            RemoteUrl remoteUrl = new RemoteUrl(curUrl, hostList);

            RestfulMgr.getInstance().downloadFromServer(remoteUrl, "a.conf",
                    "D:\\test\\tmp", "D:\\test", true);

        } catch (Exception e) {

            e.printStackTrace();

            Assert.assertFalse(true);
        }
    }
}
