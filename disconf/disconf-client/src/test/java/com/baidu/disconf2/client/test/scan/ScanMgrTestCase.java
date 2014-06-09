package com.baidu.disconf2.client.test.scan;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf2.client.core.DisconfCoreMgr;
import com.baidu.disconf2.client.scan.ScanMgr;

public class ScanMgrTestCase {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(ScanMgrTestCase.class);

    @Test
    public void scan() {

        String packName = "com.baidu.disconf2.client";

        try {

            //
            ScanMgr.scanAndStore(packName);

            //
            LOGGER.info(DisconfCoreMgr.getInstance().getConfFileMap()
                    .toString());

        } catch (Exception e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }
}
