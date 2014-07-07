package com.baidu.dsp.common.exception;

import java.util.HashMap;
import java.util.Map;

import com.baidu.dsp.common.constant.ErrorCode;
import com.baidu.dsp.common.constant.ModuleCode;
import com.baidu.dsp.common.exception.base.RuntimeGlobalException;

/**
 * 专门用业务逻辑校验出错的异常
 * 
 * @author liaoqiqi
 * @version 2013-12-2
 */
public class FieldException extends RuntimeGlobalException {

    /**
	 * 
	 */
    private static final long serialVersionUID = -774654820037286427L;

    private Map<String, String> messageErrorMap = new HashMap<String, String>();
    private boolean isGlobal = false;
    private String globalErrorMsg = "";

    /**
     * 局部的，非Map形式
     * 
     * @param field
     * @param messageSource
     */
    public FieldException(String field, String messageSource,
            Throwable throwable) {

        super(ErrorCode.FIELD_ERROR, messageSource, throwable);
        messageErrorMap.put(field, messageSource);
    }

    /**
     * 局部的，用Map形式
     * 
     * @param errorMap
     */
    public FieldException(Map<String, String> errorMap, Throwable throwable) {

        super(ErrorCode.FIELD_ERROR, "FIELD ERROR LIST", throwable);

        if (errorMap != null) {
            messageErrorMap = errorMap;
        }
    }

    /**
     * 全局的
     * 
     * @param globalErrorMsg
     */
    public FieldException(String globalErrorMsg, Throwable throwable) {

        super(ErrorCode.FIELD_ERROR, "GLOBAL FIELD ERROR", throwable);

        isGlobal = true;
        this.globalErrorMsg = globalErrorMsg;
    }

    public Map<String, String> getMessageErrorMap() {
        return messageErrorMap;
    }

    public boolean isGlobal() {
        return isGlobal;
    }

    public String getGlobalErrorMsg() {
        return globalErrorMsg;
    }

    public void setGlobalErrorMsg(String globalErrorMsg) {
        this.globalErrorMsg = globalErrorMsg;
    }

    @Override
    public ModuleCode getModuleCode() {
        return ModuleCode.OTHER;
    }

}
