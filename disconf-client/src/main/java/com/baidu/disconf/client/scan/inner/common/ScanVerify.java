package com.baidu.disconf.client.scan.inner.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.constants.SupportFileTypeEnum;
import com.baidu.disconf.client.common.update.IDisconfUpdate;

/**
 * 扫描校验模块
 *
 * @author liaoqiqi
 * @version 2014-6-15
 */
public class ScanVerify {

    protected static final Logger LOGGER = LoggerFactory.getLogger(ScanVerify.class);

    /**
     * 判断回调函数实现的接口是否正确
     */
    public static boolean hasIDisconfUpdate(Class<?> disconfUpdateServiceClass) {

        Class<?>[] interfaceClasses = disconfUpdateServiceClass.getInterfaces();
        boolean hasInterface = false;
        for (Class<?> infClass : interfaceClasses) {
            if (infClass.equals(IDisconfUpdate.class)) {
                hasInterface = true;
            }
        }
        if (!hasInterface) {
            LOGGER.error("Your class " + disconfUpdateServiceClass.toString() + " should implement interface: " +
                             IDisconfUpdate.class.toString());
            return false;
        }

        return true;
    }

    /**
     * 判断配置文件的类型是否正确
     */
    public static boolean isDisconfFileTypeRight(DisconfFile disconfFile) {

        String fileName = disconfFile.filename();

        SupportFileTypeEnum supportFileTypeEnum = SupportFileTypeEnum.getByFileName(fileName);

        if (supportFileTypeEnum == null) {

            LOGGER.error("now we only support this type of conf: " + disconfFile.toString());
            return false;
        }

        return true;
    }
}
