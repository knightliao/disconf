package com.baidu.disconf.core.common.utils.http.impl;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import com.baidu.disconf.core.common.utils.http.HttpResponseCallbackHandler;

/**
 * Created by knightliao on 16/1/7.
 */
public class HttpResponseCallbackHandlerJsonHandler<T> implements HttpResponseCallbackHandler<T> {

    private Class<T> clazz = null;

    public HttpResponseCallbackHandlerJsonHandler(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T handleResponse(String requestBody, HttpEntity entity) throws IOException {

        String json = EntityUtils.toString(entity, "UTF-8");

        com.google.gson.Gson gson = new com.google.gson.Gson();
        T response = gson.fromJson(json, clazz);

        return response;
    }
}
