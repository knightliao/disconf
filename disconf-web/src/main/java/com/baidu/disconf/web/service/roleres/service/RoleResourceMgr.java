package com.baidu.disconf.web.service.roleres.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 */
public interface RoleResourceMgr {

    /**
     * get all role_resource info in db/cache info in the form of <url, <method,
     * List<roleId>>>
     *
     * @return
     */
    public Map<String, Map<RequestMethod, List<Integer>>> getAllAsMap();

    /**
     * evict dspRoleResourceCache so that the next query will get data from db
     */
    public void evictCache();

    /**
     * 检查用户是否有权限访问
     *
     * @param pattern
     * @param method
     *
     * @return
     */
    public boolean checkUserPermission(String pattern, RequestMethod method);

}
