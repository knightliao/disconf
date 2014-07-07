package com.baidu.disconf.client.fetcher.inner.restful.http;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf.client.fetcher.inner.restful.core.UnreliableInterface;

/**
 * RestFul get
 * 
 * @author liaoqiqi
 * @version 2014-6-16
 */
public class RestfulGet implements UnreliableInterface {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(RestfulGet.class);

    private Invocation.Builder builder = null;

    public RestfulGet(Invocation.Builder builder) {

        this.builder = builder;
    }

    /**
     * Get数据
     */
    @Override
    public Object call() throws Exception {

        Response response = builder.get();

        if (response.getStatus() != 200) {
            throw new Exception("query is not ok, response "
                    + response.getStatus());
        }

        return response;
    }
}
