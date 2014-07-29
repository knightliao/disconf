package com.baidu.disconf.client.test.common;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baidu.disconf.client.config.ConfigMgr;
import com.baidu.disconf.client.config.DisClientConfig;
import com.baidu.disconf.client.config.DisClientSysConfig;
import com.baidu.disconf.core.common.path.DisconfWebPathMgr;
import com.baidu.utils.NetUtils;

/**
 * 
 * @author liaoqiqi
 * @version 2014-6-11
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class BaseTestCase {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(BaseTestCase.class);

    @Before
    public void init() {

    }

    @Test
    public void pass() {

    }

    /**
     * 
     * @return
     */
    protected boolean checkNetWork() {

        //
        // 如果网络不通则认为测试通过
        //
        try {
            ConfigMgr.init();
        } catch (Exception e) {
            Assert.assertTrue(false);
        }

        if (!NetUtils.pingUrl(DisClientConfig.getInstance().getHostList()
                .get(0)
                + DisconfWebPathMgr.getZooHostsUrl(DisClientSysConfig
                        .getInstance().CONF_SERVER_ZOO_ACTION))) {
            return false;
        }

        return true;
    }

}
