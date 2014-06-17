package com.baidu.dsp.common.context.impl;

import java.util.Locale;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.baidu.dsp.common.context.ContextReader;

@Service
public class ContextReaderImpl implements ApplicationContextAware,
        ContextReader {

    private ApplicationContext context;

    public void setApplicationContext(ApplicationContext arg0)
            throws BeansException {
        this.context = arg0;
    }

    public String getMessage(String resourceMessage) {

        String message = context.getMessage(resourceMessage, null,
                Locale.SIMPLIFIED_CHINESE);
        return message;
    }
}
