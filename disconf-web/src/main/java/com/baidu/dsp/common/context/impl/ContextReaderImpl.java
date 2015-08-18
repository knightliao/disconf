package com.baidu.dsp.common.context.impl;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;

import com.baidu.dsp.common.context.ContextReader;

@Service
public class ContextReaderImpl implements ApplicationContextAware, ContextReader {

    protected static final Logger LOG = LoggerFactory.getLogger(ContextReaderImpl.class);

    private ApplicationContext context;

    public void setApplicationContext(ApplicationContext arg0) throws BeansException {
        this.context = arg0;
    }

    public String getMessage(String resourceMessage) throws NoSuchMessageException {
        return context.getMessage(resourceMessage, null, Locale.SIMPLIFIED_CHINESE);
    }

    public String getMessage(String resourceMessage, Object[] args) throws NoSuchMessageException {
        return context.getMessage(resourceMessage, args, Locale.SIMPLIFIED_CHINESE);
    }

    public String getMessage(MessageSourceResolvable resolvable) throws NoSuchMessageException {
        return context.getMessage(resolvable, Locale.SIMPLIFIED_CHINESE);
    }
}
