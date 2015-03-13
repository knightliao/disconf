package com.baidu.dsp.common.vo;

import java.util.HashMap;
import java.util.Map;

import com.baidu.dsp.common.constant.ErrorCode;
import com.baidu.dsp.common.constant.FrontEndInterfaceConstant;

/**
 * 错误类
 *
 * @author liaoqiqi
 * @version 2013-12-3
 */
public class JsonObjectError extends JsonObjectBase {

    /**
     *
     */
    private static final long serialVersionUID = -169418876178835690L;

    /**
     * 内部状态码
     */
    private int status = ErrorCode.DEFAULT_ERROR.getCode();

    /**
     * Field 错误列表
     */
    Map<String, Object> fieldError = new HashMap<String, Object>();

    public JsonObjectError() {
        super();
        success = FrontEndInterfaceConstant.RETURN_FAIL;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * 参数错误: Field
     *
     * @param errors
     *
     * @return
     */
    public void addFieldError(String field, String errorInfo) {

        fieldError.put(field, errorInfo);
        message.put(FrontEndInterfaceConstant.FIELD_STRING, fieldError);
    }

    /**
     * 参数错误: Field
     *
     * @param errors
     *
     * @return
     */
    public void addGlobalError(String errorInfo) {

        message.put(FrontEndInterfaceConstant.GLOBAL_STRING, errorInfo);
    }

}
