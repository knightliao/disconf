package com.baidu.disconf.client.store.processor.impl;

import static com.baidu.disconf.client.store.inner.DisconfCenterStore.getInstance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf.client.common.update.IDisconfUpdatePipeline;
import com.baidu.disconf.client.store.DisconfStorePipelineProcessor;

/**
 *
 */
public class DisconfStorePipelineProcessorImpl implements DisconfStorePipelineProcessor {

    protected static final Logger LOGGER = LoggerFactory.getLogger(DisconfStorePipelineProcessorImpl.class);

    @Override
    public void setDisconfUpdatePipeline(IDisconfUpdatePipeline iDisconfUpdatePipeline) {

        getInstance().setiDisconfUpdatePipeline(iDisconfUpdatePipeline);
    }
}
