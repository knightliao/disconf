package com.baidu.disconf2.client.fetcher.inner.restful.retry;

import java.io.File;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf2.client.fetcher.inner.restful.core.RemoteUrl;
import com.baidu.disconf2.client.fetcher.inner.restful.core.UnreliableInterface;
import com.baidu.disconf2.client.fetcher.inner.restful.file.FetchConfFile;

/**
 * 重试的策略
 * 
 * @author liaoqiqi
 * @version 2014-6-10
 */
public class RetryProxy {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(RetryProxy.class);

    /**
     * 
     * @param unreliableImpl
     * @param retryTimes
     * @param sleepSeconds
     */
    public static Object retry(UnreliableInterface unreliableImpl,
            int retryTimes, int sleepSeconds) throws Exception {

        int cur_time = 0;
        for (; cur_time < retryTimes; ++cur_time) {

            try {

                return unreliableImpl.call();

            } catch (Exception e) {

                LOGGER.warn("cannot reach, will retry " + cur_time + " .... "
                        + e.toString());

                try {
                    Thread.sleep(sleepSeconds * 1000);
                } catch (InterruptedException e1) {
                }
            }
        }

        LOGGER.warn("finally failed....");

        throw new Exception();
    }

    /**
     * 
     * Retry封装 RemoteUrl 支持多Server的下载
     * 
     * @param remoteUrl
     * @param localTmpFile
     * @param retryTimes
     * @param sleepSeconds
     * @return
     */
    public static Object retry4ConfDownload(RemoteUrl remoteUrl,
            File localTmpFile, int retryTimes, int sleepSeconds)
            throws Exception {

        for (URL url : remoteUrl.getUrls()) {

            // 可重试的下载
            UnreliableInterface unreliableImpl = new FetchConfFile(url,
                    localTmpFile);

            try {

                return RetryProxy.retry(unreliableImpl, retryTimes,
                        sleepSeconds);

            } catch (Exception e) {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                }
            }
        }

        throw new Exception();
    }

}
