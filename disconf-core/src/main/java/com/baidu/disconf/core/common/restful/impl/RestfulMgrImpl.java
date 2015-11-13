package com.baidu.disconf.core.common.restful.impl;

import java.io.File;
import java.net.URL;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf.core.common.restful.RestfulMgr;
import com.baidu.disconf.core.common.restful.core.RemoteUrl;
import com.baidu.disconf.core.common.restful.core.UnreliableInterface;
import com.baidu.disconf.core.common.restful.retry.RetryStrategy;
import com.baidu.disconf.core.common.restful.type.FetchConfFile;
import com.baidu.disconf.core.common.restful.type.RestfulGet;
import com.baidu.disconf.core.common.utils.MyStringUtils;
import com.github.knightliao.apollo.utils.io.OsUtil;

/**
 * RestFul的一个实现, 独立模块
 *
 * @author liaoqiqi
 * @version 2014-6-10
 */
public class RestfulMgrImpl implements RestfulMgr {

    protected static final Logger LOGGER = LoggerFactory.getLogger(RestfulMgrImpl.class);

    /**
     * 连接器
     */
    private Client client = null;

    /**
     * 重试策略
     */
    private RetryStrategy retryStrategy;

    public RestfulMgrImpl(RetryStrategy retryStrategy) {

        this.retryStrategy = retryStrategy;
    }

    /**
     * @return void
     *
     * @throws Exception
     * @Description: 初始化
     * @author liaoqiqi
     * @date 2013-6-16
     */
    public void init() throws Exception {

        client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();

        if (client == null) {
            throw new Exception("RestfulMgr init failed!");
        }
    }

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
    public <T> T getJsonData(Class<T> clazz, RemoteUrl remoteUrl, int retryTimes, int retyrSleepSeconds)
            throws Exception {

        for (URL url : remoteUrl.getUrls()) {

            WebTarget webtarget = client.target(url.toURI());

            LOGGER.debug("start to query url : " + webtarget.getUri().toString());

            Invocation.Builder builder = webtarget.request(MediaType.APPLICATION_JSON_TYPE);

            // 可重试的下载
            UnreliableInterface unreliableImpl = new RestfulGet(builder);

            try {

                Response response = (Response) retryStrategy.retry(unreliableImpl, retryTimes, retyrSleepSeconds);

                return response.readEntity(clazz);

            } catch (Exception e) {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    LOGGER.info("pass");
                }
            }
        }

        throw new Exception("cannot get: " + remoteUrl);
    }

    /**
     * @return void
     *
     * @Description：关闭
     * @author liaoqiqi
     * @date 2013-6-16
     */
    public void close() {

        if (client != null) {
            client.close();
        }
    }

    /**
     * @param remoteUrl          远程地址
     * @param fileName           文件名
     * @param localFileDir       本地文件地址
     * @param copy2TargetDirPath 下载完后，还需要复制到此文件夹下
     * @param retryTimes
     * @param retrySleepSeconds
     *
     * @return
     *
     * @throws Exception
     */
    @Override
    public String downloadFromServer(RemoteUrl remoteUrl, String fileName, String localFileDir,
                                     String copy2TargetDirPath, int retryTimes, int retrySleepSeconds)
            throws Exception {

        // 目标地址文件
        File localFile = null;

        //
        // 进行下载、mv、copy
        //

        try {

            // 可重试的下载
            File tmpFilePathUniqueFile = retryDownload(fileName, remoteUrl, retryTimes, retrySleepSeconds);

            // 将 tmp file copy localFileDir
            transfer2SpecifyDir(tmpFilePathUniqueFile, localFileDir, fileName, false);

            // mv 到指定目录
            if (copy2TargetDirPath != null) {

                localFile = transfer2SpecifyDir(tmpFilePathUniqueFile, copy2TargetDirPath, fileName, true);
            }

            LOGGER.debug("Move to: " + localFile.getAbsolutePath());

        } catch (Exception e) {

            LOGGER.warn("download file failed, using previous download file.", e);
        }

        //
        // 判断是否下载失败
        //

        if (!localFile.exists()) {
            throw new Exception("target file cannot be found! " + fileName);
        }

        //
        // 下面为下载成功
        //

        // 返回相对路径
        if (localFileDir != null) {
            String relativePathString = OsUtil.getRelativePath(localFile, new File(localFileDir));
            if (relativePathString != null) {
                if (new File(relativePathString).isFile()) {
                    return relativePathString;
                }
            }
        }

        // 否则, 返回全路径
        return localFile.getAbsolutePath();
    }

    /**
     * 可重试的下载
     *
     * @param fileName
     * @param remoteUrl
     * @param retryTimes
     * @param retrySleepSeconds
     *
     * @throws Exception
     */
    private File retryDownload(String fileName, RemoteUrl remoteUrl, int retryTimes, int retrySleepSeconds)
            throws Exception {

        String tmpFileDir = "./disconf/download";
        String tmpFilePath = OsUtil.pathJoin(tmpFileDir, fileName);
        String tmpFilePathUnique = MyStringUtils.getRandomName(tmpFilePath);
        File tmpFilePathUniqueFile = new File(tmpFilePathUnique);
        retry4ConfDownload(remoteUrl, tmpFilePathUniqueFile, retryTimes, retrySleepSeconds);

        return tmpFilePathUniqueFile;
    }

    /**
     * copy/mv 到指定目录
     *
     * @param srcFile
     * @param copy2TargetDirPath
     * @param fileName
     *
     * @return
     *
     * @throws Exception
     */
    private File transfer2SpecifyDir(File srcFile, String copy2TargetDirPath, String fileName,
                                     boolean isMove) throws Exception {

        // make dir
        OsUtil.makeDirs(copy2TargetDirPath);

        File targetPath = new File(OsUtil.pathJoin(copy2TargetDirPath, fileName));
        if (targetPath != null) {
            // 从下载文件 复制/mv 到targetPath 原子性的做转移
            OsUtil.transferFileAtom(srcFile, targetPath, isMove);
            return targetPath;

        } else {
            LOGGER.warn("targetPath is null, cannot transfer " + fileName + " to targetPath");
            return null;
        }
    }

    /**
     * Retry封装 RemoteUrl 支持多Server的下载
     *
     * @param remoteUrl
     * @param localTmpFile
     * @param retryTimes
     * @param sleepSeconds
     *
     * @return
     */
    private Object retry4ConfDownload(RemoteUrl remoteUrl, File localTmpFile, int retryTimes, int sleepSeconds)
            throws Exception {

        for (URL url : remoteUrl.getUrls()) {

            // 可重试的下载
            UnreliableInterface unreliableImpl = new FetchConfFile(url, localTmpFile);

            try {

                return retryStrategy.retry(unreliableImpl, retryTimes, sleepSeconds);

            } catch (Exception e) {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    LOGGER.info("pass");
                }
            }
        }

        throw new Exception("download failed.");
    }

}
