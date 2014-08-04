package com.baidu.disconf.client.watch;

import com.baidu.disconf.client.config.ConfigMgr;
import com.baidu.disconf.client.config.DisClientSysConfig;
import com.baidu.disconf.client.fetcher.FetcherMgr;
import com.baidu.disconf.client.watch.impl.WatchMgrImpl;
import com.baidu.disconf.core.common.path.DisconfWebPathMgr;

/**
 * 
 * 监控器 实例 工厂
 * 
 * @author liaoqiqi
 * @version 2014-7-29
 */
public class WatchFactory {

    private static String hosts = null;
    private static volatile Object hostsSync = new Object();

    /**
     * 
     * @param fetcherMgr
     * @return
     * @throws Exception
     */
    public static WatchMgr getWatchMgr(FetcherMgr fetcherMgr) throws Exception {

        if (!ConfigMgr.isInit()) {
            throw new Exception(
                    "ConfigMgr should be init before WatchFactory.getWatchMgr");
        }

        if (hosts == null) {
            synchronized (hostsSync) {
                if (hosts == null) {

                    // 获取 Zoo Hosts
                    try {
                        hosts = fetcherMgr.getValueFromServer(DisconfWebPathMgr
                                .getZooHostsUrl(DisClientSysConfig
                                        .getInstance().CONF_SERVER_ZOO_ACTION));
                    } catch (Exception e) {
                        throw new Exception("cannot get zoo hosts", e);
                    }
                }
            }
        }

        WatchMgr watchMgr = new WatchMgrImpl();
        watchMgr.init(hosts,
                DisClientSysConfig.getInstance().ZOOKEEPER_URL_PREFIX);

        return watchMgr;
    }
}
