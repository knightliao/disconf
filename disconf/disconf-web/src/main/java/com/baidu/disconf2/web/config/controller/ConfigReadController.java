package com.baidu.disconf2.web.config.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baidu.disconf2.service.config.form.ConfListForm;
import com.baidu.disconf2.service.config.form.VersionListForm;
import com.baidu.disconf2.service.config.service.ConfigMgr;
import com.baidu.disconf2.service.config.vo.ConfListVo;
import com.baidu.disconf2.web.config.validator.ConfigValidator;
import com.baidu.dsp.common.constant.WebConstants;
import com.baidu.dsp.common.controller.BaseController;
import com.baidu.dsp.common.vo.JsonObjectBase;
import com.baidu.ub.common.generic.vo.DaoPageResult;

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

    @Autowired
    private ConfigValidator configValidator;

    /**
     * 获取版本的List
     * 
     * @return
     */
    @RequestMapping(value = "/versionlist", method = RequestMethod.GET)
    @ResponseBody
    public JsonObjectBase getVersionList(@Valid VersionListForm versionListForm) {

        List<String> versionList = configMgr
                .getVersionListByAppId(versionListForm.getAppId());

        return buildListSuccess(versionList, versionList.size());
    }

    /**
     * 
     * @param confListForm
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public JsonObjectBase getConfigList(ConfListForm confListForm) {

        DaoPageResult<ConfListVo> configs = configMgr
                .getConfigList(confListForm);

        return buildListSuccess(configs);
    }

    /**
     * 
     * @param confListForm
     * @return
     */
    @RequestMapping(value = "/{configId}", method = RequestMethod.GET)
    @ResponseBody
    public JsonObjectBase getConfigList(@PathVariable long configId) {

        // 业务校验
        configValidator.valideConfigExist(configId);

        ConfListVo config = configMgr.getConfVo(configId);

        return buildSuccess(config);
    }
}
