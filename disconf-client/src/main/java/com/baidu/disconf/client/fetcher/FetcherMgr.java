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
     *
     * @param url
     *
     * @return
     */
    String getValueFromServer(String url) throws Exception;

    /**
     * 下载配置文件, remoteUrl是目标 url
     *
     * @param url
     * @param fileName
     *
     * @return
     *
     * @throws Exception
     */
    String downloadFileFromServer(String url, String fileName) throws Exception;

    /**
     * 释放资源
     */
    void release();
}
