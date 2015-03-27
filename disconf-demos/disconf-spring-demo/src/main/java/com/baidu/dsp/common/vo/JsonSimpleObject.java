package com.baidu.dsp.common.vo;

import com.baidu.dsp.common.constant.FrontEndInterfaceConstant;

/**
 * 顶层结构的成功返回
 *
 * @author liaoqiqi
 * @version 2014-1-21
 */
public class JsonSimpleObject extends JsonObjectBase {

    /**
     *
     */
    private static final long serialVersionUID = -8022268276600397031L;

    private Object result = new Object();

    public JsonSimpleObject() {
        super();
        success = FrontEndInterfaceConstant.RETURN_OK;
    }

    @Override
    public String toString() {
        return "JsonSimpleObject [result=" + result + "]";
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

}
