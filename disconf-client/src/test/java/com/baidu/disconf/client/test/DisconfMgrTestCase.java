package com.baidu.disconf.client.test;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.baidu.disconf.client.DisconfMgr;
import com.baidu.disconf.client.DisconfMgrBean;
import com.baidu.disconf.client.core.DisconfCoreFactory;
import com.baidu.disconf.client.core.DisconfCoreMgr;
import com.baidu.disconf.client.core.impl.DisconfCoreMgrImpl;
import com.baidu.disconf.client.fetcher.FetcherFactory;
import com.baidu.disconf.client.fetcher.FetcherMgr;
import com.baidu.disconf.client.store.DisconfStoreProcessorFactory;
import com.baidu.disconf.client.store.inner.DisconfCenterHostFilesStore;
import com.baidu.disconf.client.support.registry.Registry;
import com.baidu.disconf.client.test.common.BaseSpringMockTestCase;
import com.baidu.disconf.client.test.model.ConfA;
import com.baidu.disconf.client.test.model.ServiceA;
import com.baidu.disconf.client.test.model.StaticConf;
import com.baidu.disconf.client.test.scan.inner.ScanPackTestCase;
import com.baidu.disconf.client.test.watch.mock.WatchMgrMock;
import com.baidu.disconf.client.utils.StringUtil;
import com.baidu.disconf.client.watch.WatchMgr;

import mockit.Mock;
import mockit.MockUp;

/**
 * 一个Demo示例, 远程的下载服务器使用WireMOck, Watch模块使用Jmockit
 *
 * @author liaoqiqi
 * @version 2014-6-10
 */
public class DisconfMgrTestCase extends BaseSpringMockTestCase {

    protected static final Logger LOGGER = LoggerFactory.getLogger(DisconfMgrTestCase.class);

    @Autowired
    private ConfA confA;

    @Autowired
    private ServiceA serviceA;

    @Test
    public void demo() {

        //
        // mock up factory method
        //
        new MockUp<DisconfCoreFactory>() {

            @Mock
            public DisconfCoreMgr getDisconfCoreMgr(Registry registry) throws Exception {

                FetcherMgr fetcherMgr = FetcherFactory.getFetcherMgr();

                // Watch 模块
                final WatchMgr watchMgr = new WatchMgrMock().getMockInstance();
                watchMgr.init("", "", true);

                // registry
                DisconfCoreMgr disconfCoreMgr = new DisconfCoreMgrImpl(watchMgr, fetcherMgr,
                        registry);

                return disconfCoreMgr;
            }
        };

        //
        // 正式测试
        //
        try {

            LOGGER.info("================ BEFORE DISCONF ==============================");

            LOGGER.info("before disconf values:");
            LOGGER.info(String.valueOf("varA: " + confA.getVarA()));
            LOGGER.info(String.valueOf("varA2: " + confA.getVarA2()));
            LOGGER.info(String.valueOf("varAA: " + serviceA.getVarAA()));

            LOGGER.info("================ BEFORE DISCONF ==============================");

            //
            // start it
            //
            Set<String> fileSet = new HashSet<String>();
            fileSet.add("atomserverl.properties");
            fileSet.add("atomserverm_slave.properties");
            DisconfCenterHostFilesStore.getInstance().addJustHostFileSet(fileSet);

            DisconfMgr.getInstance().start(StringUtil.parseStringToStringList(ScanPackTestCase.SCAN_PACK_NAME,
                    DisconfMgrBean.SCAN_SPLIT_TOKEN));

            //
            LOGGER.info(DisconfStoreProcessorFactory.getDisconfStoreFileProcessor().confToString());

            //
            LOGGER.info(DisconfStoreProcessorFactory.getDisconfStoreItemProcessor().confToString());

            LOGGER.info("================ AFTER DISCONF ==============================");

            LOGGER.info(String.valueOf("varA: " + confA.getVarA()));
            Assert.assertEquals(new Long(1000), confA.getVarA());

            LOGGER.info(String.valueOf("varA2: " + confA.getVarA2()));
            Assert.assertEquals(new Long(2000), confA.getVarA2());

            LOGGER.info(String.valueOf("varAA: " + serviceA.getVarAA()));
            Assert.assertEquals(new Integer(1000).intValue(), serviceA.getVarAA());

            LOGGER.info(String.valueOf("staticvar: " + StaticConf.getStaticvar()));
            Assert.assertEquals(new Integer(50).intValue(), StaticConf.getStaticvar());

            LOGGER.info("================ AFTER DISCONF ==============================");

        } catch (Exception e) {

            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }
}
