package com.baidu.disconf.core.test.zookeeper.mock;

import java.util.List;
import java.util.Map;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;

import com.baidu.disconf.core.common.zookeeper.inner.ResilientActiveKeyValueStore;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Store的Mock
 *
 * @author liaoqiqi
 * @version 2014-7-30
 */
public class ResilientActiveKeyValueStoreMock extends ResilientActiveKeyValueStore {

    // 数据结构
    Map<String, String> map = Maps.newHashMap();

    /**
     * @param
     */
    public ResilientActiveKeyValueStoreMock() {
        super(true);
    }

    @Override
    public void write(String path, String value) throws InterruptedException, KeeperException {

        map.put(path, value);

        return;
    }

    @Override
    public String createEphemeralNode(String path, String value, CreateMode createMode)
        throws InterruptedException, KeeperException {

        map.put(path, value);

        return path;
    }

    @Override
    public boolean exists(String path) throws InterruptedException, KeeperException {

        return map.containsKey(path);
    }

    @Override
    public List<String> getRootChildren() {

        return Lists.newArrayList("a", "b", "c");
    }

    @Override
    public void deleteNode(String path) {

        map.remove(path);

        return;
    }

    @Override
    public String read(String path, Watcher watcher, Stat stat) throws InterruptedException, KeeperException {

        return map.get(path);
    }
}
