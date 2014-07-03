package com.baidu.disconf2.web.service.config.form;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 新建配置表格
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

    @NotNull(message = "key.empty")
    @NotEmpty(message = "key.empty")
    private String key;
    public static final String KEY = "key";

    @NotNull(message = "env.empty")
    private Long envId;
    public static final String ENVID = "envId";

    @NotNull(message = "value.empty")
    @NotEmpty(message = "value.empty")
    private String value;
    public static final String VALUE = "value";

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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getEnvId() {
        return envId;
    }

    public void setEnvId(Long envId) {
        this.envId = envId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ConfNewForm [appId=" + appId + ", version=" + version
                + ", key=" + key + ", envId=" + envId + ", value=" + value
                + "]";
    }

}
