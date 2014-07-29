package com.baidu.disconf.client.test.fetcher;

import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.Test;

import com.baidu.disconf.client.fetcher.FetcherMgr;
import com.baidu.disconf.client.fetcher.impl.FetcherMgrImpl;
import com.baidu.disconf.client.fetcher.inner.restful.RestfulMgr;
import com.baidu.disconf.client.test.fetcher.inner.restful.RestfulMgrMock;

/**
 * 
 * FetcherMgrMgr测试
 * 
 * @author liaoqiqi
 * @version 2014-6-17
 */
public class FetcherMgrMgrTestCase {

    private static String requestUrl = "/url";

    /**
     * 验证获取数据的接口
     * 
     * @throws Exception
     */
    @Test
    public void testGetValueFromServer() throws Exception {

        final RestfulMgr restfulMgr = new RestfulMgrMock().getMockInstance();

        FetcherMgr fetcherMgr = new FetcherMgrImpl(restfulMgr, 3, 5, true, "",
                new ArrayList<String>());

        try {

            String valueString = fetcherMgr.getValueFromServer(requestUrl);
            Assert.assertEquals(RestfulMgrMock.defaultValue, valueString);

        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    /**
     * 验证下载文件的接口
     * 
     * @throws Exception
     */
    @Test
    public void testDownloadFileFromServer() throws Exception {

        final RestfulMgr restfulMgr = new RestfulMgrMock().getMockInstance();

        FetcherMgr fetcherMgr = new FetcherMgrImpl(restfulMgr, 3, 5, true, "",
                new ArrayList<String>());

        try {

            String valueString = fetcherMgr.downloadFileFromServer(requestUrl,
                    RestfulMgrMock.defaultFileName);
            Assert.assertEquals(RestfulMgrMock.defaultFileName, valueString);

        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }
}
