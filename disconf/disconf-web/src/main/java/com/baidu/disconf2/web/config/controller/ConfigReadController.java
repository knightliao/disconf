package com.baidu.disconf2.web.config.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baidu.disconf2.service.config.form.VersionListForm;
import com.baidu.disconf2.service.config.service.ConfigMgr;
import com.baidu.dsp.common.constant.WebConstants;
import com.baidu.dsp.common.controller.BaseController;
import com.baidu.dsp.common.vo.JsonObjectBase;

/**
 * 
 * 专用于配置读取
 * 
 * @author liaoqiqi
 * @version 2014-6-22
 */
@Controller
@RequestMapping(WebConstants.API_PREFIX + "/config")
public class ConfigReadController extends BaseController {

    protected static final Logger LOG = LoggerFactory
            .getLogger(ConfigReadController.class);

    @Autowired
    private ConfigMgr configMgr;

    /**
     * 获取版本的List
     * 
     * @return
     */
    @RequestMapping(value = "/versionlist", method = RequestMethod.GET)
    @ResponseBody
    public JsonObjectBase getVersionList(@Valid VersionListForm versionListForm) {

        List<String> versionList = configMgr.getConfByAppId(versionListForm
                .getAppId());

        return buildListSuccess(versionList, versionList.size());
    }
}
