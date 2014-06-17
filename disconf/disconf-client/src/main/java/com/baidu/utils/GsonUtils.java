package com.baidu.utils;

import com.google.gson.Gson;

/**
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
