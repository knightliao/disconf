package com.baidu.disconf.web.innerapi.zookeeper.impl;

import java.util.List;
import java.util.Map;

import com.baidu.disconf.core.common.constants.DisConfigTypeEnum;
import com.baidu.disconf.web.innerapi.zookeeper.ZooKeeperDriver;
import com.baidu.disconf.web.service.zookeeper.dto.ZkDisconfData;

/**
 * Created by knightliao on 15/1/14.
 */
public class ZookeeperDriverMock implements ZooKeeperDriver {

    @Override
    public void notifyNodeUpdate(String app, String env, String version, String key, String value,
                                 DisConfigTypeEnum disConfigTypeEnum) {

    }

    @Override
    public Map<String, ZkDisconfData> getDisconfData(String app, String env, String version) {
        return null;
    }

    @Override
    public ZkDisconfData getDisconfData(String app, String env, String version, DisConfigTypeEnum disConfigTypeEnum,
                                        String keyName) {
        return null;
    }

    @Override
    public List<String> getConf(String groupName) {
        return null;
    }

    @Override
    public void destroy() throws Exception {

    }
}
