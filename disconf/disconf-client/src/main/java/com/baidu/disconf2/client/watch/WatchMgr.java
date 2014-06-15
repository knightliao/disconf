package com.baidu.disconf2.client.watch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf2.client.config.inner.DisClientSysConfig;
import com.baidu.disconf2.client.fetcher.FetcherMgr;
import com.baidu.disconf2.core.common.path.PathMgr;
import com.baidu.disconf2.core.common.utils.ZooUtils;
import com.baidu.disconf2.core.common.zookeeper.ZookeeperMgr;

/**
 * Watch ZOO 模块, 依赖 FetcherMgr模块, 必须先初始化FetcherMgr模块
 * 
 * @author liaoqiqi
 * @version 2014-6-10
 */
public class WatchMgr {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(WatchMgr.class);

    /**
     * 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例 没有绑定关系，而且只有被调用到时才会装载，从而实现了延迟加载。
     */
    private static class SingletonHolder {
        /**
         * 静态初始化器，由JVM来保证线程安全
         */
        private static WatchMgr instance = new WatchMgr();
    }

    public static WatchMgr getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 父目录的路径
     */
    private String zkParentDirPath = "";

    /**
     * Zoo hosts
     */
    private String hosts = "";

    /**
     * 
     * @Description: 获取自己的主备类型
     * 
     * @return void
     * @author liaoqiqi
     * @date 2013-6-16
     */
    public void init() throws Exception {

        if (!FetcherMgr.isInit()) {
            throw new Exception("FetcherMgr should be init before WatchMgr");
        }

        // Zoo 地址
        hosts = FetcherMgr
                .getValueFromServer(PathMgr.getZooHostsUrl(DisClientSysConfig
                        .getInstance().CONF_SERVER_ZOO_ACTION));

        //
        this.zkParentDirPath = DisClientSysConfig.getInstance().ZOOKEEPER_URL_PREFIX;

        //
        // init zookeeper
        //
        LOGGER.info("init zookeeper......");
        ZookeeperMgr.getInstance().init(hosts, zkParentDirPath);

        // 新建zookeeper的目录
        ZookeeperMgr.getInstance().writePersistentUrl(zkParentDirPath,
                ZooUtils.getZooDirValue());
        LOGGER.info("make url: " + zkParentDirPath + " successful!");
    }

}
