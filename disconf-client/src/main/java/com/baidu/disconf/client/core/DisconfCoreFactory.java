package com.baidu.disconf.client.core;

import com.baidu.disconf.client.core.impl.DisconfCoreMgrImpl;
import com.baidu.disconf.client.fetcher.FetcherFactory;
import com.baidu.disconf.client.fetcher.FetcherMgr;
import com.baidu.disconf.client.watch.WatchFactory;
import com.baidu.disconf.client.watch.WatchMgr;

/**
 * Core模块的工厂类
 *
 * @author liaoqiqi
 * @version 2014-7-29
 */
public class DisconfCoreFactory {

    /**
     * @return
     *
     * @throws Exception
     */
    public static DisconfCoreMgr getDisconfCoreMgr() throws Exception {

        FetcherMgr fetcherMgr = FetcherFactory.getFetcherMgr();

        // Watch 模块
        WatchMgr watchMgr = WatchFactory.getWatchMgr(fetcherMgr);

        DisconfCoreMgr disconfCoreMgr = new DisconfCoreMgrImpl(watchMgr, fetcherMgr);

        return disconfCoreMgr;
    }
}
