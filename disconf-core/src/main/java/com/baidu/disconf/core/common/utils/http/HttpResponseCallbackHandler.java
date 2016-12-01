package com.baidu.disconf.core.common.utils.http;

import java.io.IOException;

import org.apache.http.HttpEntity;

public interface HttpResponseCallbackHandler<T> {
	
	public static final String STRING = "STRING";
	public static final String JSON = "JSON";
	
    T handleResponse(String requestBody, HttpEntity entity) throws IOException;
}
