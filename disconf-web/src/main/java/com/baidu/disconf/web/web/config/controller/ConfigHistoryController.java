package com.baidu.disconf.web.web.config.controller;

import com.baidu.disconf.web.service.config.form.ConfHistoryListForm;
import com.baidu.disconf.web.service.config.service.ConfigHistoryMgr;
import com.baidu.disconf.web.service.config.vo.ConfHistoryListVo;
import com.baidu.dsp.common.constant.WebConstants;
import com.baidu.dsp.common.controller.BaseController;
import com.baidu.dsp.common.vo.JsonObjectBase;
import com.baidu.ub.common.db.DaoPageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

/**
 * 历史配置列表
 *
 * @author Chen Hui
 */
@Controller
@RequestMapping(WebConstants.API_PREFIX + "/configHistory")
public class ConfigHistoryController extends BaseController {

    protected static final Logger LOG = LoggerFactory.getLogger(ConfigHistoryController.class);

    @Autowired
    private ConfigHistoryMgr configHistoryMgr;


    /**
     * 获取列表,有分页的
     *
     * @param confHistoryListForm
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public JsonObjectBase getConfigList(@Valid ConfHistoryListForm confHistoryListForm) {

        LOG.info(confHistoryListForm.toString());

        DaoPageResult<ConfHistoryListVo> configs = configHistoryMgr.getConfigHistoryList(confHistoryListForm);

        return buildListSuccess(configs);
    }
    /**
     * 获取历史配置值
     *
     * @param id
     *
     * @return
     */
    @RequestMapping(value = "/value", method = RequestMethod.GET)
    @ResponseBody
    public JsonObjectBase getConfig(@RequestParam long id,@RequestParam int type) {

        return buildSuccess(configHistoryMgr.getConfigHistoryValue(id,type));
    }
}
