package com.baidu.disconf.client.fetcher.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf.client.fetcher.FetcherMgr;
import com.baidu.disconf.core.common.constants.Constants;
import com.baidu.disconf.core.common.json.ValueVo;
import com.baidu.disconf.core.common.restful.RestfulMgr;
import com.baidu.disconf.core.common.restful.core.RemoteUrl;
import com.baidu.disconf.core.common.utils.OsUtil;

/**
 * 抓取器, 独立模块，不依赖外部模块, 由Factory来管理此实例
 *
 * @author liaoqiqi
 * @version 2014-7-29
 */
public class FetcherMgrImpl implements FetcherMgr {

    protected static final Logger LOGGER = LoggerFactory.getLogger(FetcherMgrImpl.class);

    // 获取远程配置 重试次数
    private int retryTime = 3;

    // 获取远程配置 重试时休眠时间
    private int retrySleepSeconds = 5;

    // 下载的文件会被迁移到classpath根路径下
    private boolean enableLocalDownloadDirInClassPath = true;

    // 下载文件夹, 远程文件下载后会放在这里
    private String localDownloadDir;

    // temp 临时目录
    private String localDownloadDirTemp;

    //
    private List<String> hostList = new ArrayList<String>();

    // result tool
    private RestfulMgr restfulMgr = null;

    //
    // 创建对象
    //
    public FetcherMgrImpl(RestfulMgr restfulMgr, int retryTime, int retrySleepSeconds,
                          boolean enableLocalDownloadDirInClassPath, String localDownloadDir, String
                                  localDownloadDirTemp, List<String>
                                  hostList) {

        this.restfulMgr = restfulMgr;

        this.retrySleepSeconds = retrySleepSeconds;
        this.retryTime = retryTime;
        this.enableLocalDownloadDirInClassPath = enableLocalDownloadDirInClassPath;
        this.localDownloadDir = localDownloadDir;
        this.localDownloadDirTemp = localDownloadDirTemp;
        OsUtil.makeDirs(this.localDownloadDir);

        this.hostList = hostList;
    }

    /**
     * 根据 URL 从远程 获取Value值
     */
    public String getValueFromServer(String url) throws Exception {

        // 远程地址
        RemoteUrl remoteUrl = new RemoteUrl(url, hostList);

        ValueVo confItemVo = restfulMgr.getJsonData(ValueVo.class, remoteUrl, retryTime, retrySleepSeconds);
        LOGGER.debug("remote server return: " + confItemVo.toString());

        if (confItemVo.getStatus().equals(Constants.NOTOK)) {
            throw new Exception("status is not ok.");
        }

        return confItemVo.getValue();
    }

    /**
     * 下载配置文件, remoteUrl是 url
     *
     * @throws Exception
     */
    public String downloadFileFromServer(String url, String fileName, String targetFileDir) throws Exception {

        // 下载的路径
        String localDir = getLocalDownloadDirPath();

        // 设置远程地址
        RemoteUrl remoteUrl = new RemoteUrl(url, hostList);

        // 下载
        return restfulMgr
                .downloadFromServer(remoteUrl, fileName, localDir, localDownloadDirTemp, targetFileDir,
                        enableLocalDownloadDirInClassPath,
                        retryTime,
                        retrySleepSeconds);

    }

    /**
     * 获取本地下载的路径DIR, 通过参数判断是否是临时路径
     *
     * @throws Exception
     */
    private String getLocalDownloadDirPath() throws Exception {

        String localUrl = localDownloadDir;

        if (!new File(localUrl).exists()) {
            new File(localUrl).mkdirs();
        }

        return localUrl;
    }

    @Override
    public void release() {

        restfulMgr.close();
    }
}
