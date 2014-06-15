package com.baidu.disconf2.client.scan.inner;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf2.client.common.annotations.DisconfFile;
import com.baidu.disconf2.client.common.inter.IDisconfUpdate;
import com.baidu.disconf2.utils.ClassUtils;

/**
 * 扫描校验模块
 * 
 * @author liaoqiqi
 * @version 2014-6-15
 */
public class ScanVerify {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(ScanVerify.class);

    /**
     * 判断回调函数实现的接口是否正确
     * 
     * @return
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
            LOGGER.error("Your class " + disconfUpdateServiceClass.toString()
                    + " should implement interface: "
                    + IDisconfUpdate.class.toString());
            return false;
        }

        return true;
    }

    /**
     * 判断配置文件的类型是否正确
     * 
     * @return
     */
    public static boolean isDisconfFileTypeRight(DisconfFile disconfFile) {

        String fileName = disconfFile.filename();

        if (!fileName.endsWith(".properties")) {

            LOGGER.error("now we only support .properites conf: "
                    + disconfFile.toString());
            return false;
        }

        return true;
    }

    /**
     * 对于一个get***方法，返回其相对应的Field
     * 
     * @return
     */
    public static Field getFieldFromMethod(Method method) {

        String methodName = method.getName();

        // 必须以get开始的
        if (!methodName.startsWith("get")) {
            LOGGER.error(method.toString() + " not start with get****");
            return null;
        }

        String fieldName = ClassUtils.getFieldNameByGetMethodName(methodName);

        Class<?> cls = method.getDeclaringClass();

        try {

            Field field = cls.getDeclaredField(fieldName);
            return field;

        } catch (Exception e) {
            LOGGER.error(method.toString()
                    + " cannot get its related field name " + fieldName);
        }

        return null;
    }
}
