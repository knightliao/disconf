package com.baidu.disconf2.client.fetcher.inner.restful.vo;

import java.util.HashMap;
import java.util.Map;

/**
 * 多层结构的 成功返回
 * 
 * @author liaoqiqi
 * @version 2013-12-3
 */
public class JsonObject extends JsonObjectBase {

    /**
	 * 
	 */
    private static final long serialVersionUID = -7115209443980058705L;

    private Map<String, Object> result = new HashMap<String, Object>();

    public JsonObject() {
        super();
        success = "true";
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

    public void addData(String key, Object value) {
        result.put(key, value);
    }

}
