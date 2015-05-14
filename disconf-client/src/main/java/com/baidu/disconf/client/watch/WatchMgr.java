package com.baidu.disconf.client.watch;

import com.baidu.disconf.client.common.model.DisConfCommonModel;
import com.baidu.disconf.client.core.processor.DisconfCoreProcessor;
import com.baidu.disconf.core.common.constants.DisConfigTypeEnum;

/**
 * 监控的接口
 *
 * @author liaoqiqi
 * @version 2014-7-29
 */
public interface WatchMgr {

    /**
     * 初始化
     *
     * @throws Exception
     */
    void init(String hosts, String zooUrlPrefix, boolean debug) throws Exception;

    /**
     * 监控路径,监控前会事先创建路径,并且会新建一个自己的Temp子结点
     */
    void watchPath(DisconfCoreProcessor disconfCoreMgr, DisConfCommonModel disConfCommonModel, String keyName,
                   DisConfigTypeEnum disConfigTypeEnum, String value) throws Exception;

    void release();
}
