package com.baidu.disconf.client.fetcher;

/**
 * 下载模块
 *
 * @author liaoqiqi
 * @version 2014-6-12
 */
public interface FetcherMgr {

    /**
     * 根据 URL 从远程 获取Value值
     */
    String getValueFromServer(String url) throws Exception;

    /**
     * 下载配置文件, remoteUrl是目标 url, 下载到预定义的文件夹，并复制到classpath文件夹下
     *
     * @throws Exception
     */
    String downloadFileFromServer(String url, String fileName, String copy2TargetDirPath) throws Exception;

    /**
     * 释放资源
     */
    void release();
}
