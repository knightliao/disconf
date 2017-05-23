package com.baidu.disconf.core.common.zookeeper;

import com.baidu.disconf.core.common.utils.ZooUtils;
import com.baidu.disconf.core.common.zookeeper.inner.ResilientActiveKeyValueStore;
import com.baidu.disconf.core.common.zookeeper.inner.ZKHost;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * ZK统一管理器
 *
 * @author liaoqiqi
 * @version 2014-7-7
 */
public class ZookeeperMgr {

    protected static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperMgr.class);

    private ResilientActiveKeyValueStore store;

    private String curHost = "";
    private String zkHosts = "";
    private String curDefaultPrefixString = "";

    /**
     * @return void
     *
     * @throws Exception
     * @Description: 初始化
     * @author liaoqiqi
     * @date 2013-6-14
     */
    public void init(String host, String defaultPrefixString, boolean debug) throws Exception {

        try {

            initInternal(host, defaultPrefixString, debug);

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
     * 重新连接
     */
    public void reconnect() {
        store.reconnect();
    }

    /**
     * @return void
     *
     * @throws IOException
     * @throws InterruptedException
     * @Description: 初始化
     * @author liaoqiqi
     * @date 2013-6-14
     */
    private void initInternal(String hosts, String defaultPrefixString, boolean debug) throws IOException, InterruptedException {

        zkHosts = hosts;
        curHost = hosts;
        curDefaultPrefixString = defaultPrefixString;

        store = new ResilientActiveKeyValueStore(debug);
        store.connect(hosts);

        LOGGER.info("zoo prefix: " + defaultPrefixString);

        // 新建父目录
        //makeDir(defaultPrefixString, ZooUtils.getIp());
        makeDir(defaultPrefixString, ZooUtils.getNonLocalIp(null));
    }

    /**
     * Zoo的新建目录
     *
     * @param dir
     */
    public void makeDir(String dir, String data) {

        try {

            boolean deafult_path_exist = store.exists(dir);
            if (! deafult_path_exist) {
                LOGGER.info("create: " + dir);
                this.writePersistentUrl(dir, data);
            } else {
            }

        } catch (KeeperException e) {

            LOGGER.error("cannot create path: " + dir, e);

        } catch (Exception e) {

            LOGGER.error("cannot create path: " + dir, e);
        }
    }

    /**
     * @return void
     *
     * @Description: 应用程序必须调用它来释放zookeeper资源
     * @author liaoqiqi
     * @date 2013-6-14
     */
    public void release() throws InterruptedException {

        store.close();
    }

    /**
     * @return List<String>
     *
     * @Description: 获取子孩子 列表
     * @author liaoqiqi
     * @date 2013-6-14
     */
    public List<String> getRootChildren() {

        return store.getRootChildren();
    }

    /**
     * @return List<String>
     *
     * @Description: 写持久化结点, 没有则新建, 存在则进行更新
     * @author liaoqiqi
     * @date 2013-6-14
     */
    public void writePersistentUrl(String url, String value) throws Exception {

        store.write(url, value);
    }

    /**
     * @return List<String>
     *
     * @Description: 读结点数据
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
    public String createEphemeralNode(String path, String value, CreateMode createMode) throws Exception {

        return store.createEphemeralNode(path, value, createMode);
    }

    /**
     * @param path
     * @param watcher
     * @param stat
     *
     * @return String
     *
     * @throws InterruptedException
     * @throws KeeperException
     * @Description: 带状态信息的读取数据
     * @author liaoqiqi
     * @date 2013-6-17
     */
    public String read(String path, Watcher watcher, Stat stat) throws InterruptedException, KeeperException {

        return store.read(path, watcher, stat);
    }

    /**
     * @param path
     *
     * @return void
     *
     * @Description: 删除结点
     * @author liaoqiqi
     * @date 2013-6-17
     */
    public void deleteNode(String path) {

        store.deleteNode(path);
    }

    public String getZkHosts() {
        return zkHosts;
    }

    /** 获取zkhosts的列表 */
    public List<ZKHost> getZKHostsList() {
        String hosts = getZkHosts();
        if (Strings.isNullOrEmpty(getZkHosts())) {
            return Lists.newArrayList();
        }
        List<ZKHost> zkHosts = Lists.newArrayList();
        String hostsList[] = hosts.split(",");
        for (String host : hostsList) {
            int port = 2181;
            int pidx = host.lastIndexOf(':');
            if (pidx >= 0) {
                // otherwise : is at the end of the string, ignore
                if (pidx < host.length() - 1) {
                    port = Integer.parseInt(host.substring(pidx + 1));
                }
                host = host.substring(0, pidx);
            }
            zkHosts.add(new ZKHost(host, port));
        }
        return zkHosts;
    }
}
