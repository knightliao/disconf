package com.baidu.disconf.client.scan.inner.dynamic.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baidu.disconf.client.common.model.DisconfKey;
import com.baidu.disconf.client.common.update.IDisconfUpdate;
import com.baidu.disconf.client.common.update.IDisconfUpdatePipeline;

/**
 * 动态扫描对象
 *
 * @author liaoqiqi
 * @version 2014-6-18
 */
public class ScanDynamicModel {

    // 配置及影响的回调函数, Key为配置项KEY 或 配置文件
    private Map<DisconfKey, List<IDisconfUpdate>> disconfUpdateServiceInverseIndexMap =
            new HashMap<DisconfKey, List<IDisconfUpdate>>();

    private IDisconfUpdatePipeline disconfUpdatePipeline;

    public Map<DisconfKey, List<IDisconfUpdate>> getDisconfUpdateServiceInverseIndexMap() {
        return disconfUpdateServiceInverseIndexMap;
    }

    public IDisconfUpdatePipeline getDisconfUpdatePipeline() {
        return disconfUpdatePipeline;
    }

    public void setDisconfUpdatePipeline(
            IDisconfUpdatePipeline disconfUpdatePipeline) {
        this.disconfUpdatePipeline = disconfUpdatePipeline;
    }

    public void setDisconfUpdateServiceInverseIndexMap(Map<DisconfKey,
            List<IDisconfUpdate>>
                                                               disconfUpdateServiceInverseIndexMap) {
        this.disconfUpdateServiceInverseIndexMap = disconfUpdateServiceInverseIndexMap;
    }

    @Override
    public String toString() {
        return "ScanDynamicModel{" +
                "disconfUpdateServiceInverseIndexMap=" + disconfUpdateServiceInverseIndexMap +
                ", disconfUpdatePipeline=" + disconfUpdatePipeline +
                '}';
    }
}
