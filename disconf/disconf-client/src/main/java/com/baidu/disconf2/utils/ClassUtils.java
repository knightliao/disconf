package com.baidu.disconf2.utils;

/**
 * 
 * @author liaoqiqi
 * @version 2014-6-11
 */
public class ClassUtils {

    /**
     * 由Get Method名称获取Field名
     * 
     * @return
     */
    public static String getFieldNameByGetMethodName(String methodName) {

        // 必须以get开始的
        if (!methodName.startsWith("get")) {
            return null;
        }

        String fieldName = methodName.substring(3);
        if (fieldName.length() >= 1) {
            String firstCharStr = String.valueOf(fieldName.charAt(0))
                    .toLowerCase();
            if (fieldName.length() > 1) {
                fieldName = firstCharStr + fieldName.substring(1);
            } else {
                fieldName = firstCharStr.toLowerCase();
            }
        }

        return fieldName;
    }
}
