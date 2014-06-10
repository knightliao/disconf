package com.baidu.disconf2.client.fether.inner.restful.file;

import java.io.File;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf2.client.fether.inner.restful.core.UnreliableInterface;
import com.baidu.utils.OsUtil;

/**
 * 下载文件
 * 
 * @author liaoqiqi
 * @email liaoqiqi@baidu.com
 * 
 */
public class FetchConfFile implements UnreliableInterface {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(FetchConfFile.class);

    private URL remoteUrl;
    private File localTmpFile;

    /**
     * 远程文件，本地文件
     * 
     * @param remoteUrl
     * @param localTmpFile
     */
    public FetchConfFile(URL remoteUrl, File localTmpFile) {

        this.remoteUrl = remoteUrl;
        this.localTmpFile = localTmpFile;
    }

    @Override
    public Object call() throws Exception {

        // start tp download
        LOGGER.info("start to download. From: " + remoteUrl + " , TO: "
                + localTmpFile.getAbsolutePath());

        // 下载
        FileUtils.copyURLToFile(remoteUrl, localTmpFile);

        // check
        if (!OsUtil.isFileExist(localTmpFile.getAbsolutePath())) {
            throw new Exception(
                    "download is ok, but cannot find downloaded file."
                            + localTmpFile);
        }

        // download success
        LOGGER.info("download success!  " + localTmpFile.getAbsolutePath());

        return null;
    }

}
