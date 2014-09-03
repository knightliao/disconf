/**
 * Project  : disconf-web
 * Package  : com.baidu.disconf.web.web.host.controller
 * FileName : HostController.java
 * Copyright (c) 2014, Baidu Inc. All rights reserved.
 */
package com.baidu.disconf.web.web.usage.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baidu.disconf.web.service.config.service.ConfigMgr;
import com.baidu.dsp.common.constant.WebConstants;
import com.baidu.dsp.common.controller.BaseController;
import com.baidu.dsp.common.vo.JsonObjectBase;


/**
 * @description : UsageController
 * 查询配置信息被Host使用的情况
 * 
 * @author      : WuNing
 * @email       : Wuning01@baidu.com
 * @date        : 2014年8月5日 下午1:39:28
 */
@Controller
@RequestMapping(WebConstants.API_PREFIX + "/usage")
public class UsageController extends BaseController {

    protected static final Logger LOG = LoggerFactory
            .getLogger(UsageController.class);
    
    @Autowired
    ConfigMgr configMgr;
    
    /**
     * 返回正在使用当前配置的服务器的列表
     * 注：当前版本没有提供分页功能，所有服务器列表将一起返回
     * 
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public JsonObjectBase list() {
        
        String hostInfoStr = configMgr.getUsageInfo();
        
        return buildSuccess("hostInfo",hostInfoStr);
    }
}
