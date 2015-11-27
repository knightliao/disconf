package com.baidu.disconf.web.service.roleres.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.baidu.disconf.web.service.roleres.constant.RoleResourceConstant;
import com.baidu.disconf.web.service.user.dto.Visitor;
import com.baidu.dsp.common.exception.AccessDeniedException;
import com.baidu.ub.common.commons.ThreadContext;

/**
 * 判断用户是否具有请求方法的访问权
 */
@Aspect
public class RoleResourceAspect {

    protected static final Logger LOG = LoggerFactory.getLogger(RoleResourceAspect.class);

    @Autowired
    private RoleResourceMgr roleResMgr;

    // 不用进行权限控制
    private List<String> noAuthCheckUrl;

    @Pointcut(value = "execution(public * *(..))")
    public void anyPublicMethod() {
    }

    /**
     * 判断当前用户对访问的方法是否有权限
     *
     * @param pjp            方法
     * @param requestMapping 方法上的annotation
     *
     * @return
     *
     * @throws Throwable
     */
    @Around("anyPublicMethod() && @annotation(requestMapping) && !@annotation(com.baidu.dsp.common.annotation.NoAuth)")
    public Object decideAccess(ProceedingJoinPoint pjp, RequestMapping requestMapping) throws Throwable {

        // 获取method上的url，若未标注value则默认为空字符串
        String[] values = requestMapping.value();
        String methodUrl = "";
        if (values.length != 0) {
            methodUrl = values[0];
        }

        String clsUrl = pjp.getTarget().getClass().getAnnotation(RequestMapping.class).value()[0];

        // 拼接method和class上标注的url
        if (!clsUrl.endsWith(RoleResourceConstant.URL_SPLITOR) &&
                !methodUrl.startsWith(RoleResourceConstant.URL_SPLITOR)) {
            clsUrl += RoleResourceConstant.URL_SPLITOR;
        }

        String urlPattarn = clsUrl + methodUrl;
        if (!urlPattarn.endsWith(RoleResourceConstant.URL_SPLITOR)) {
            urlPattarn += RoleResourceConstant.URL_SPLITOR;
        }

        if (noAuthCheckUrl != null && noAuthCheckUrl.contains(urlPattarn)) {

            LOG.info("don't need to check this url: " + urlPattarn);
        } else {

            // 获取method上标注的http method，若未标注method则默认为GET
            RequestMethod[] methods = requestMapping.method();
            RequestMethod methodType = RequestMethod.GET;
            if (methods.length != 0) {
                methodType = methods[0];
            }

            String urlInfo = urlPattarn + ", method:" + methodType.toString();

            // 获取用户角色
            Visitor visitor = ThreadContext.getSessionVisitor();
            if (visitor == null) {
                LOG.warn("No session visitor!");
                throw new AccessDeniedException("No session visitor! " + urlInfo);
            }
            Integer roleId = visitor.getRoleId();
            String visitorInfo = ", UserId:" + visitor.getId() + ", RoleId:" + roleId;

            Boolean isPriviledged = true;
            // 判断用户是否有权限访问方法
            if (!this.isMethodAccessible(urlPattarn, methodType, roleId)) {
                isPriviledged = false;
                throw new AccessDeniedException("Access Denied: " + urlInfo + visitorInfo);
            }
            LOG.info("Accessing URL:" + urlInfo + visitorInfo + ", Is priviledged:" + isPriviledged.toString());
        }

        Object rtnOb = null;

        try {
            // 执行方法
            rtnOb = pjp.proceed();
        } catch (Throwable t) {
            LOG.info(t.getMessage());
            throw t;
        }

        return rtnOb;
    }

    /**
     * 用户角色是否有权限访问当前方法
     *
     * @param url
     * @param method
     * @param userRoleId
     *
     * @return
     */
    private boolean isMethodAccessible(String url, RequestMethod method, Integer userRoleId) {
        boolean accessible = false;

        List<Integer> roleIdList = getPriviledgedRoles(url, method);
        if (roleIdList.contains(userRoleId)) {
            accessible = true;
        }

        return accessible;
    }

    /**
     * 获取可以访问方法的角色id列表
     *
     * @param url
     * @param method
     *
     * @return
     */
    private List<Integer> getPriviledgedRoles(String url, RequestMethod method) {

        Map<String, Map<RequestMethod, List<Integer>>> roleResMap = roleResMgr.getAllAsMap();
        Map<RequestMethod, List<Integer>> methodMap = roleResMap.get(url);
        if (methodMap == null) {
            return new ArrayList<Integer>();
        }

        List<Integer> roleIdList = methodMap.get(method);
        if (roleIdList == null) {
            roleIdList = new ArrayList<Integer>();
        }

        return roleIdList;
    }

    public List<String> getNoAuthCheckUrl() {
        return noAuthCheckUrl;
    }

    public void setNoAuthCheckUrl(List<String> noAuthCheckUrl) {
        this.noAuthCheckUrl = noAuthCheckUrl;
    }

}
