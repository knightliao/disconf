package com.baidu.disconf.web.service.config.form;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 新建配置文件表格
 *
 * @author liaoqiqi
 * @version 2014-7-3
 */
public class ConfNewForm {

    @NotNull(message = "app.empty")
    private Long appId;
    public static final String APPID = "appId";

    @NotNull(message = "version.empty")
    @NotEmpty(message = "version.empty")
    private String version;
    public static final String VERSION = "version";

    @NotNull(message = "env.empty")
    private Long envId;
    public static final String ENVID = "envId";

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
        return "ConfNewForm [appId=" + appId + ", version=" + version + ", envId=" + envId + "]";
    }

    public ConfNewForm(Long appId, String version, Long envId) {
        super();
        this.appId = appId;
        this.version = version;
        this.envId = envId;
    }

    public ConfNewForm() {
        super();
    }

}
