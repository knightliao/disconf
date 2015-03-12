package com.baidu.disconf.core.common.zookeeper.inner;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooKeeper.States;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 连接管理
 *
 * @author liaoqiqi
 */
public class ConnectionWatcher implements Watcher {

    protected static final Logger LOGGER = LoggerFactory.getLogger(ConnectionWatcher.class);

    // 10 秒会话时间 ，避免频繁的session expired
    private static final int SESSION_TIMEOUT = 10000;

    // 3秒
    private static final int CONNECT_TIMEOUT = 3000;

    protected ZooKeeper zk;
    private CountDownLatch connectedSignal = new CountDownLatch(1);

    private static String internalHost = "";

    // 是否调试状态
    private boolean debug = false;

    /**
     * @param debug
     */
    public ConnectionWatcher(boolean debug) {
        this.debug = debug;
    }

    /**
     * @param hosts
     *
     * @return void
     *
     * @throws IOException
     * @throws InterruptedException
     * @Description: 连接ZK
     * @author liaoqiqi
     * @date 2013-6-14
     */
    public void connect(String hosts) throws IOException, InterruptedException {
        internalHost = hosts;
        zk = new ZooKeeper(internalHost, SESSION_TIMEOUT, this);

        // 连接有超时哦
        connectedSignal.await(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);

        LOGGER.info("zookeeper: " + hosts + " , connected.");
    }

    /**
     * 当连接成功时调用的
     */
    @Override
    public void process(WatchedEvent event) {
        if (event.getState() == KeeperState.SyncConnected) {

            LOGGER.info("zk SyncConnected");
            connectedSignal.countDown();

        } else if (event.getState().equals(KeeperState.Disconnected)) {

            // 这时收到断开连接的消息，这里其实无能为力，因为这时已经和ZK断开连接了，只能等ZK再次开启了
            LOGGER.warn("zk Disconnected");

        } else if (event.getState().equals(KeeperState.Expired)) {

            if (!debug) {

                // 这时收到这个信息，表示，ZK已经重新连接上了，但是会话丢失了，这时需要重新建立会话。
                LOGGER.error("zk Expired");

                // just reconnect forever
                reconnect();
            } else {
                LOGGER.info("zk Expired");
            }

        } else if (event.getState().equals(KeeperState.AuthFailed)) {

            LOGGER.error("zk AuthFailed");
        }
    }

    /**
     * 含有重试机制的retry，加锁, 一直尝试连接，直至成功
     */
    public synchronized void reconnect() {

        LOGGER.info("start to reconnect....");

        int retries = 0;
        while (true) {

            try {

                if (!zk.getState().equals(States.CLOSED)) {
                    break;
                }

                LOGGER.warn("zookeeper lost connection, reconnect");

                close();

                connect(internalHost);

            } catch (Exception e) {

                LOGGER.error(retries + "\t" + e.toString());

                // sleep then retry
                try {
                    int sec = ResilientActiveKeyValueStore.RETRY_PERIOD_SECONDS;
                    LOGGER.warn("sleep " + sec);
                    TimeUnit.SECONDS.sleep(sec);
                } catch (InterruptedException e1) {
                }
            }
        }
    }

    /**
     * @return void
     *
     * @throws InterruptedException
     * @Description: 关闭
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
