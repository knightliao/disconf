package com.baidu.disconf2.client.fetcher;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf2.client.config.inner.DisClientConfig;
import com.baidu.disconf2.client.config.inner.DisClientSysConfig;
import com.baidu.disconf2.client.fetcher.inner.restful.RestfulMgr;
import com.baidu.disconf2.client.fetcher.inner.restful.core.RemoteUrl;

/**
 * 下载模块
 * 
 * @author liaoqiqi
 * @version 2014-6-12
 */
public class FetcherMgr {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(FetcherMgr.class);

    /*
     * 下载 fileName，remoteUrl是 url
     */
    public static String downloadFileFromServer(String url, String fileName)
            throws Exception {

        // 获本地的地址
        String localTmpDir = getLocalDownloadDirPath(true);
        String localDir = getLocalDownloadDirPath(false);

        // 远程地址
        RemoteUrl remoteUrl = new RemoteUrl(url, DisClientConfig.getInstance()
                .getHostList());

        // 下载
        return RestfulMgr
                .getInstance()
                .downloadFromServer(
                        remoteUrl,
                        fileName,
                        localTmpDir,
                        localDir,
                        DisClientSysConfig.getInstance().ENABLE_LOCAL_DOWNLOAD_DIR_IN_CLASS_PATH);
    }

    /**
     * 获取本地下载的临时路径DIR
     * 
     * @param isTmp
     * @return
     * @throws Exception
     */
    private static String getLocalDownloadDirPath(boolean isTmp)
            throws Exception {

        String localUrl = DisClientSysConfig.getInstance().getDownloadTmpDir();

        if (!isTmp) {
            localUrl = DisClientSysConfig.getInstance().LOCAL_DOWNLOAD_DIR;
        }

        if (!new File(localUrl).exists()) {
            new File(localUrl).mkdirs();
        }

        return localUrl;
    }
}
