package com.baidu.disconf.client.scan.inner.model;

import java.util.List;
import java.util.Map;

import com.baidu.disconf.client.common.inter.IDisconfUpdate;

/**
 * 动态扫描对象
 * 
 * @author liaoqiqi
 * @version 2014-6-18
 */
public class ScanDynamicModel {

    // 配置及影响的回调函数, Key为配置文件Key或配置项Key
    private Map<String, List<IDisconfUpdate>> disconfUpdateServiceInverseIndexMap;

    public Map<String, List<IDisconfUpdate>> getDisconfUpdateServiceInverseIndexMap() {
        return disconfUpdateServiceInverseIndexMap;
    }

    public void setDisconfUpdateServiceInverseIndexMap(
            Map<String, List<IDisconfUpdate>> disconfUpdateServiceInverseIndexMap) {
        this.disconfUpdateServiceInverseIndexMap = disconfUpdateServiceInverseIndexMap;
    }

    @Override
    public String toString() {
        return "ScanDynamicModel [disconfUpdateServiceInverseIndexMap="
                + disconfUpdateServiceInverseIndexMap + "]";
    }

}
