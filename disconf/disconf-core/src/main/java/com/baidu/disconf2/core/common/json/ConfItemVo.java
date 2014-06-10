package com.baidu.disconf2.core.common.json;

/**
 * 
 * ITEM数据获取的的接口格式
 * 
 * @author liaoqiqi
 * @version 2014-6-10
 */
public class ConfItemVo {

    public static final Integer OK = 1;
    public static final Integer NOTOK = 0;

    // 是否存在此KEy
    private Integer status = OK;

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

}
