package com.baidu.disconf.web.service.config.service;

import java.io.File;
import java.util.List;

import com.baidu.disconf.core.common.constants.DisConfigTypeEnum;
import com.baidu.disconf.web.service.config.bo.Config;
import com.baidu.disconf.web.service.config.form.ConfListForm;
import com.baidu.disconf.web.service.config.form.ConfNewItemForm;
import com.baidu.disconf.web.service.config.vo.ConfListVo;
import com.baidu.disconf.web.service.config.vo.MachineListVo;
import com.baidu.ub.common.db.DaoPageResult;

/**
 * @author liaoqiqi
 * @version 2014-6-16
 */
public interface ConfigMgr {

    /**
     * @param
     *
     * @return
     */
    List<String> getVersionListByAppEnv(Long appId, Long envId);

    /**
     * @return
     */
    DaoPageResult<ConfListVo> getConfigList(ConfListForm confListForm, boolean fetchZk, final boolean getErrorMessage);

    /**
     * @param configId
     *
     * @return
     */
    ConfListVo getConfVo(Long configId);

    /**
     * @param configId
     *
     * @return
     */
    MachineListVo getConfVoWithZk(Long configId);

    /**
     * @param configId
     *
     * @return
     */
    Config getConfigById(Long configId);

    /**
     * 更新 配置项/配置文件
     *
     * @param configId
     *
     * @return
     */
    String updateItemValue(Long configId, String value);

    /**
     * 获取config value
     *
     * @param configId
     *
     * @return
     */
    String getValue(Long configId);

    /**
     * 通知zk
     *
     * @param configId
     */
    void notifyZookeeper(Long configId);

    /**
     * 新建一个config
     *
     * @param confNewForm
     * @param disConfigTypeEnum
     */
    void newConfig(ConfNewItemForm confNewForm, DisConfigTypeEnum disConfigTypeEnum);

    /**
     * 删除一个config
     *
     * @param configId
     */
    void delete(Long configId);

    /**
     * @param confListForm
     *
     * @return
     */
    List<File> getDisconfFileList(ConfListForm confListForm);

}
