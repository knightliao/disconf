package com.baidu.disconf.client.test;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.baidu.disconf.client.DisconfMgr;
import com.baidu.disconf.client.store.DisconfStoreMgr;
import com.baidu.disconf.client.test.common.BaseTestCase;
import com.baidu.disconf.client.test.model.ConfA;
import com.baidu.disconf.client.test.model.ServiceA;

/**
 * 
 * 一个Demo示例
 * 
 * @author liaoqiqi
 * @version 2014-6-10
 */
public class DisconfMgrTestCase extends BaseTestCase {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(DisconfMgrTestCase.class);

    @Autowired
    private ConfA confA;

    @Autowired
    private ServiceA serviceA;

    @Test
    public void demo() {

        String packName = "com.baidu.disconf2.client.test";

        try {

            //
            if (!checkNetWork()) {
                return;
            }

            LOGGER.info("================ BEFORE DISCONF ==============================");

            LOGGER.info("before disconf values:");
            LOGGER.info(String.valueOf("varA: " + confA.getVarA()));
            LOGGER.info(String.valueOf("varA2: " + confA.getVarA2()));
            LOGGER.info(String.valueOf("varAA: " + serviceA.getVarAA()));

            LOGGER.info("================ BEFORE DISCONF ==============================");

            //
            //
            //
            DisconfMgr.start(packName);

            //
            LOGGER.info(DisconfStoreMgr.getInstance().getConfFileMap()
                    .toString());

            //
            LOGGER.info(DisconfStoreMgr.getInstance().getConfItemMap()
                    .toString());

            LOGGER.info("================ AFTER DISCONF ==============================");

            LOGGER.info(String.valueOf("varA: " + confA.getVarA()));
            LOGGER.info(String.valueOf("varA2: " + confA.getVarA2()));
            LOGGER.info(String.valueOf("varAA: " + serviceA.getVarAA()));

            LOGGER.info("================ AFTER DISCONF ==============================");

            Thread.sleep(10000);

        } catch (Exception e) {

            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }
}
