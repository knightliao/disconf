package com.baidu.disconf.core.common.restful.impl;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import com.baidu.disconf.core.common.json.ValueVo;
import com.baidu.disconf.core.common.utils.http.HttpResponseCallbackHandler;

/**
 * Created by knightliao on 16/1/7.
 */
public class HttpResponseCallbackHandlerJsonHandler implements HttpResponseCallbackHandler<ValueVo> {

    @Override
    public ValueVo handleResponse(String requestBody, HttpEntity entity) throws IOException {

        String json = EntityUtils.toString(entity, "UTF-8");

        com.google.gson.Gson gson = new com.google.gson.Gson();
        ValueVo response = gson.fromJson(json, ValueVo.class);

        return response;
    }
}
