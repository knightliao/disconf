package com.baidu.disconf.core.common.restful.type;

import java.net.URL;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf.core.common.restful.core.UnreliableInterface;
import com.baidu.disconf.core.common.restful.impl.HttpResponseCallbackHandlerJsonHandler;
import com.baidu.disconf.core.common.utils.http.HttpClientUtil;
import com.baidu.disconf.core.common.utils.http.HttpResponseCallbackHandler;

/**
 * RestFul get
 *
 * @author liaoqiqi
 * @version 2014-6-16
 */
public class RestfulGet<ValueVo> implements UnreliableInterface {

    protected static final Logger LOGGER = LoggerFactory.getLogger(RestfulGet.class);

    private HttpRequestBase request = null;
    private HttpResponseCallbackHandler httpResponseCallbackHandler = null;

    public RestfulGet(URL url) {

        HttpGet request = new HttpGet(url.toString());
        request.addHeader("content-type", "application/json");
        this.request = request;
        this.httpResponseCallbackHandler = new
                HttpResponseCallbackHandlerJsonHandler();
    }

    /**
     * Get数据
     */
    @Override
    public Object call() throws Exception {

        ValueVo value = (ValueVo) HttpClientUtil.execute(request, httpResponseCallbackHandler);

        return value;
    }
}
