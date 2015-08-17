package com.baidu.disconf.web.service.app.form;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.baidu.dsp.common.form.RequestFormBase;

import lombok.Data;

/**
 * @author liaoqiqi
 * @version 2014-1-24
 */
@Data
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

    // 邮箱
    private String emails = "";

}
