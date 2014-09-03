package com.baidu.disconf.client.scan.inner;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;
import com.baidu.disconf.client.common.annotations.DisconfItem;
import com.baidu.disconf.client.common.update.IDisconfUpdate;
import com.baidu.disconf.core.common.constants.DisConfigTypeEnum;
import com.github.knightliao.apollo.utils.common.ClassUtils;

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
     * 对于一个 get/is 方法，返回其相对应的Field
     * 
     * @return
     */
    public static Field getFieldFromMethod(Method method,
            Field[] expectedFields, DisConfigTypeEnum disConfigTypeEnum) {

        String fieldName = null;

        if (disConfigTypeEnum.equals(DisConfigTypeEnum.FILE)) {

            DisconfFileItem disconfFileItem = method
                    .getAnnotation(DisconfFileItem.class);

            // 根据用户设定的注解来获取
            fieldName = disconfFileItem.associateField();
        } else {
            DisconfItem disItem = method.getAnnotation(DisconfItem.class);

            // 根据用户设定的注解来获取
            fieldName = disItem.associateField();
        }

        //
        // 如果用户未设定注解，则猜其名字
        //
        if (StringUtils.isEmpty(fieldName)) {
            // 从方法名 获取其 Field 名
            fieldName = ClassUtils
                    .getFieldNameByGetMethodName(method.getName());
        }

        // 确认此Field名是正确的
        for (Field field : expectedFields) {

            if (field.getName().equals(fieldName)) {
                return field;
            }
        }

        LOGGER.error(method.toString() + " cannot get its related field name. ");

        return null;
    }
}
