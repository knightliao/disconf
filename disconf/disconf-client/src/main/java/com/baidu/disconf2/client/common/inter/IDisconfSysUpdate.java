package com.baidu.disconf2.client.common.inter;

import com.baidu.disconf2.core.common.constants.DisConfigTypeEnum;

/**
 * 配置更新 时系统来调用
 * 
 * @author liaoqiqi
 * @version 2014-5-20
 */
public interface IDisconfSysUpdate {

    /**
     * 
     * @param disConfigTypeEnum
     * @param keyName
     * @throws Exception
     */
    public void reload(DisConfigTypeEnum disConfigTypeEnum, String keyName)
            throws Exception;
}
