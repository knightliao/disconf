package com.baidu.disconf.web.test.service.config.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.baidu.disconf.web.service.config.bo.Config;
import com.baidu.disconf.web.service.config.dao.ConfigDao;
import com.baidu.disconf.web.test.common.BaseTestCase;
import com.github.knightliao.apollo.utils.io.FileUtils;

import junit.framework.Assert;

/**
 * @author liaoqiqi
 * @version 2014-6-17
 */
public class ConfigDaoTestCase extends BaseTestCase {

    protected static final Logger LOG = LoggerFactory.getLogger(ConfigDaoTestCase.class);

    @Autowired
    private ConfigDao configDao;

    @Test
    public void test() {

        URL url = ClassLoader.getSystemResource("file2/confA.properties");

        byte[] btyes = readFileContent(url.getPath());

        try {

            // read data
            String str = new String(btyes, "UTF-8");

            // save to db
            Config config = configDao.get(1L);
            config.setValue(str);
            configDao.update(config);

            // read
            LOG.info(configDao.get(1L).getValue());

        } catch (UnsupportedEncodingException e) {

            Assert.assertTrue(false);
        }
    }

    /**
     * 读取文件的内容到byte数组中
     *
     * @param fileName
     *
     * @return
     */
    private byte[] readFileContent(String fileName) {

        InputStream is = null;
        try {

            // 按GBK编码与UTF-8编码分别查找文件
            File f = new File(new String(fileName.getBytes("GBK")));
            f = f.isFile() ? f : new File(new String(fileName.getBytes("UTF-8")));
            if (!f.isFile()) {
                LOG.error(fileName + " 文件不存在!");
                return null;
            }

            int length = (int) f.length();
            byte[] bytes = new byte[length];

            is = new FileInputStream(f);
            is.read(bytes);
            return bytes;

        } catch (Exception e) {

            LOG.error("error when download " + fileName, e);

        } finally {
            FileUtils.closeInputStream(is);
        }
        return null;
    }
}
