package com.baidu.disconf2.core.common.path;

import java.util.HashMap;
import java.util.Map;

import com.baidu.disconf2.core.common.constants.Constants;
import com.baidu.disconf2.core.common.constants.DisConfigTypeEnum;

/**
 * Remote URL 格式，不包括 IP和端口号的
 * 
 * @author liaoqiqi
 * @version 2014-6-10
 */
public class PathMgr {

    public PathMgr() {

    }

    /**
     * 
     * 输入 配置项 或者 是配置ITEM, 获取远程参数等
     * 
     * @return
     */
    public static String getRemoteUrlParameter(String app, String version,
            String env, String key, DisConfigTypeEnum disConfigTypeEnum) {

        Map<String, String> parameterMap = getConfServerBasePathMap(app,
                version, env, key);

        // 配置文件或配置项
        parameterMap.put(Constants.TYPE,
                String.valueOf(disConfigTypeEnum.getType()));

        StringBuffer sb = new StringBuffer();

        if (disConfigTypeEnum.getType() == DisConfigTypeEnum.FILE.getType()) {
            sb.append("/file");
        } else {
            sb.append("/item");
        }

        sb.append("?");
        for (String thisKey : parameterMap.keySet()) {

            String cur = thisKey + "=" + parameterMap.get(thisKey);
            cur += "&";
            sb.append(cur);
        }

        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();

    }

    /**
     * 
     * @Description: 获取基本配置路径 的MAP
     * 
     * @return
     * @return String
     * @author liaoqiqi
     * @date 2013-6-16
     */
    private static Map<String, String> getConfServerBasePathMap(String app,
            String version, String env, String key) {

        Map<String, String> parameterMap = new HashMap<String, String>();

        parameterMap.put(Constants.VERSION, version);
        parameterMap.put(Constants.APP, app);
        parameterMap.put(Constants.ENV, env);
        parameterMap.put(Constants.KEY, key);

        return parameterMap;
    }

}
