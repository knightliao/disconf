package com.baidu.disconf2.web.config.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.baidu.disconf2.service.config.service.ConfigMgr;
import com.baidu.dsp.common.constant.WebConstants;

/**
 * 
 * 专用于配置读取
 * 
 * @author liaoqiqi
 * @version 2014-6-22
 */
@Controller
@RequestMapping(WebConstants.API_PREFIX + "/config")
public class ConfigReadController {

    protected static final Logger LOG = LoggerFactory
            .getLogger(ConfigReadController.class);

    @Autowired
    private ConfigMgr configMgr;
    
    
    
}
