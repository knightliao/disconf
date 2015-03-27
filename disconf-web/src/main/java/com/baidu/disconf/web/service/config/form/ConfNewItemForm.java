package com.baidu.disconf.web.service.config.form;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 新建配置项表格
 *
 * @author liaoqiqi
 * @version 2014-7-3
 */
public class ConfNewItemForm extends ConfNewForm {

    @NotNull(message = "value.empty")
    @NotEmpty(message = "value.empty")
    private String value;
    public static final String VALUE = "value";

    @NotNull(message = "key.empty")
    @NotEmpty(message = "key.empty")
    private String key;
    public static final String KEY = "key";

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "ConfNewItemForm [value=" + value + ", key=" + key + "]";
    }

    public ConfNewItemForm(ConfNewForm confNewForm) {
        super(confNewForm.getAppId(), confNewForm.getVersion(), confNewForm.getEnvId());
    }

    public ConfNewItemForm() {
        super();
    }
}
