package com.baidu.disconf2.client.test;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.baidu.disconf2.client.DisconfMgr;
import com.baidu.disconf2.client.config.ConfigMgr;
import com.baidu.disconf2.client.config.inner.DisClientConfig;
import com.baidu.disconf2.client.config.inner.DisClientSysConfig;
import com.baidu.disconf2.client.store.DisconfStoreMgr;
import com.baidu.disconf2.client.test.common.BaseTestCase;
import com.baidu.disconf2.client.test.model.ConfA;
import com.baidu.disconf2.client.test.model.ServiceA;
import com.baidu.disconf2.core.common.path.DisconfWebPathMgr;
import com.baidu.utils.NetUtils;

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

        String packName = "com.baidu.disconf2.client";

        try {

            //
            // 如果网络不通则认为测试通过
            //
            ConfigMgr.init();
            if (!NetUtils.pingUrl(DisClientConfig.getInstance().getHostList()
                    .get(0)
                    + DisconfWebPathMgr.getZooHostsUrl(DisClientSysConfig
                            .getInstance().CONF_SERVER_ZOO_ACTION))) {
                return;
            }

            //
            //
            //

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

            Thread.sleep(10000000);

        } catch (Exception e) {

            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }
}
