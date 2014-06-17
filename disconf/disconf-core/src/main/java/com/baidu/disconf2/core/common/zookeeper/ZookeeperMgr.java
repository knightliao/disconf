package com.baidu.disconf2.core.common.zookeeper;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf2.core.common.utils.ZooUtils;
import com.baidu.disconf2.core.common.zookeeper.inner.ResilientActiveKeyValueStore;

public class ZookeeperMgr {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(ZookeeperMgr.class);

    private ResilientActiveKeyValueStore store;

    /**
     * 
     * @Description: 初始化
     * 
     * @throws Exception
     * @return void
     * @author liaoqiqi
     * @date 2013-6-14
     */
    public void init(String host, String defaultPrefixString) throws Exception {

        try {

            initInternal(host, defaultPrefixString);

            LOGGER.debug("ZookeeperMgr init.");

        } catch (Exception e) {

            throw new Exception("zookeeper init failed. ", e);
        }
    }

    /**
     * 建立连接
     */
    private ZookeeperMgr() {

    }

    /**
     * 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例 没有绑定关系，而且只有被调用到时才会装载，从而实现了延迟加载。
     */
    private static class SingletonHolder {
        /**
         * 静态初始化器，由JVM来保证线程安全
         */
        private static ZookeeperMgr instance = new ZookeeperMgr();
    }

    public static ZookeeperMgr getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 
     * @Description: 初始化
     * 
     * @throws IOException
     * @throws InterruptedException
     * @return void
     * @author liaoqiqi
     * @date 2013-6-14
     */
    private void initInternal(String hosts, String defaultPrefixString)
            throws IOException, InterruptedException {

        store = new ResilientActiveKeyValueStore();
        store.connect(hosts);

        // 新建父目录
        makeDir(defaultPrefixString, ZooUtils.getIp());
    }

    /**
     * Zoo的新建目录
     * 
     * @param dir
     */
    public void makeDir(String dir, String data) {

        try {

            boolean deafult_path_exist = store.exists(dir);
            if (!deafult_path_exist) {
                LOGGER.info("create: " + dir);
                this.writePersistentUrl(dir, data);
            } else {
                this.writePersistentUrl(dir, data);
            }

        } catch (KeeperException e) {

            LOGGER.error("cannot create path: " + dir, e);

        } catch (Exception e) {

            LOGGER.error("cannot create path: " + dir, e);
        }
    }

    /**
     * 
     * @Description: 应用程序必须调用它来释放zookeeper资源
     * 
     * @return void
     * @author liaoqiqi
     * @date 2013-6-14
     */
    public void release() throws InterruptedException {

        store.close();
    }

    /**
     * 
     * @Description: 获取子孩子 列表
     * 
     * @return
     * @return List<String>
     * @author liaoqiqi
     * @date 2013-6-14
     */
    public List<String> getRootChildren() {

        return store.getRootChildren();
    }

    /**
     * 
     * @Description: 写持久化结点,没有则新建
     * 
     * @return
     * @return List<String>
     * @author liaoqiqi
     * @date 2013-6-14
     */
    public void writePersistentUrl(String url, String value) throws Exception {

        store.write(url, value);
    }

    /**
     * 
     * @Description: 读结点数据
     * 
     * @return
     * @return List<String>
     * @author liaoqiqi
     * @date 2013-6-14
     */
    public String readUrl(String url, Watcher watcher) throws Exception {

        return store.read(url, watcher, null);
    }

    /*
     * 返回zk
     */
    public ZooKeeper getZk() {

        return store.getZk();
    }

    /*
     * 路径是否存在
     */
    public boolean exists(String path) throws Exception {

        return store.exists(path);
    }

    /*
     * 生成一个临时结点
     */
    public String createEphemeralNode(String path, String value,
            CreateMode createMode) throws Exception {

        return store.createEphemeralNode(path, value, createMode);
    }

    /**
     * 
     * @Description: 带状态信息的读取数据
     * 
     * @param path
     * @param watcher
     * @param stat
     * @return
     * @throws InterruptedException
     * @throws KeeperException
     * @return String
     * @author liaoqiqi
     * @date 2013-6-17
     */
    public String read(String path, Watcher watcher, Stat stat)
            throws InterruptedException, KeeperException {

        return store.read(path, watcher, stat);
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

        store.deleteNode(path);
    }
}
