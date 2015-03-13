package com.baidu.disconf.web.service.config.form;

import javax.validation.constraints.NotNull;

import com.baidu.dsp.common.form.RequestListBase;

/**
 * @author liaoqiqi
 * @version 2014-6-23
 */
public class ConfListForm extends RequestListBase {

    /**
     *
     */
    private static final long serialVersionUID = -2498128894396346299L;

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

}
