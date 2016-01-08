package com.baidu.disconf.core.common.restful.retry.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf.core.common.restful.core.UnreliableInterface;
import com.baidu.disconf.core.common.restful.retry.RetryStrategy;

/**
 * 轮循重试
 *
 * @author liaoqiqi
 * @version 2014-8-4
 */
public class RetryStrategyRoundBin implements RetryStrategy {

    protected static final Logger LOGGER = LoggerFactory.getLogger(RetryStrategyRoundBin.class);

    /**
     * @param unreliableImpl
     * @param retryTimes
     * @param sleepSeconds
     */
    public <T> T retry(UnreliableInterface unreliableImpl, int retryTimes, int sleepSeconds) throws Exception {

        int cur_time = 0;
        for (; cur_time < retryTimes; ++cur_time) {

            try {

                return unreliableImpl.call();

            } catch (Exception e) {

                LOGGER.warn("cannot reach, will retry " + cur_time + " .... " + e.toString());

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
