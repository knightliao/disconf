package com.baidu.disconf.core.common.utils.http;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

/**
 * Created by knightliao on 16/1/7.
 */
public class HttpClientKeepAliveStrategy implements ConnectionKeepAliveStrategy {

    private int keepAliveTimeOut = 5;

    @Override
    public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
        HeaderElementIterator it = new BasicHeaderElementIterator(
                response.headerIterator(HTTP.CONN_KEEP_ALIVE));
        while (it.hasNext()) {
            HeaderElement he = it.nextElement();
            String param = he.getName();
            String value = he.getValue();
            if (value != null && param.equalsIgnoreCase("timeout")) {
                try {
                    return Long.parseLong(value) * 1000;
                } catch (NumberFormatException ignore) {
                }
            }
        }
        return keepAliveTimeOut * 1000;
    }

    public void setKeepAliveTimeOut(int keepAliveTimeOut) {
        this.keepAliveTimeOut = keepAliveTimeOut;
    }
}
