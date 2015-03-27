package com.baidu.disconf.web.service.app.form;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.baidu.dsp.common.form.RequestFormBase;

/**
 * @author liaoqiqi
 * @version 2014-1-24
 */
public class AppNewForm extends RequestFormBase {

    /**
     *
     */
    private static final long serialVersionUID = 4329463343279659715L;

    @NotNull(message = "app.empty")
    @NotEmpty(message = "app.empty")
    private String app;
    public final static String APP = "app";

    @NotNull(message = "desc.empty")
    @NotEmpty(message = "desc.empty")
    private String desc;

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "AppNew [app=" + app + ", desc=" + desc + "]";
    }
}
