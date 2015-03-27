package com.baidu.disconf.client.common.model;

/**
 * 通用的模型数据，包括 APP，版本，环境，Zookeeper上的URL表示
 *
 * @author liaoqiqi
 * @version 2014-5-20
 */
public class DisConfCommonModel {

    // app 名
    private String app;

    // 版本号
    private String version;

    // 环境
    private String env;

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    @Override
    public String toString() {
        return "DisConfCommonModel [app=" + app + ", version=" + version + ", env=" + env + "]";
    }

}
