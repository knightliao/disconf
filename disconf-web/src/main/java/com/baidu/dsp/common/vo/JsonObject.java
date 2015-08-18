package com.baidu.dsp.common.vo;

import java.util.HashMap;
import java.util.Map;

import com.baidu.dsp.common.constant.FrontEndInterfaceConstant;

import lombok.Data;

/**
 * 多层结构的 成功返回
 *
 * @author liaoqiqi
 * @version 2013-12-3
 */
@Data
public class JsonObject extends JsonObjectBase {

    /**
     *
     */
    private static final long serialVersionUID = -7115209443980058705L;

    private Map<String, Object> result = new HashMap<String, Object>();

    public JsonObject() {
        super();
        success = FrontEndInterfaceConstant.RETURN_OK;
    }

    public void addData(String key, Object value) {
        result.put(key, value);
    }

}
