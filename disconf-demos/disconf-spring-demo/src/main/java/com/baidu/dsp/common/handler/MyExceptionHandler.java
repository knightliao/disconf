package com.baidu.dsp.common.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.baidu.dsp.common.constant.ErrorCode;
import com.baidu.dsp.common.vo.JsonObjectBase;
import com.baidu.dsp.common.vo.JsonObjectError;
import com.baidu.dsp.common.vo.JsonObjectUtils;

/**
 * 
 * @author liaoqiqi
 * @version 2013-12-2
 */
public class MyExceptionHandler extends SimpleMappingExceptionResolver
        implements ApplicationContextAware {

    protected static final Logger LOG = LoggerFactory
            .getLogger(MyExceptionHandler.class);

    protected ApplicationContext context;

    public void setApplicationContext(ApplicationContext arg0)
            throws BeansException {
        this.context = arg0;
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request,
            HttpServletResponse response, Object o, Exception e) {

        LOG.warn("ExceptionHandler FOUND. " + e.toString() + "\t"
                + e.getCause());

        return buildError("syserror.inner", ErrorCode.GLOBAL_ERROR);

    }

    /**
     * 全局的错误
     * 
     * @param mvc
     * @param errorMsg
     * @param errorCode
     * @return
     */
    private ModelAndView buildError(String errorMsg, ErrorCode errorCode) {

        JsonObjectBase jsonObject = JsonObjectUtils.buildGlobalError(errorMsg,
                errorCode);
        LOG.warn(jsonObject.toString());
        return JsonObjectUtils
                .JsonObjectError2ModelView((JsonObjectError) jsonObject);
    }

}
