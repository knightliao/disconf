package com.baidu.disconf.web.service.zookeeper.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * 每个配置对应的数据
 *
 * @author liaoqiqi
 * @version 2014-9-12
 */
public class ZkDisconfData {

    /**
     * 每个配置对应一个实例的数据
     *
     * @author liaoqiqi
     * @version 2014-9-12
     */
    public static class ZkDisconfDataItem {

        // 所在机器
        private String machine = "";

        // 值
        private String value = "";

        private List<String> errorList = new ArrayList<String>();

        public String getMachine() {
            return machine;
        }

        public void setMachine(String machine) {
            this.machine = machine;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public List<String> getErrorList() {
            return errorList;
        }

        public void setErrorList(List<String> errorList) {
            this.errorList = errorList;
        }

        @Override
        public String toString() {
            return "ZkDisconfDataItem [machine=" + machine + ", value=" + value + ", errorList=" + errorList + "]";
        }

    }

    /*
     * 
     */
    private List<ZkDisconfDataItem> data = new ArrayList<ZkDisconfDataItem>();

    private String key;

    public List<ZkDisconfDataItem> getData() {
        return data;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setData(List<ZkDisconfDataItem> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ZkDisconfData [data=" + data + ", key=" + key + "]";
    }

}
