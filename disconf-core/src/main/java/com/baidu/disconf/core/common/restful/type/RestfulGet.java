package com.baidu.disconf.core.common.restful.type;

import java.net.URL;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf.core.common.restful.core.UnreliableInterface;
import com.baidu.disconf.core.common.utils.http.impl.HttpResponseCallbackHandlerJsonHandler;
import com.baidu.disconf.core.common.utils.http.HttpClientUtil;
import com.baidu.disconf.core.common.utils.http.HttpResponseCallbackHandler;

/**
 * RestFul get
 *
 * @author liaoqiqi
 * @version 2014-6-16
 */
public class RestfulGet<T> implements UnreliableInterface {

    protected static final Logger LOGGER = LoggerFactory.getLogger(RestfulGet.class);

    private HttpRequestBase request = null;
    private HttpResponseCallbackHandler<T> httpResponseCallbackHandler = null;

    public RestfulGet(Class<T> clazz, URL url) {

        HttpGet request = new HttpGet(url.toString());
        request.addHeader("content-type", "application/json");
        this.request = request;
        this.httpResponseCallbackHandler = new
                HttpResponseCallbackHandlerJsonHandler<T>(clazz);
    }

    /**
     * Get数据
     */
    @Override
    public T call() throws Exception {

        T value = HttpClientUtil.execute(request, httpResponseCallbackHandler);

        return value;
    }
}
