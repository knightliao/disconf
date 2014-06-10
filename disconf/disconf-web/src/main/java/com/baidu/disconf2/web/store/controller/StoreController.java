package com.baidu.disconf2.web.store.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baidu.dsp.common.constant.WebConstants;
import com.baidu.dsp.common.controller.BaseController;
import com.baidu.dsp.common.vo.JsonObjectBase;

/**
 * 
 * @author liaoqiqi
 * @version 2014-1-20
 */
@Controller
@RequestMapping(WebConstants.API_PREFIX + "/store")
public class StoreController extends BaseController {

    protected static final Logger LOG = LoggerFactory
            .getLogger(StoreController.class);

    /**
     * GET 获取
     * 
     * @param demoUserId
     * @return
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public JsonObjectBase get() {

        int aaa = 10;

        return buildSuccess(aaa);
    }

    /**
     * 更新Conf值
     * 
     * @param demoUserId
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.GET)
    @ResponseBody
    public JsonObjectBase updateValue() {

        return buildSuccess("");
    }

}