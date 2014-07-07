package com.baidu.disconf2.core.common.zookeeper.inner;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

public class ResilientActiveKeyValueStore extends ConnectionWatcher {

    private static final Charset CHARSET = Charset.forName("UTF-8");
    private static final int MAX_RETRIES = 5;
    private static final int RETRY_PERIOD_SECONDS = 10;

    /**
     * 
     * @Description: 写PATH数据, 是持久化的
     * 
     * @param path
     * @param value
     * @throws InterruptedException
     * @throws KeeperException
     * @return void
     * @author liaoqiqi
     * @date 2013-6-14
     */
    public void write(String path, String value) throws InterruptedException,
            KeeperException {

        int retries = 0;
        while (true) {

            try {

                Stat stat = zk.exists(path, false);

                if (stat == null) {

                    zk.create(path, value.getBytes(CHARSET),
                            Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

                } else {

                    zk.setData(path, value.getBytes(CHARSET), stat.getVersion());
                }

                break;

            } catch (KeeperException.SessionExpiredException e) {

                throw e;

            } catch (KeeperException e) {

                LOGGER.warn(e.toString());

                if (retries++ == MAX_RETRIES) {
                    throw e;
                }
                // sleep then retry
                TimeUnit.SECONDS.sleep(RETRY_PERIOD_SECONDS);
            }
        }
    }

    /**
     * 
     * @Description: 创建一个临时结点，如果原本存在，则不新建
     * 
     * @param path
     * @param value
     * @throws InterruptedException
     * @throws KeeperException
     * @return void
     * @author liaoqiqi
     * @date 2013-6-14
     */
    public String createEphemeralNode(String path, String value,
            CreateMode createMode) throws InterruptedException, KeeperException {

        int retries = 0;
        while (true) {

            try {

                Stat stat = zk.exists(path, false);

                if (stat == null) {

                    return zk.create(path, value.getBytes(CHARSET),
                            Ids.OPEN_ACL_UNSAFE, createMode);
                }

                return path;

            } catch (KeeperException.SessionExpiredException e) {

                throw e;

            } catch (KeeperException e) {

                LOGGER.warn(e.toString());

                if (retries++ == MAX_RETRIES) {
                    throw e;
                }
                // sleep then retry
                TimeUnit.SECONDS.sleep(RETRY_PERIOD_SECONDS);
            }
        }
    }

    /**
     * 
     * @Description: 判断是否存在
     * 
     * @param path
     * @param value
     * @throws InterruptedException
     * @throws KeeperException
     * @return void
     * @author liaoqiqi
     * @date 2013-6-14
     */
    public boolean exists(String path) throws InterruptedException,
            KeeperException {

        int retries = 0;
        while (true) {

            try {

                Stat stat = zk.exists(path, false);

                if (stat == null) {

                    return false;

                } else {

                    return true;
                }

            } catch (KeeperException.SessionExpiredException e) {

                throw e;

            } catch (KeeperException e) {

                LOGGER.warn(e.toString());

                if (retries++ == MAX_RETRIES) {
                    throw e;
                }
                // sleep then retry
                TimeUnit.SECONDS.sleep(RETRY_PERIOD_SECONDS);
            }
        }
    }

    /**
     * 
     * @Description: 读数据
     * 
     * @param path
     * @param watcher
     * @return
     * @throws InterruptedException
     * @throws KeeperException
     * @return String
     * @author liaoqiqi
     * @date 2013-6-14
     */
    public String read(String path, Watcher watcher, Stat stat)
            throws InterruptedException, KeeperException {

        byte[] data = zk.getData(path, watcher, stat);
        return new String(data, CHARSET);
    }

    /**
     * 
     * @Description: 获取子孩子数据
     * 
     * @return
     * @return List<String>
     * @author liaoqiqi
     * @date 2013-6-14
     */
    public List<String> getRootChildren() {

        List<String> children = new ArrayList<String>();
        try {
            children = zk.getChildren("/", false);
        } catch (KeeperException e) {
            LOGGER.error(e.toString());
        } catch (InterruptedException e) {
            LOGGER.error(e.toString());
        }

        return children;
    }

    /**
     * 
     * @Description: 删除结点
     * 
     * @param path
     * @return void
     * @author liaoqiqi
     * @date 2013-6-17
     */
    public void deleteNode(String path) {

        try {

            zk.delete(path, -1);

        } catch (KeeperException.NoNodeException e) {

            LOGGER.error("cannot delete path: " + path, e);

        } catch (InterruptedException e) {

            LOGGER.warn(e.toString());

        } catch (KeeperException e) {

            LOGGER.error("cannot delete path: " + path, e);
        }
    }

}
