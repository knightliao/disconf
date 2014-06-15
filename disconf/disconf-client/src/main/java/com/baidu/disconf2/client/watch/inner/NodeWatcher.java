package com.baidu.disconf2.client.watch.inner;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf2.core.common.zookeeper.ZookeeperMgr;

/**
 * 结点监控器
 * 
 * @author liaoqiqi
 * @version 2014-6-16
 */
public class NodeWatcher implements Watcher {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(NodeWatcher.class);

    private String monitorPath = "";

    /**
     * 
     * @param monitorPath
     */
    public void monitorMaster(String monitorPath) {

        this.monitorPath = monitorPath;

        Stat stat = new Stat();
        try {
            ZookeeperMgr.getInstance().read(monitorPath, this, stat);
        } catch (InterruptedException e) {

        } catch (KeeperException e) {
            LOGGER.error("cannot monitor " + monitorPath, e);
        }

        LOGGER.info("monitor path: " + monitorPath + " has been added!");
    }

    @Override
    public void process(WatchedEvent event) {

        if (event.getType() == EventType.NodeDataChanged) {

            try {

                monitorMaster(monitorPath);

            } catch (Exception e) {

                LOGGER.error("monitor node exception. " + monitorPath, e);
            }
        }
    }
}
