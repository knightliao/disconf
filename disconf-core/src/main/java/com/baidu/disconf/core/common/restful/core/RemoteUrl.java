package com.baidu.disconf.core.common.restful.core;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 远程对象URL表示，包括Host地址 和 URL
 *
 * @author liaoqiqi
 * @version 2014-6-10
 */
public class RemoteUrl {

    private String url;
    private List<String> serverList;

    private List<URL> urls = new ArrayList<URL>();

    protected static final Logger LOGGER = LoggerFactory.getLogger(RemoteUrl.class);

    public RemoteUrl(String url, List<String> serverList) {

        this.url = url;
        this.serverList = serverList;

        for (String server : serverList) {

            try {

                if (!server.startsWith("http://")) {
                    if (server.startsWith("https://")) {
                    } else {
                        server = "http://" + server;
                    }
                }

                urls.add(new URL(server + url));

            } catch (MalformedURLException e) {
                LOGGER.error(e.toString());
            }
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getServerList() {
        return serverList;
    }

    public void setServerList(List<String> serverList) {
        this.serverList = serverList;
    }

    public List<URL> getUrls() {

        return urls;
    }

    @Override
    public String toString() {
        return "RemoteUrl [url=" + url + ", serverList=" + serverList + ", urls=" + urls + "]";
    }

}
