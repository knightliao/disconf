package com.baidu.disconf.web.service.roleres.constant;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author weiwei
 * @date 2013-12-20 下午6:29:00
 */
public class RoleResourceConstant {

    /**
     * a method is accessible to current role
     */
    public static final char METHOD_IS_ACCESSIBLE = '1';

    /**
     * number of http method, i.e., length of method_mask
     */
    public static final int METHOD_NUM = 4;

    /**
     * <method, index> for RoleResource.methodMask
     */
    public static final Map<RequestMethod, Integer> METHOD_IND_MAP;

    static {
        Map<RequestMethod, Integer> tmpMap = new HashMap<RequestMethod, Integer>();
        tmpMap.put(RequestMethod.GET, 0);
        tmpMap.put(RequestMethod.PUT, 1);
        tmpMap.put(RequestMethod.POST, 2);
        tmpMap.put(RequestMethod.DELETE, 3);
        METHOD_IND_MAP = Collections.unmodifiableMap(tmpMap);
    }

    /**
     * <index, method> for RoleResource.methodMask
     */
    public static final Map<Integer, RequestMethod> IND_METHOD_MAP;

    static {
        Map<Integer, RequestMethod> tmpMap = new HashMap<Integer, RequestMethod>();
        tmpMap.put(0, RequestMethod.GET);
        tmpMap.put(1, RequestMethod.PUT);
        tmpMap.put(2, RequestMethod.POST);
        tmpMap.put(3, RequestMethod.DELETE);
        IND_METHOD_MAP = Collections.unmodifiableMap(tmpMap);
    }

    public static final String URL_SPLITOR = "/";

}
