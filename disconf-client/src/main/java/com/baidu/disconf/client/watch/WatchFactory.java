package com.baidu.disconf.client.watch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf.client.config.ConfigMgr;
import com.baidu.disconf.client.config.DisClientConfig;
import com.baidu.disconf.client.config.DisClientSysConfig;
import com.baidu.disconf.client.fetcher.FetcherMgr;
import com.baidu.disconf.client.watch.impl.WatchMgrImpl;
import com.baidu.disconf.core.common.path.DisconfWebPathMgr;

/**
 * 监控器 实例 工厂
 *
 * @author liaoqiqi
 * @version 2014-7-29
 */
public class WatchFactory {

    protected static final Logger LOGGER = LoggerFactory.getLogger(WatchFactory.class);

    private static String hosts = null;
    private static String zooPrefix = null;
    private static final Object hostsSync = new Object();

    /**
     * @throws Exception
     */
    public static WatchMgr getWatchMgr(FetcherMgr fetcherMgr) throws Exception {

        if (!ConfigMgr.isInit()) {
            throw new Exception("ConfigMgr should be init before WatchFactory.getWatchMgr");
        }

        if (hosts == null || zooPrefix == null) {
            synchronized(hostsSync) {
                if (hosts == null || zooPrefix == null) {

                    // 获取 Zoo Hosts
                    try {

                        hosts = fetcherMgr.getValueFromServer(DisconfWebPathMgr.getZooHostsUrl(DisClientSysConfig
                                                                                                   .getInstance()
                                                                                                   .CONF_SERVER_ZOO_ACTION));

                        zooPrefix = fetcherMgr.getValueFromServer(DisconfWebPathMgr.getZooPrefixUrl(DisClientSysConfig
                                                                                                        .getInstance
                                                                                                             ()
                                                                                                        .CONF_SERVER_ZOO_ACTION));

                        WatchMgr watchMgr = new WatchMgrImpl();
                        watchMgr.init(hosts, zooPrefix, DisClientConfig.getInstance().DEBUG);

                        return watchMgr;

                    } catch (Exception e) {

                        LOGGER.error("cannot get watch module", e);

                    }
                }
            }
        }

        return null;
    }
}
