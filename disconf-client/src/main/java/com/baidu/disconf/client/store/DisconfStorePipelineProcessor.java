package com.baidu.disconf.client.store;

import com.baidu.disconf.client.common.update.IDisconfUpdatePipeline;

/**
 *
 */
public interface DisconfStorePipelineProcessor {

    void setDisconfUpdatePipeline(IDisconfUpdatePipeline iDisconfUpdatePipeline);
}
