package com.baidu.disconf.web.service.zookeeper.form;

import javax.validation.constraints.NotNull;

/**
 * @author liaoqiqi
 * @version 2014-9-11
 */
public class ZkDeployForm {

    @NotNull
    private Long appId;

    @NotNull
    private String version;

    @NotNull
    private Long envId;

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Long getEnvId() {
        return envId;
    }

    public void setEnvId(Long envId) {
        this.envId = envId;
    }

    @Override
    public String toString() {
        return "ZkDeployForm [appId=" + appId + ", version=" + version + ", envId=" + envId + "]";
    }

}
