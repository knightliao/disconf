package com.baidu.disconf2.client.test.common;

import org.junit.Assert;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf2.client.test.model.ConfA;
import com.baidu.disconf2.client.test.model.ConfB;
import com.baidu.utils.DisconfAutowareConfig;

/**
 * 
 * @author liaoqiqi
 * @version 2014-6-11
 */
public class BaseTestCase {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(BaseTestCase.class);

    @Before
    public void init() {

        try {

            //
            // CONFB
            //
            DisconfAutowareConfig.autowareConfig(ConfB.getInstance(),
                    ConfB.filename);
            LOGGER.info("ConfB-VarB: "
                    + String.valueOf(ConfB.getInstance().getVarB()));

            //
            // CONFA
            //
            DisconfAutowareConfig.autowareStatucConfig(ConfA.class,
                    ConfA.filename);
            LOGGER.info("ConfA-varA: " + String.valueOf(ConfA.getVarA()));

        } catch (Exception e) {

            e.printStackTrace();
            Assert.assertTrue(false);
        }

    }
}
