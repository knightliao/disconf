package com.baidu.dsp.common.context;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;

public interface ContextReader {

    String getMessage(String resourceMessage) throws NoSuchMessageException;

    String getMessage(String resourceMessage, Object []args) throws NoSuchMessageException;

    String getMessage(MessageSourceResolvable resolvable) throws NoSuchMessageException;
}
