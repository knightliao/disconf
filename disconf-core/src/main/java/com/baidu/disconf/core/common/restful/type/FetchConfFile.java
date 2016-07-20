package com.baidu.disconf.core.common.restful.type;

import java.io.File;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf.core.common.restful.core.UnreliableInterface;
import com.baidu.disconf.core.common.utils.OsUtil;

/**
 * 下载文件
 *
 * @author liaoqiqi
 */
public class FetchConfFile implements UnreliableInterface {

    protected static final Logger LOGGER = LoggerFactory.getLogger(FetchConfFile.class);

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

    /**
     * 进行下载
     */
    @Override
    public <T> T call() throws Exception {

        // 删除临时文件
        // LOGGER.info("start to remove tmp download file: " + ""
        // + localTmpFile.getAbsolutePath());
        if (localTmpFile.exists()) {
            localTmpFile.delete();
        }

        // start tp download
        LOGGER.debug("start to download. From: " + remoteUrl + " , TO: " + localTmpFile.getAbsolutePath());

        // 下载
        FileUtils.copyURLToFile(remoteUrl, localTmpFile);

        // check
        if (!OsUtil.isFileExist(localTmpFile.getAbsolutePath())) {
            throw new Exception("download is ok, but cannot find downloaded file." + localTmpFile);
        }

        // download success
        LOGGER.debug("download success!  " + localTmpFile.getAbsolutePath());

        return null;
    }

}
