package com.baidu.disconf.web.service.roleres.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;

import com.baidu.disconf.web.service.roleres.bo.RoleResource;
import com.baidu.disconf.web.service.roleres.constant.RoleResourceConstant;
import com.baidu.disconf.web.service.roleres.dao.RoleResourceDao;
import com.baidu.disconf.web.service.roleres.service.RoleResourceMgr;
import com.baidu.disconf.web.service.user.dto.Visitor;
import com.baidu.ub.common.commons.ThreadContext;

/**
 *
 */
@Service
public class RoleResourceMgrImpl implements RoleResourceMgr {

    protected static final Logger LOG = LoggerFactory.getLogger(RoleResourceMgrImpl.class);

    @Autowired
    private RoleResourceDao roleResDao;

    /**
     *
     */
    @Override
    @CacheEvict(value = "${role_res_cache_name}")
    public void evictCache() {
        LOG.info("Evicting role_resource cache...");
    }

    /**
     * @return
     */
    @Override
    @Cacheable(value = "${role_res_cache_name}")
    public Map<String, Map<RequestMethod, List<Integer>>> getAllAsMap() {
        Map<String, Map<RequestMethod, List<Integer>>> infoMap =
                new HashMap<String, Map<RequestMethod, List<Integer>>>();

        LOG.info("Querying role_resource table to get all...");
        List<RoleResource> roleResList = roleResDao.findAll();
        // 遍历列表，把数据按<url, <method, List<roleId>>>的形式加到infoMap
        for (RoleResource roleRes : roleResList) {

            String urlPattern = roleRes.getUrlPattern();
            if (!urlPattern.endsWith(RoleResourceConstant.URL_SPLITOR)) {
                urlPattern += RoleResourceConstant.URL_SPLITOR;
            }
            // LOG.info(urlPattern);

            Map<RequestMethod, List<Integer>> value = infoMap.get(urlPattern);
            if (value == null) {
                value = new HashMap<RequestMethod, List<Integer>>();
                infoMap.put(urlPattern, value);
            }
            updateMethodMap(value, roleRes.getRoleId(), roleRes.getMethodMask());
        }
        return infoMap;
    }

    /**
     * 把roleRes的信息加到对应的<method, List<roleId>>
     *
     * @param methodMap
     * @param roleId
     * @param methodMask
     */
    private void updateMethodMap(Map<RequestMethod, List<Integer>> methodMap, Integer roleId, String methodMask) {
        for (int i = 0; i < RoleResourceConstant.METHOD_NUM; ++i) {
            if (methodMask.charAt(i) == RoleResourceConstant.METHOD_IS_ACCESSIBLE) {
                RequestMethod curMethod = RoleResourceConstant.IND_METHOD_MAP.get(i);

                // update List<roleId>
                List<Integer> roleIdList = methodMap.get(curMethod);
                if (roleIdList == null) {
                    roleIdList = new ArrayList<Integer>();
                    methodMap.put(curMethod, roleIdList);
                }
                roleIdList.add(roleId);
            }
        }
    }

    /**
     * @param pattern
     * @param method
     *
     * @return
     */
    @Override
    public boolean checkUserPermission(String pattern, RequestMethod method) {
        // 获取用户角色
        Visitor visitor = ThreadContext.getSessionVisitor();
        if (visitor == null) {
            return false;
        }

        String urlPattarn = pattern;
        if (!urlPattarn.endsWith(RoleResourceConstant.URL_SPLITOR)) {
            urlPattarn += RoleResourceConstant.URL_SPLITOR;
        }

        Integer roleId = visitor.getRoleId();

        Map<String, Map<RequestMethod, List<Integer>>> roleResMap = getAllAsMap();
        Map<RequestMethod, List<Integer>> methodMap = roleResMap.get(urlPattarn);
        if (methodMap == null) {
            return false;
        }

        List<Integer> roleIdList = methodMap.get(method);
        if (roleIdList == null) {
            return false;
        }

        if (!roleIdList.contains(roleId)) {
            return false;
        }
        return true;
    }

}
