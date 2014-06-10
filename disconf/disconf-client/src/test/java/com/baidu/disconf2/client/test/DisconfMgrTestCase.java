package com.baidu.disconf2.client.test;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf2.client.DisconfMgr;
import com.baidu.disconf2.client.core.DisconfCoreMgr;

/**
 * 
 * @author liaoqiqi
 * @version 2014-6-10
 */
public class DisconfMgrTestCase {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(DisconfMgrTestCase.class);

    @Test
    public void scan() {

        String packName = "com.baidu.disconf2.client";

        try {

            DisconfMgr.run(packName);

            //
            LOGGER.info(DisconfCoreMgr.getInstance().getConfFileMap()
                    .toString());

            //
            LOGGER.info(DisconfCoreMgr.getInstance().getConfItemMap()
                    .toString());

        } catch (Exception e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }
}
