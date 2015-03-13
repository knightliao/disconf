package com.baidu.dsp.common.constant;

/**
 * @author liaoqiqi
 * @version 2013-12-2
 */
public enum ModuleCode {

    OTHER(0, "Other"), REMOTE(3, "Remote"), EXCEPTION(5, "EXCEPTION"), FILE(6, "FILEUPLOAD");
    ;

    private int code = 0;
    private String modelName = null;

    private ModuleCode(int code, String modelName) {
        this.code = code;
        this.modelName = modelName;
    }

    public int getCode() {
        return code;
    }

    public String getModelName() {
        return modelName;
    }
}
