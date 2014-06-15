package com.baidu.disconf2.client.fetcher;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf2.client.config.inner.DisClientConfig;
import com.baidu.disconf2.client.config.inner.DisClientSysConfig;
import com.baidu.disconf2.client.fetcher.inner.restful.RestfulMgr;
import com.baidu.disconf2.client.fetcher.inner.restful.core.RemoteUrl;
import com.baidu.disconf2.core.common.json.ConfItemVo;

/**
 * 下载模块
 * 
 * @author liaoqiqi
 * @version 2014-6-12
 */
public class FetcherMgr {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(FetcherMgr.class);

    /**
     * 下载配置Key
     * 
     * @param url
     * @return
     */
    public static String getItemFromServer(String url) throws Exception {

        // 远程地址
        RemoteUrl remoteUrl = new RemoteUrl(url, DisClientConfig.getInstance()
                .getHostList());

        ConfItemVo confItemVo = RestfulMgr.getInstance().getJsonData(
                ConfItemVo.class, remoteUrl);
        LOGGER.info("remote server return: " + confItemVo.toString());

        if (confItemVo.getStatus().equals(ConfItemVo.NOTOK)) {
            throw new Exception("remote server return " + confItemVo.toString()
                    + ", status is not ok.");
        }

        return confItemVo.getValue();
    }

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
