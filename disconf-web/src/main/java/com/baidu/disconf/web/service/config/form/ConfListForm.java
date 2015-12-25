package com.baidu.disconf.web.service.config.form;

import javax.validation.constraints.NotNull;

import com.baidu.dsp.common.form.RequestListBase;

import lombok.Data;

/**
 * @author liaoqiqi
 * @version 2014-6-23
 */
@Data
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

}
