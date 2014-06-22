package com.baidu.disconf2.service.config.form;

import javax.validation.constraints.NotNull;

/**
 * 
 * @author liaoqiqi
 * @version 2014-6-22
 */
public class VersionListForm {

    @NotNull
    private String app;

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

}
