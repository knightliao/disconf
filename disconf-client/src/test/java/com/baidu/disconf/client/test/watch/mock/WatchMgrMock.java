package com.baidu.disconf.client.test.watch.mock;

import com.baidu.disconf.client.common.model.DisConfCommonModel;
import com.baidu.disconf.client.core.processor.DisconfCoreProcessor;
import com.baidu.disconf.client.watch.WatchMgr;
import com.baidu.disconf.core.common.constants.DisConfigTypeEnum;

import mockit.Mock;
import mockit.MockUp;

/**
 * Watch MOckup
 *
 * @author liaoqiqi
 * @version 2014-7-31
 */
public class WatchMgrMock extends MockUp<WatchMgr> {

    @Mock
    public void init(String hosts, String zooUrlPrefix, boolean debug) throws Exception {
        return;
    }

    /**
     * 监控路径,监控前会事先创建路径,并且会新建一个自己的Temp子结点
     */
    @Mock
    public void watchPath(DisconfCoreProcessor disconfCoreMgr, DisConfCommonModel disConfCommonModel, String keyName,
                          DisConfigTypeEnum disConfigTypeEnum, String value) throws Exception {

        return;
    }

    @Mock
    public void release() {
        return;
    }
}
