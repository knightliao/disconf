package com.baidu.dsp.common.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.baidu.dsp.common.constant.ErrorCode;
import com.baidu.dsp.common.vo.JsonObjectBase;
import com.baidu.dsp.common.vo.JsonObjectUtils;
import com.github.knightliao.apollo.utils.data.JsonUtils;

/**
 * @author liaoqiqi
 * @version 2013-11-26
 */
public class WebCommonInterceptor extends HandlerInterceptorAdapter {

    protected static final Logger LOG = LoggerFactory.getLogger(WebCommonInterceptor.class);

    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {
    }

    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
            throws Exception {
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        return false;
    }

    /**
     * 自定义的全局错误
     *
     * @param request
     * @param response
     * @param message
     * @param errorCode
     *
     * @throws IOException
     */
    protected void returnJsonSystemError(HttpServletRequest request, HttpServletResponse response, String message,
                                         ErrorCode errorCode) throws IOException {

        JsonObjectBase jsonObjectBase = JsonObjectUtils.buildGlobalError(message, errorCode);

        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(JsonUtils.toJson(jsonObjectBase));
    }
}
