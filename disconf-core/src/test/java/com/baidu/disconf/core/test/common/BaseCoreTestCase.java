package com.baidu.disconf.core.test.common;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf.core.common.constants.Constants;
import com.baidu.disconf.core.common.json.ValueVo;
import com.baidu.disconf.core.common.utils.GsonUtils;
import com.baidu.disconf.core.test.restful.RemoteMockServer;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;

/**
 * @author liaoqiqi
 * @version 2014-6-11
 */
public class BaseCoreTestCase {

    @ClassRule
    @Rule
    public static WireMockClassRule wireMockRule = new WireMockClassRule(RemoteMockServer.PORT);

    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseCoreTestCase.class);

    /**
     * 导入配置
     */
    @Before
    public void init() {

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
        valueVo.setValue(RemoteMockServer.DEFAULT_ITEM_VALUE);
        // System.out.println(GsonUtils.toJson(valueVo));
        stubFor(get(urlEqualTo(RemoteMockServer.ITEM_URL))
                .willReturn(aResponse().withHeader("Content-Type", RemoteMockServer.CONTENT_TYPE).withStatus(200)
                        .withBody(GsonUtils.toJson(valueVo))));

        //
        // 配置文件
        //
        stubFor(get(urlEqualTo(RemoteMockServer.FILE_URL))
                .willReturn(aResponse().withHeader("Content-Type", "text/html;charset=UTF-8")
                        .withHeader("Content-Disposition",
                                "attachment; filename=" + RemoteMockServer.FILE_NAME).withStatus(200)
                        .withBody(RemoteMockServer.FILE_CONTENT.getBytes())));

        //
        // 空配置文件
        //
        stubFor(get(urlEqualTo(RemoteMockServer.EMPTY_FILE_URL))
                .willReturn(aResponse().withHeader("Content-Type", "text/html;charset=UTF-8")
                        .withHeader("Content-Disposition",
                                "attachment; filename=" + RemoteMockServer.EMPTY_FILE_URL)
                        .withStatus(200).withBody(RemoteMockServer.FILE_CONTENT.getBytes())));

        //
        // 静态 配置文件
        //
        stubFor(get(urlEqualTo(RemoteMockServer.STATIC_FILE_URL))
                .willReturn(aResponse().withHeader("Content-Type", "text/html;charset=UTF-8")
                        .withHeader("Content-Disposition",
                                "attachment; filename=" + RemoteMockServer.STATIC_FILE_NAME)
                        .withStatus(200).withBody(RemoteMockServer.STATIC_FILE_CONTENT.getBytes())));

        //
        // 非注解 配置文件
        //
        stubFor(get(urlEqualTo(RemoteMockServer.NON_ANOTATION_FILE_URL))
                .willReturn(aResponse().withHeader("Content-Type", "text/html;charset=UTF-8")
                        .withHeader("Content-Disposition",
                                "attachment; filename=" + RemoteMockServer.NON_ANOTATION_FILE_NAME)
                        .withStatus(200)
                        .withBody(RemoteMockServer.NON_ANOTATTION_FILE_CONTENT.getBytes())));

        //
        // 非注解 配置文件2
        //
        stubFor(get(urlEqualTo(RemoteMockServer.NON_ANOTATION_FILE_URL2))
                .willReturn(aResponse().withHeader("Content-Type", "text/html;charset=UTF-8")
                        .withHeader("Content-Disposition",
                                "attachment; filename=" + RemoteMockServer.NON_ANOTATION_FILE_NAME2)
                        .withStatus(200)
                        .withBody(RemoteMockServer.NON_ANOTATTION_FILE_CONTENT2.getBytes())));

        //
        // ZOO
        //
        valueVo = new ValueVo();
        valueVo.setMessage("");
        valueVo.setStatus(Constants.OK);
        valueVo.setValue(RemoteMockServer.ZOO_HOSTS);
        // System.out.println(GsonUtils.toJson(valueVo));
        stubFor(get(urlEqualTo(RemoteMockServer.ZOO_URL))
                .willReturn(aResponse().withHeader("Content-Type", RemoteMockServer.CONTENT_TYPE).withStatus(200)
                        .withBody(GsonUtils.toJson(valueVo))));

        valueVo = new ValueVo();
        valueVo.setMessage("");
        valueVo.setStatus(Constants.OK);
        valueVo.setValue(RemoteMockServer.ZOO_PREFIX_VALUE);
        // System.out.println(GsonUtils.toJson(valueVo));
        stubFor(get(urlEqualTo(RemoteMockServer.ZOO_PREFIX_URL))
                .willReturn(aResponse().withHeader("Content-Type", RemoteMockServer.CONTENT_TYPE).withStatus(200)
                        .withBody(GsonUtils.toJson(valueVo))));

    }

    @Test
    public void pass() {

    }
}
