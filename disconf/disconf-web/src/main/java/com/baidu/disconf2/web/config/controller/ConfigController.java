package com.baidu.disconf2.web.config.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baidu.disconf2.core.common.json.ValueVo;
import com.baidu.disconf2.service.config.bo.Config;
import com.baidu.disconf2.service.config.form.ConfForm;
import com.baidu.disconf2.service.config.service.ConfigMgr;
import com.baidu.disconf2.service.config.utils.ConfigUtils;
import com.baidu.disconf2.web.config.validator.ConfigValidator;
import com.baidu.disconf2.web.config.validator.ConfigValidator.ConfigModel;
import com.baidu.dsp.common.constant.WebConstants;
import com.baidu.dsp.common.exception.DocumentNotFoundException;

/**
 * 
 * @author liaoqiqi
 * @version 2014-6-16
 */
@Controller
@RequestMapping(WebConstants.API_PREFIX + "/config")
public class ConfigController {

    protected static final Logger LOG = LoggerFactory
            .getLogger(ConfigController.class);

    @Autowired
    private ConfigValidator configValidator;

    @Autowired
    private ConfigMgr configMgr;

    /**
     * 获取配置项 Item
     * 
     * @param demoUserId
     * @return
     */
    @RequestMapping(value = "/item", method = RequestMethod.GET)
    @ResponseBody
    public ValueVo getItem(ConfForm confForm) {

        //
        // 校验
        //
        ConfigModel configModel = null;
        try {
            configModel = configValidator.verifyConfForm(confForm);
        } catch (Exception e) {
            LOG.warn(e.toString());
            return ConfigUtils.getErrorVo(e.getMessage());
        }

        return configMgr.getConfItemByParameter(configModel.getAppId(),
                configModel.getEnvId(), configModel.getVersion(),
                configModel.getKey());
    }

    /**
     * 
     * 获取配置文件
     * 
     * @return
     */
    @RequestMapping(value = "/file", method = RequestMethod.GET)
    @ResponseBody
    public HttpEntity<byte[]> getFile(ConfForm confForm) {

        boolean hasError = false;

        //
        // 校验
        //
        ConfigModel configModel = null;
        try {
            configModel = configValidator.verifyConfForm(confForm);
        } catch (Exception e) {
            LOG.error(e.toString(), e);
            hasError = true;
        }

        if (hasError != false) {
            try {
                //
                Config config = configMgr.getConfByParameter(
                        configModel.getAppId(), configModel.getEnvId(),
                        configModel.getVersion(), configModel.getKey());
                if (config == null) {
                    hasError = true;
                    throw new DocumentNotFoundException(configModel.getKey());
                }

                return downloadDspBill(configModel.getKey(), config.getValue());

            } catch (Exception e) {
                LOG.error(e.toString(), e);
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

}
