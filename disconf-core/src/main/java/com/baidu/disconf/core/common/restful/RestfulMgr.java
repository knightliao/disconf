package com.baidu.disconf.core.common.restful;

import com.baidu.disconf.core.common.restful.core.RemoteUrl;

/**
 * Restful 抓取工具
 *
 * @author liaoqiqi
 * @version 2014-7-29
 */
public interface RestfulMgr {

    /**
     * 获取JSON数据
     *
     * @param clazz
     * @param remoteUrl
     *
     * @return
     *
     * @throws Exception
     */
    <T> T getJsonData(Class<T> clazz, RemoteUrl remoteUrl, int retryTimes, int retyrSleepSeconds) throws Exception;

    /**
     * @param remoteUrl          远程地址
     * @param fileName           文件名
     * @param localFileDir       本地文件地址
     * @param copy2TargetDirPath 下载完后，还需要复制到此文件夹下
     *
     * @return 如果是放到Classpath目录下，则返回相对Classpath的路径，如果不是，则返回全路径
     *
     * @throws Exception
     */
    String downloadFromServer(RemoteUrl remoteUrl, String fileName, String localFileDir, String copy2TargetDirPath,
                              int retryTimes, int retrySleepSeconds) throws Exception;

    void close();

    void init() throws Exception;
}
