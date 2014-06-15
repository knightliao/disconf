package com.baidu.disconf2.core.common.json;

import com.baidu.disconf2.core.common.constants.Constants;

/**
 * 
 * 通用的Value Vo
 * 
 * 
 * @author liaoqiqi
 * @version 2014-6-10
 */
public class ValueVo {

    // 是否存在此KEy
    private Integer status = Constants.OK;

    //
    private String value = "";

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ConfItemVo [status=" + status + ", value=" + value + "]";
    }

}
