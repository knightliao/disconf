package com.baidu.disconf2.client.fetcher.inner.restful.http;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf2.client.fetcher.inner.restful.core.UnreliableInterface;

/**
 * 
 * @author liaoqiqi
 * @email liaoqiqi@baidu.com
 *
 */

/**
 * Restful get
 */
public class RestfulGet implements UnreliableInterface {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(RestfulGet.class);

    private Invocation.Builder builder = null;

    public RestfulGet(Invocation.Builder builder) {

		this.builder = builder;
	}

	@Override
	public Object call() throws Exception {

		LOGGER.info("start to get...." );

		Response response = builder.get();

		if (response.getStatus() != 200) {
			throw new Exception("query is not ok, response "
					+ response.getStatus());
		}

		return response;
	}
}
