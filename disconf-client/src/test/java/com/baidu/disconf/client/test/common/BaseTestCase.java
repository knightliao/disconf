package com.baidu.disconf.client.test.common;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baidu.disconf.client.config.ConfigMgr;
import com.baidu.disconf.client.config.DisClientConfig;
import com.baidu.disconf.client.config.DisClientSysConfig;
import com.baidu.disconf.client.test.mockserver.DisconfWebMockServer;
import com.baidu.disconf.core.common.constants.Constants;
import com.baidu.disconf.core.common.json.ValueVo;
import com.baidu.disconf.core.common.path.DisconfWebPathMgr;
import com.baidu.utils.GsonUtils;
import com.baidu.utils.NetUtils;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;

/**
 * 
 * 采用 WireMock 测试法
 * 
 * @author liaoqiqi
 * @version 2014-6-11
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class BaseTestCase {

    @ClassRule
    @Rule
    public static WireMockClassRule wireMockRule = new WireMockClassRule(
            DisconfWebMockServer.PORT);

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(BaseTestCase.class);

    /**
     * 导入配置
     */
    @BeforeClass
    public static void init() {

        // 导入配置
        try {
            ConfigMgr.init();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Assert.assertFalse(true);
        }

        //
        // 设置Mock服务数据
        //
        setupRemoteData();
    }

    /**
     * 
     */
    private static void setupRemoteData() {

        //
        // 配置项
        //
        ValueVo valueVo = new ValueVo();
        valueVo.setMessage("");
        valueVo.setStatus(Constants.OK);
        valueVo.setValue(DisconfWebMockServer.DEFAULT_ITEM_VALUE);
        System.out.println(GsonUtils.toJson(valueVo));
        stubFor(get(urlEqualTo(DisconfWebMockServer.ITEM_URL)).willReturn(
                aResponse()
                        .withHeader("Content-Type",
                                DisconfWebMockServer.CONTENT_TYPE)
                        .withStatus(200).withBody(GsonUtils.toJson(valueVo))));

        //
        // 配置文件
        //
        stubFor(get(urlEqualTo(DisconfWebMockServer.FILE_URL)).willReturn(
                aResponse()
                        .withHeader("Content-Type",
                                DisconfWebMockServer.CONTENT_TYPE)
                        .withStatus(200).withBody(GsonUtils.toJson(valueVo))));
    }

    @Test
    public void pass() {

    }

    /**
     * 
     * @return
     */
    @Deprecated
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
