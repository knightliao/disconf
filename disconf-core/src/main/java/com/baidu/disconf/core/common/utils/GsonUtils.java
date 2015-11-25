package com.baidu.disconf.core.common.utils;

import java.lang.reflect.Type;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Google Json工具
 *
 * @author liaoqiqi
 * @version 2014-6-16
 */
public final class GsonUtils {

    private GsonUtils() {

    }

    /**
     * @param object
     * @return
     */
    public static String toJson(Object object) {

        Gson gson = new Gson();
        String json = gson.toJson(object);
        return json;
    }

    /**
     * Parse json to map
     *
     * @param json
     * @return
     */
    public static Map<String, String> parse2Map(String json) {

        Type stringStringMap = new TypeToken<Map<String, String>>() {
        }.getType();

        Gson gson = new Gson();
        Map<String, String> map = gson.fromJson(json, stringStringMap);

        return map;
    }
}
