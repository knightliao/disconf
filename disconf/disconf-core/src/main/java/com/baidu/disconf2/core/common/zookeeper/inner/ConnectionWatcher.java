package com.baidu.disconf2.core.common.zookeeper.inner;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 连接管理
 * 
 * @author liaoqiqi
 * @email liaoqiqi@baidu.com
 * 
 */
public class ConnectionWatcher implements Watcher {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(ConnectionWatcher.class);

    private static final int SESSION_TIMEOUT = 5000;

    protected ZooKeeper zk;
    private CountDownLatch connectedSignal = new CountDownLatch(1);

    /**
     * 
     * @Description: 连接ZK
     * 
     * @param hosts
     * @throws IOException
     * @throws InterruptedException
     * @return void
     * @author liaoqiqi
     * @date 2013-6-14
     */
    public void connect(String hosts) throws IOException, InterruptedException {
        zk = new ZooKeeper(hosts, SESSION_TIMEOUT, this);
        connectedSignal.await();
        LOGGER.info("zookeeper: " + hosts + " , connected.");
    }

    /**
     * 当连接成功时调用的
     */
    @Override
    public void process(WatchedEvent event) {
        if (event.getState() == KeeperState.SyncConnected) {
            connectedSignal.countDown();
        }
    }

    /**
     * 
     * @Description: 关闭
     * 
     * @throws InterruptedException
     * @return void
     * @author liaoqiqi
     * @date 2013-6-14
     */
    public void close() throws InterruptedException {
        zk.close();
    }

    public ZooKeeper getZk() {
        return zk;
    }

    public void setZk(ZooKeeper zk) {
        this.zk = zk;
    }
}
