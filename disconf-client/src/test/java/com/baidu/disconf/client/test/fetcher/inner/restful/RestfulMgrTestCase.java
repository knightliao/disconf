package com.baidu.disconf.client.test.fetcher.inner.restful;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.baidu.disconf.client.config.DisClientConfig;
import com.baidu.disconf.client.config.DisClientSysConfig;
import com.baidu.disconf.client.fetcher.inner.restful.RestfulMgr;
import com.baidu.disconf.client.fetcher.inner.restful.core.RemoteUrl;
import com.baidu.disconf.client.fetcher.inner.restful.impl.RestfulMgrImpl;
import com.baidu.disconf.client.test.common.BaseTestCase;
import com.baidu.disconf.client.test.mockserver.DisconfWebMockServer;
import com.baidu.disconf.core.common.json.ValueVo;

/**
 * 使用 WireMock 进行测试
 * 
 * @author liaoqiqi
 * @version 2014-7-30
 */
public class RestfulMgrTestCase extends BaseTestCase {

    private RestfulMgr restfulMgr = null;

    @Before
    public void myInit() {

        restfulMgr = new RestfulMgrImpl();
        try {
            restfulMgr.init();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @After
    public void release() {

        restfulMgr.close();
    }

    /**
     * 
     */
    @Test
    public void tetGetJsonData() {

        try {
            RemoteUrl remoteUrl = new RemoteUrl(DisconfWebMockServer.ITEM_URL,
                    DisClientConfig.getInstance().getHostList());

            ValueVo valueVo = restfulMgr.getJsonData(ValueVo.class, remoteUrl,
                    3, 3);

            System.out.println(valueVo);

        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    /**
     * 
     */
    public void tetDownloadFromServer() {

        try {
            RemoteUrl remoteUrl = new RemoteUrl(DisconfWebMockServer.FILE_URL,
                    DisClientConfig.getInstance().getHostList());

            String downloadFilePath = restfulMgr.downloadFromServer(remoteUrl,
                    DisconfWebMockServer.FILE_NAME, "",
                    DisClientSysConfig.getInstance().LOCAL_DOWNLOAD_DIR, true,
                    3, 3);

            System.out.println(downloadFilePath);
            
            DisClientConfig.getInstance().getHostList();

        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }
}
