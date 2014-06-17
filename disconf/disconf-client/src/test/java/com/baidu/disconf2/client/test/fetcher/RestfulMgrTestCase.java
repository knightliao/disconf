package com.baidu.disconf2.client.test.fetcher;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.baidu.disconf2.client.fetcher.FetcherMgr;
import com.baidu.disconf2.client.fetcher.inner.restful.RestfulMgr;
import com.baidu.disconf2.client.fetcher.inner.restful.core.RemoteUrl;
import com.baidu.disconf2.client.test.common.BaseTestCase;
import com.baidu.disconf2.client.test.utils.DirUtils;
import com.baidu.disconf2.core.common.constants.DisConfigTypeEnum;
import com.baidu.disconf2.core.common.path.DisconfWebPathMgr;

/**
 * 
 * @author liaoqiqi
 * @version 2014-6-17
 */
public class RestfulMgrTestCase extends BaseTestCase {

    /**
     * 
     */
    @Test
    public void testDownloadFromServer() {

        //
        if (!checkNetWork()) {
            return;
        }

        try {

            // 初始化下载器
            FetcherMgr.init();

        } catch (Exception e1) {
            e1.printStackTrace();
            Assert.assertTrue(false);
        }

        List<String> hostList = new ArrayList<String>();
        hostList.add("http://127.0.0.1:8089");
        hostList.add("http://127.0.0.1:8089");

        try {

            String curUrl = DisconfWebPathMgr.getRemoteUrlParameter(
                    "/api/config", "disconf_testcase", "1_0_0_0", "rd",
                    "confA.properties", DisConfigTypeEnum.FILE);

            RemoteUrl remoteUrl = new RemoteUrl(curUrl, hostList);

            File tempFile = DirUtils.createTempDirectory();
            File tempFile2 = DirUtils.createTempDirectory();

            RestfulMgr.getInstance().downloadFromServer(remoteUrl,
                    "confA.properties", tempFile.getAbsolutePath(),
                    tempFile2.getAbsolutePath(), true);

        } catch (Exception e) {

            e.printStackTrace();

            Assert.assertFalse(true);
        }
    }
}
