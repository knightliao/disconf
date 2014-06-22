package com.baidu.dsp.common.constant;

/**
 * 
 * @author liaoqiqi
 * @version 2013-12-2
 */
public enum ModuleCode {

    OTHER(0, "Other"), 
    DEMO_FANTUAN(1, "FANTUAN"),
    AD(2, "Ad"), 
    REMOTE(3,"Remote"),
    CREATIVITY(4, "Creativity"),
    EXCEPTION(5, "EXCEPTION"),
    DAS(6, "DAS");

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
