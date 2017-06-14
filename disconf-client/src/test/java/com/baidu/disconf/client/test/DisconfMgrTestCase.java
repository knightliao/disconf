package com.baidu.disconf.client.test;

import com.baidu.disconf.client.DisconfMgr;
import com.baidu.disconf.client.DisconfMgrBean;
import com.baidu.disconf.client.store.DisconfStoreProcessorFactory;
import com.baidu.disconf.client.store.inner.DisconfCenterHostFilesStore;
import com.baidu.disconf.client.support.utils.StringUtil;
import com.baidu.disconf.client.test.common.BaseSpringMockTestCase;
import com.baidu.disconf.client.test.model.ConfA;
import com.baidu.disconf.client.test.model.ServiceA;
import com.baidu.disconf.client.test.model.StaticConf;
import com.baidu.disconf.client.test.scan.inner.ScanPackTestCase;
import com.baidu.disconf.client.usertools.DisconfDataGetter;
import com.baidu.disconf.core.common.constants.Constants;
import com.baidu.disconf.core.common.json.ValueVo;
import com.baidu.disconf.core.common.utils.GsonUtils;
import com.baidu.disconf.core.common.zookeeper.ZookeeperMgr;
import com.baidu.disconf.core.test.restful.RemoteMockServer;
import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.CuratorFrameworkFactory;
import com.netflix.curator.framework.state.ConnectionState;
import com.netflix.curator.framework.state.ConnectionStateListener;
import com.netflix.curator.retry.ExponentialBackoffRetry;
import com.netflix.curator.test.TestingServer;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashSet;
import java.util.Set;

import static com.baidu.disconf.core.test.restful.RemoteMockServer.*;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

/**
 * 一个Demo示例, 远程的下载服务器使用WireMOck, Watch模块使用Jmockit
 *
 * @author liaoqiqi
 * @version 2014-6-10
 */
public class DisconfMgrTestCase extends BaseSpringMockTestCase implements ApplicationContextAware {

    protected static final Logger LOGGER = LoggerFactory.getLogger(DisconfMgrTestCase.class);

    // application context
    private ApplicationContext applicationContext;

    @Autowired
    private ConfA confA;

    @Autowired
    private ServiceA serviceA;

    @Test
    public void demo() {
        try {
            // 使用curator模拟zk
            TestingServer server = new TestingServer(ZOO_PORT);
            CuratorFramework client = CuratorFrameworkFactory.newClient(server.getConnectString(), new ExponentialBackoffRetry(1000, 3));
            client.getConnectionStateListenable().addListener(new ConnectionStateListener() {
                @Override
                public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {
                    System.out.println("连接状态：" + connectionState.name());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(false);
        }

        //
        // 正式测试
        //
        try {

            LOGGER.info("================ BEFORE DISCONF ==============================");

            LOGGER.info("before disconf values:");
            LOGGER.info(String.valueOf("varA: " + confA.getVarA()));
            LOGGER.info(String.valueOf("varA2: " + confA.getVarA2()));
            LOGGER.info(String.valueOf("varAA: " + serviceA.getVarAA()));
            LOGGER.info(String.valueOf("varAAStatic: " + serviceA.getVarAAStatic()));

            LOGGER.info("================ BEFORE DISCONF ==============================");

            //
            // start it
            //
            Set<String> fileSet = new HashSet<String>();
            fileSet.add("atomserverl.properties");
            fileSet.add("atomserverm_slave.properties");
            DisconfCenterHostFilesStore.getInstance().addJustHostFileSet(fileSet);

            DisconfMgr.getInstance().setApplicationContext(applicationContext);
            DisconfMgr.getInstance().start(StringUtil.parseStringToStringList(ScanPackTestCase.SCAN_PACK_NAME,
                    DisconfMgrBean.SCAN_SPLIT_TOKEN));

            //
            LOGGER.info(DisconfStoreProcessorFactory.getDisconfStoreFileProcessor().confToString());

            //
            LOGGER.info(DisconfStoreProcessorFactory.getDisconfStoreItemProcessor().confToString());

            LOGGER.info("================ AFTER DISCONF ==============================");

            LOGGER.info(String.valueOf("varA: " + confA.getVarA()));
            Assert.assertEquals(new Long(1000), confA.getVarA());

            // 修改配置文件的值
            stubFor(get(urlEqualTo(RemoteMockServer.FILE_URL))
                    .willReturn(aResponse().withHeader("Content-Type", "text/html;charset=UTF-8")
                            .withHeader("Content-Disposition",
                                    "attachment; filename=" + RemoteMockServer.FILE_NAME).withStatus(200)
                            .withBody(FILE_CONTENT_NEW.getBytes())));
            // 通知zk
            ZookeeperMgr.getInstance().writePersistentUrl(ZOO_FILE_UPDATE_PATH, GsonUtils.toJson(FILE_CONTENT_NEW));

            Thread.sleep(1000);

            // 获取修改后的值
            LOGGER.info(String.valueOf("varA new: " + confA.getVarA()));
            Assert.assertEquals(new Long(9999), confA.getVarA());

            LOGGER.info(String.valueOf("varA2: " + confA.getVarA2()));
            Assert.assertEquals(new Long(2000), confA.getVarA2());

            LOGGER.info(String.valueOf("varAA: " + serviceA.getVarAA()));
            Assert.assertEquals(new Integer(1000).intValue(), serviceA.getVarAA());

            // 修改配置项的值
            ValueVo valueVo = new ValueVo();
            valueVo.setMessage("");
            valueVo.setStatus(Constants.OK);
            valueVo.setValue(RemoteMockServer.DEFAULT_ITEM_VALUE_NEW);
            stubFor(get(urlEqualTo(RemoteMockServer.ITEM_URL))
                    .willReturn(aResponse().withHeader("Content-Type", RemoteMockServer.CONTENT_TYPE).withStatus(200)
                            .withBody(GsonUtils.toJson(valueVo))));
            // 通知zk
            ZookeeperMgr.getInstance().writePersistentUrl(ZOO_ITEM_UPDATE_PATH, RemoteMockServer.DEFAULT_ITEM_VALUE_NEW);

            Thread.sleep(1000);

            // 获取修改后的值
            LOGGER.info(String.valueOf("varAA new: " + serviceA.getVarAA()));
            Assert.assertEquals(new Integer(8888).intValue(), serviceA.getVarAA());

            LOGGER.info(String.valueOf("varAAStatic: " + serviceA.getVarAAStatic()));
            Assert.assertEquals(new Integer(2001).intValue(), serviceA.getVarAAStatic());

            LOGGER.info(String.valueOf("varBBStatic: " + serviceA.getVarBBStatic()));
            Assert.assertEquals(new Integer(3001).intValue(), serviceA.getVarBBStatic());

            LOGGER.info(String.valueOf("static var: " + StaticConf.getStaticvar()));
            Assert.assertEquals(new Integer(50).intValue(), StaticConf.getStaticvar());

            LOGGER.info(String.valueOf("static var: " + StaticConf.getStaticvar3()));
            Assert.assertEquals(new Integer(4001).intValue(), StaticConf.getStaticvar3());

            testDynamicGetter();

            LOGGER.info("================ AFTER DISCONF ==============================");

        } catch (Exception e) {

            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    private void testDynamicGetter() {

        Assert.assertEquals(DisconfDataGetter.getByFile("confA.properties").get("confa.varA").toString(),
                "9999");

        Assert.assertEquals(DisconfDataGetter.getByItem("keyA").toString(),
                "8888");

        Assert.assertEquals(DisconfDataGetter.getByFileItem("confA.properties", "confa.varA").toString(),
                "9999");
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
