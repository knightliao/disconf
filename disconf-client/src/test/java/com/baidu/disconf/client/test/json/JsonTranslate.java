package com.baidu.disconf.client.test.json;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.baidu.disconf.client.test.common.BaseSpringTestCase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Gson的测试
 *
 * @author liaoqiqi
 * @version 2014-6-16
 */
public class JsonTranslate extends BaseSpringTestCase {

    @Test
    public void test() {

        Map<String, String> colours = new HashMap<String, String>();
        colours.put("BLACK", "#000000");
        colours.put("RED", "#FF0000");
        colours.put("GREEN", "#008000");
        colours.put("BLUE", "#0000FF");
        colours.put("YELLOW", "#FFFF00");
        colours.put("WHITE", "#FFFFFF");

        //
        // Convert a Map into JSON string.
        //
        Gson gson = new Gson();
        String json = gson.toJson(colours);
        System.out.println("json = " + json);

        //
        // Convert JSON string back to Map.
        //
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        Map<String, String> map = gson.fromJson(json, type);
        for (String key : map.keySet()) {
            System.out.println("map.get = " + map.get(key));
        }
    }
}
