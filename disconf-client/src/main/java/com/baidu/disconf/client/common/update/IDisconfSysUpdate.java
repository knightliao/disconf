package com.baidu.disconf.client.common.update;

import com.baidu.disconf.client.core.processor.DisconfCoreProcessor;
import com.baidu.disconf.core.common.constants.DisConfigTypeEnum;

/**
 * 当发生 配置更新 时 此回调会首先被调用
 *
 * @author liaoqiqi
 * @version 2014-5-20
 */
public interface IDisconfSysUpdate {

    /**
     * @param disconfCoreMgr    处理算子
     * @param disConfigTypeEnum 配置类型
     * @param keyName           配置KEY
     *
     * @throws Exception
     */
    void reload(DisconfCoreProcessor disconfCoreMgr, DisConfigTypeEnum disConfigTypeEnum, String keyName)
            throws Exception;
}
