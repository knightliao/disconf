package com.baidu.disconf.core.common.restful.type;

import org.apache.http.client.methods.HttpRequestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf.core.common.restful.core.UnreliableInterface;
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

    public RestfulGet(HttpRequestBase request, HttpResponseCallbackHandler<T> responseHandler) {

        this.request = request;
        this.httpResponseCallbackHandler = responseHandler;
    }

    /**
     * Get数据
     */
    @Override
    public Object call() throws Exception {

        T value = HttpClientUtil.execute(request, httpResponseCallbackHandler);

        return value;
    }
}
