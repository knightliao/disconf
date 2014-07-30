package com.baidu.disconf.client.fetcher;

import com.baidu.disconf.client.config.ConfigMgr;
import com.baidu.disconf.client.config.DisClientConfig;
import com.baidu.disconf.client.config.DisClientSysConfig;
import com.baidu.disconf.client.fetcher.impl.FetcherMgrImpl;
import com.baidu.disconf.core.common.restful.RestfulMgr;
import com.baidu.disconf.core.common.restful.impl.RestfulMgrImpl;

/**
 * 
 * 抓取器工厂
 * 
 * @author liaoqiqi
 * @version 2014-7-29
 */
public class FetcherFactory {

    /**
     * 获取抓取器实例，记得释放资源, 它依赖Conf模块
     * 
     * @return
     */
    public static FetcherMgr getFetcherMgr() throws Exception {

        if (!ConfigMgr.isInit()) {
            throw new Exception(
                    "ConfigMgr should be init before FetcherFactory.getFetcherMgr");
        }

        RestfulMgr restfulMgr = new RestfulMgrImpl();
        restfulMgr.init();

        FetcherMgr fetcherMgr = new FetcherMgrImpl(
                restfulMgr,
                DisClientSysConfig.getInstance().CONF_SERVER_URL_RETRY_TIMES,
                DisClientSysConfig.getInstance().CONF_SERVER_URL_RETRY_SLEEP_SECONDS,
                DisClientSysConfig.getInstance().ENABLE_LOCAL_DOWNLOAD_DIR_IN_CLASS_PATH,
                DisClientSysConfig.getInstance().LOCAL_DOWNLOAD_DIR,
                DisClientConfig.getInstance().getHostList());

        return fetcherMgr;
    }
}
