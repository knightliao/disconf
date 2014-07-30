package com.baidu.disconf.core.utils;

import com.google.gson.Gson;

/**
 * Google Json工具
 * 
 * @author liaoqiqi
 * @version 2014-6-16
 */
public class GsonUtils {

    /**
     * 
     * @param object
     * @return
     */
    public static String toJson(Object object) {

        Gson gson = new Gson();
        String json = gson.toJson(object);
        return json;
    }
}
