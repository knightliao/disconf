package com.baidu.disconf.web.service.config.service;

import java.io.File;
import java.util.List;

import com.baidu.disconf.core.common.constants.DisConfigTypeEnum;
import com.baidu.disconf.web.service.config.bo.Config;
import com.baidu.disconf.web.service.config.form.ConfForm;
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
     * @param configId
     *
     * @return
     */
    String getValue(Long configId);

    void notifyZookeeper(Long configId);

    /**
     * @param confNewForm
     * @param disConfigTypeEnum
     */
    void newConfig(ConfNewItemForm confNewForm, DisConfigTypeEnum disConfigTypeEnum);

    void delete(Long configId);

    /**
     * @param confListForm
     *
     * @return
     */
    List<File> getDisconfFileList(ConfListForm confListForm);
    
    /**
     * 
     * 获取配置列表
     * @author 周宁
     * @date 2016年12月8日
     * @param confForm
     * @return List<Config>
     * @throws
     */
    List<Config> getConfigList(ConfForm confForm);

}
