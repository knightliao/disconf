package com.baidu.disconf2.client.fetcher.inner.restful;

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

import com.baidu.disconf2.client.config.inner.DisClientSysConfig;
import com.baidu.disconf2.client.fetcher.inner.restful.core.RemoteUrl;
import com.baidu.disconf2.client.fetcher.inner.restful.core.UnreliableInterface;
import com.baidu.disconf2.client.fetcher.inner.restful.file.FetchConfFile;
import com.baidu.disconf2.client.fetcher.inner.restful.http.RestfulGet;
import com.baidu.disconf2.client.fetcher.inner.restful.retry.RetryProxy;
import com.baidu.utils.ConfigLoaderUtils;
import com.baidu.utils.OsUtil;

/**
 * RestFul的一个实现,单例实现
 * 
 * @author liaoqiqi
 * @version 2014-6-10
 */
public class RestfulMgr {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(RestfulMgr.class);

    private RestfulMgr() {

    }

    /**
     * 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例 没有绑定关系，而且只有被调用到时才会装载，从而实现了延迟加载。
     */
    private static class SingletonHolder {
        /**
         * 静态初始化器，由JVM来保证线程安全
         */
        private static RestfulMgr instance = new RestfulMgr();
    }

    public static RestfulMgr getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 连接器
     */
    private Client client = null;

    /**
     * 
     * @Description: 初始化
     * 
     * @throws Exception
     * @return void
     * @author liaoqiqi
     * @date 2013-6-16
     */
    public void init() throws Exception {

        client = ClientBuilder.newBuilder().register(JacksonFeature.class)
                .build();

        if (client == null) {
            throw new Exception("RestfulMgr init failed!");
        }
    }

    /**
     * 
     * @return
     */
    public boolean isInit() {
        if (client == null) {
            return false;
        }
        return true;
    }

    /**
     * 获取JSON数据
     * 
     * @param clazz
     * @param remoteUrl
     * @return
     * @throws Exception
     */
    public <T> T getJsonData(Class<T> clazz, RemoteUrl remoteUrl)
            throws Exception {

        for (URL url : remoteUrl.getUrls()) {

            WebTarget webtarget = client.target(url.toURI());

            LOGGER.info("start to query url : " + webtarget.getUri().toString());

            Invocation.Builder builder = webtarget
                    .request(MediaType.APPLICATION_JSON_TYPE);

            // 可重试的下载
            UnreliableInterface unreliableImpl = new RestfulGet(builder);

            try {

                Response response = (Response) RetryProxy
                        .retry(unreliableImpl,
                                DisClientSysConfig.getInstance().CONF_SERVER_URL_RETRY_TIMES,
                                DisClientSysConfig.getInstance().CONF_SERVER_URL_RETRY_SLEEP_SECONDS);

                T t = (T) response.readEntity(clazz);

                return t;

            } catch (Exception e) {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                }
            }
        }

        throw new Exception();
    }

    /**
     * 
     * @Description：关闭
     * 
     * @return void
     * @author liaoqiqi
     * @date 2013-6-16
     */
    public void close() {

        if (client != null) {
            client.close();
        }
    }

    /**
     * 
     * 
     * @param remoteUrl
     *            远程地址
     * @param fileName
     *            文件名
     * @param localTmpFileDir
     *            本地临时 文件地址
     * @param localFileDir
     *            本地文件地址
     * @param isTransfer2Classpath
     *            是否将下载的文件放到Classpath目录下
     * @return 如果是放到Classpath目录下，则返回相对Classpath的路径，如果不是，则返回全路径
     * @throws Exception
     */
    public String downloadFromServer(RemoteUrl remoteUrl, String fileName,
            String localTmpFileDir, String localFileDir,
            boolean isTransfer2Classpath) throws Exception {

        // 本地临时全路径
        String localTmpFilePath = OsUtil.pathJoin(localTmpFileDir, fileName);
        // 本地路径
        String localFilePath = OsUtil.pathJoin(localFileDir, fileName);

        // 相应的File对象
        File localTmpFile = new File(localTmpFilePath);
        File localFile = new File(localFilePath);
        if (localFile.exists()) {
            localFile.delete();
        }

        try {

            // 可重试的下载
            retry4ConfDownload(
                    remoteUrl,
                    localTmpFile,
                    DisClientSysConfig.getInstance().CONF_SERVER_URL_RETRY_TIMES,
                    DisClientSysConfig.getInstance().CONF_SERVER_URL_RETRY_SLEEP_SECONDS);

            // 从临时文件夹迁移到下载文件夹
            OsUtil.transferFile(localTmpFile, localFile);

            // 再次转移至classpath目录下
            if (isTransfer2Classpath) {

                File classpathFile = getLocalDownloadFileInClasspath(fileName);
                if (classpathFile != null) {

                    // 从下载文件夹复制到classpath
                    OsUtil.transferFile(localFile, classpathFile);
                    localFile = classpathFile;

                } else {
                    LOGGER.warn("classpath is null, cannot transfer "
                            + fileName + " to classpath");
                }
            }

            LOGGER.info("Move to: " + localFile.getAbsolutePath());

        } catch (Exception e) {

            LOGGER.warn("download file failed, using previous download file.",
                    e);
        }

        //
        // 下载失败
        //
        if (!localFile.exists()) {
            throw new Exception("targe file cannot be found! " + fileName);
        }

        //
        // 下面为下载成功
        //

        // 如果是使用CLASS路径的，则返回相对classpath的路径
        if (!ConfigLoaderUtils.CLASS_PATH.isEmpty()) {
            String relavivePathString = OsUtil.getRelativePath(localFile,
                    new File(ConfigLoaderUtils.CLASS_PATH));
            if (relavivePathString != null) {
                if (new File(relavivePathString).isFile()) {
                    return relavivePathString;
                }
            }
        }

        // 否则, 返回全路径
        return localFile.getAbsolutePath();
    }

    /**
     * 
     * Retry封装 RemoteUrl 支持多Server的下载
     * 
     * @param remoteUrl
     * @param localTmpFile
     * @param retryTimes
     * @param sleepSeconds
     * @return
     */
    private static Object retry4ConfDownload(RemoteUrl remoteUrl,
            File localTmpFile, int retryTimes, int sleepSeconds)
            throws Exception {

        for (URL url : remoteUrl.getUrls()) {

            // 可重试的下载
            UnreliableInterface unreliableImpl = new FetchConfFile(url,
                    localTmpFile);

            try {

                return RetryProxy.retry(unreliableImpl, retryTimes,
                        sleepSeconds);

            } catch (Exception e) {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                }
            }
        }

        throw new Exception();
    }

    /**
     * 
     * @Description: 获取在CLASSPATH下的文件，如果找不到CLASSPATH，则返回null
     * 
     * @param fileName
     * @return
     * @throws Exception
     * @return File
     * @author liaoqiqi
     * @date 2013-6-20
     */
    private static File getLocalDownloadFileInClasspath(String fileName)
            throws Exception {

        String classpath = ConfigLoaderUtils.CLASS_PATH;

        if (classpath == null) {
            return null;
        }
        File file = new File(OsUtil.pathJoin(classpath, fileName));
        return file;
    }

}
