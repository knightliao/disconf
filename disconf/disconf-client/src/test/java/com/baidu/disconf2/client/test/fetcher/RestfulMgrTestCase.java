package com.baidu.disconf2.client.test.fetcher;

import java.net.URL;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.baidu.disconf2.client.DisconfMgr;
import com.baidu.disconf2.client.fetcher.inner.restful.RestfulMgr;
import com.baidu.disconf2.core.common.constants.DisConfigTypeEnum;
import com.baidu.disconf2.core.common.path.PathMgr;

public class RestfulMgrTestCase {

    @BeforeClass
    public static void beforeAllClass() throws Exception {

        DisconfMgr.run("");
    }

    @AfterClass
    public static void destory() throws Exception {

        RestfulMgr.getInstance().close();
    }

    @Test
    public final void testInit() {

    }

    @Test
    public void testDownloadFromServer() {

        String url = "http://127.0.0.1:8089/api/store";

        try {

            String curUrl = PathMgr.getRemoteUrlParameter("app", "version",
                    "env", "a.conf", DisConfigTypeEnum.FILE);

            url = url + curUrl;

            RestfulMgr.getInstance().downloadFromServer(new URL(url), "a.conf",
                    "D:\\test\\tmp", "D:\\test", true);

        } catch (Exception e) {

            Assert.assertFalse(true);
        }
    }
}
