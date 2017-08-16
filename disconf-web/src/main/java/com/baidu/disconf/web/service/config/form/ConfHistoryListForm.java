package com.baidu.disconf.web.service.config.form;

import com.baidu.dsp.common.form.RequestListBase;

/**
 * @author Chen Hui
 * @version 2014-6-23
 */
public class ConfHistoryListForm extends RequestListBase {

    /**
     *
     */
    private static final long serialVersionUID = -2498128894396346299L;

    private Long appId;

    private String version;

    private Long envId;

    private String configName;

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

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public void setPageNo(int pageNo) {
        this.getPage().setPageNo(pageNo);
    }

    public void setPageSize(int pageSize) {
        this.getPage().setPageSize(pageSize);
    }
}
