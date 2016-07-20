package com.baidu.disconf.client.test.fetcher;

import java.util.ArrayList;

import org.junit.Test;

import com.baidu.disconf.client.fetcher.FetcherMgr;
import com.baidu.disconf.client.fetcher.impl.FetcherMgrImpl;
import com.baidu.disconf.client.test.common.BaseSpringTestCase;
import com.baidu.disconf.client.test.fetcher.inner.restful.RestfulMgrMock;
import com.baidu.disconf.core.common.restful.RestfulMgr;

import junit.framework.Assert;

/**
 * FetcherMgrMgr测试 (采用Jmockit方法测试)
 *
 * @author liaoqiqi
 * @version 2014-6-17
 */
public class FetcherMgrMgrTestCase extends BaseSpringTestCase {

    private static String requestUrl = "/url";

    /**
     * 验证获取数据的接口
     *
     * @throws Exception
     */
    @Test
    public void testGetValueFromServer() throws Exception {

        final RestfulMgr restfulMgr = new RestfulMgrMock().getMockInstance();

        FetcherMgr fetcherMgr = new FetcherMgrImpl(restfulMgr, 3, 5, true, "", "", new ArrayList<String>());

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

        FetcherMgr fetcherMgr = new FetcherMgrImpl(restfulMgr, 3, 5, true, "", "", new ArrayList<String>());

        try {

            String valueString = fetcherMgr.downloadFileFromServer(requestUrl, RestfulMgrMock.defaultFileName,
                    "./disconf");
            Assert.assertEquals(RestfulMgrMock.defaultFileName, valueString);

        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }
}
