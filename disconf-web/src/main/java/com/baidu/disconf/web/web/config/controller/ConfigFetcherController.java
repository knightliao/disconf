package com.baidu.disconf.web.web.config.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baidu.disconf.core.common.constants.DisConfigTypeEnum;
import com.baidu.disconf.core.common.json.ValueVo;
import com.baidu.disconf.web.service.config.bo.Config;
import com.baidu.disconf.web.service.config.form.ConfForm;
import com.baidu.disconf.web.service.config.service.ConfigFetchMgr;
import com.baidu.disconf.web.service.config.utils.ConfigUtils;
import com.baidu.disconf.web.web.config.dto.ConfigFullModel;
import com.baidu.disconf.web.web.config.validator.ConfigValidator;
import com.baidu.disconf.web.web.config.validator.ConfigValidator4Fetch;
import com.baidu.dsp.common.annotation.NoAuth;
import com.baidu.dsp.common.constant.WebConstants;
import com.baidu.dsp.common.controller.BaseController;
import com.baidu.dsp.common.exception.DocumentNotFoundException;
import com.baidu.dsp.common.vo.JsonObjectBase;

/**
 * 配置获取Controller, Disconf-client专门使用的
 *
 * @author liaoqiqi
 * @version 2014-6-16
 */
@Controller
@RequestMapping(WebConstants.API_PREFIX + "/config")
public class ConfigFetcherController extends BaseController {

    protected static final Logger LOG = LoggerFactory.getLogger(ConfigFetcherController.class);

    @Autowired
    private ConfigValidator configValidator;

    @Autowired
    private ConfigValidator4Fetch configValidator4Fetch;

    @Autowired
    private ConfigFetchMgr configFetchMgr;

    /**
     * 获取指定app env version 的配置项列表
     *
     * @param confForm
     *
     * @return
     */
    @NoAuth
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public JsonObjectBase getList(ConfForm confForm) {
        return getListImp(confForm, true);
    }

    @NoAuth
    @RequestMapping(value = "/simple/list", method = RequestMethod.GET)
    @ResponseBody
    public JsonObjectBase getSimpleList(ConfForm confForm) {
        return getListImp(confForm, false);
    }

    /**
     * 获取配置项 Item
     *
     * @param confForm
     *
     * @return
     */
    @NoAuth
    @RequestMapping(value = "/item", method = RequestMethod.GET)
    @ResponseBody
    public ValueVo getItem(ConfForm confForm) {

        LOG.info(confForm.toString());

        //
        // 校验
        //
        ConfigFullModel configModel = null;
        try {
            configModel = configValidator4Fetch.verifyConfForm(confForm, false);
        } catch (Exception e) {
            LOG.warn(e.toString());
            return ConfigUtils.getErrorVo(e.getMessage());
        }

        return configFetchMgr.getConfItemByParameter(configModel.getApp().getId(), configModel.getEnv().getId(),
                configModel.getVersion(), configModel.getKey());
    }

    /**
     * 获取配置文件
     *
     * @return
     */
    @NoAuth
    @RequestMapping(value = "/file", method = RequestMethod.GET)
    @ResponseBody
    public HttpEntity<byte[]> getFile(ConfForm confForm) {

        boolean hasError = false;

        //
        // 校验
        //
        ConfigFullModel configModel = null;
        try {
            configModel = configValidator4Fetch.verifyConfForm(confForm, false);
        } catch (Exception e) {
            LOG.error(e.toString());
            hasError = true;
        }

        if (hasError == false) {
            try {
                //
                Config config = configFetchMgr
                        .getConfByParameter(configModel.getApp().getId(), configModel.getEnv().getId(),
                                configModel.getVersion(), configModel.getKey(),
                                DisConfigTypeEnum.FILE);
                if (config == null) {
                    hasError = true;
                    throw new DocumentNotFoundException(configModel.getKey());
                }
                //API获取节点内容也需要同样做格式转换
                return downloadDspBill(configModel.getKey(), config.getValue());

            } catch (Exception e) {
                LOG.error(e.toString());
            }
        }

        if (confForm.getKey() != null) {
            throw new DocumentNotFoundException(confForm.getKey());
        } else {
            throw new DocumentNotFoundException("");
        }
    }

    /**
     * 下载
     *
     * @param fileName
     *
     * @return
     */
    public HttpEntity<byte[]> downloadDspBill(String fileName, String value) {

        HttpHeaders header = new HttpHeaders();
        byte[] res = value.getBytes();

        String name = null;

        try {
            name = URLEncoder.encode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        header.set("Content-Disposition", "attachment; filename=" + name);
        header.setContentLength(res.length);
        return new HttpEntity<byte[]>(res, header);
    }

    private JsonObjectBase getListImp(ConfForm confForm, boolean hasValue) {
        LOG.info(confForm.toString());

        //
        // 校验
        //
        ConfigFullModel configModel = configValidator4Fetch.verifyConfForm(confForm, true);

        List<Config> configs =
                configFetchMgr.getConfListByParameter(configModel.getApp().getId(), configModel.getEnv().getId(),
                        configModel.getVersion(), hasValue);

        return buildListSuccess(configs, configs.size());
    }

}
