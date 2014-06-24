package com.baidu.disconf2.web.service.config.form;

import javax.validation.constraints.NotNull;

/**
 * 
 * @author liaoqiqi
 * @version 2014-6-22
 */
public class VersionListForm {

    @NotNull
    private Long appId;

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

}
