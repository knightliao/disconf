package com.baidu.dsp.common.interceptor.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.servlet.ModelAndView;

import com.baidu.dsp.common.interceptor.WebCommonInterceptor;
import com.baidu.ub.common.commons.ThreadContext;
import com.github.knightliao.apollo.utils.tool.TokenUtil;

/**
 * 全局的异常处理拦截器
 *
 * @author liaoqiqi
 */
public class SessionInterceptor extends WebCommonInterceptor {

    protected static final Logger LOG = LoggerFactory.getLogger(SessionInterceptor.class);

    /**
     * 会话ID
     */
    public static final String SESSION_KEY = "sessionId";

    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {

        // 删除
        MDC.remove(SESSION_KEY);
        ThreadContext.removeSessionId();
        ThreadContext.clean();
    }

    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
            throws Exception {
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        // 初始化会话
        ThreadContext.init();

        // 放SessionId
        String token = TokenUtil.generateToken();
        MDC.put(SESSION_KEY, token);
        ThreadContext.putSessionId(token);

        return true;
    }
}