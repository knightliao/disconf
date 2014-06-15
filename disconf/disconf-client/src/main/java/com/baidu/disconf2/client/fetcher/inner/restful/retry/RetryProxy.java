package com.baidu.disconf2.client.fetcher.inner.restful.retry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf2.client.fetcher.inner.restful.core.UnreliableInterface;

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

}
