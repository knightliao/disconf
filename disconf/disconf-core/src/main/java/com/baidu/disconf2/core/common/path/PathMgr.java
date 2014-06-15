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
    public static String getRemoteUrlParameter(String urlPrefix, String app,
            String version, String env, String key,
            DisConfigTypeEnum disConfigTypeEnum) {

        Map<String, String> parameterMap = getConfServerBasePathMap(app,
                version, env, key);

        // 配置文件或配置项
        parameterMap.put(Constants.TYPE,
                String.valueOf(disConfigTypeEnum.getType()));

        StringBuffer sb = new StringBuffer();
        sb.append(urlPrefix);

        if (disConfigTypeEnum.getType() == DisConfigTypeEnum.FILE.getType()) {
            sb.append(Constants.SEP_STRING + Constants.STORE_FILE_URL_KEY);
        } else {
            sb.append(Constants.SEP_STRING + Constants.STORE_ITEM_URL_KEY);
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
     * 获取Zookeeper地址URL
     * 
     * @return
     */
    public static String getZooHostsUrl(String urlPrefix) {

        return urlPrefix + Constants.SEP_STRING + Constants.ZOO_HOSTS_URL_KEY;
    }

    /**
     * 
     * @Description: 获取APP的Zookeeper的基本路径
     * 
     * @return
     * @return String
     * @author liaoqiqi
     * @date 2013-6-17
     */
    public static String getZooBaseUrl(String urlPrefix, String app,
            String env, String version) {

        StringBuffer sb = new StringBuffer();
        sb.append(urlPrefix);

        sb.append(Constants.SEP_STRING);
        sb.append(app);

        sb.append("_");
        sb.append(version);

        sb.append("_");
        sb.append(env);

        return sb.toString();
    }

    /**
     * 
     * @Description: 获取 Disconf FILE ZOO Path
     * 
     * @return
     * @return String
     * @author liaoqiqi
     * @date 2013-6-17
     */
    public static String getFileZooPath(String baseUrl) {

        return baseUrl + Constants.SEP_STRING + Constants.STORE_FILE_URL_KEY;
    }

    /**
     * 
     * @param path1
     * @param path2
     * @return
     */
    public static String joinPath(String path1, String path2) {
        return path1 + Constants.SEP_STRING + path2;
    }

    /**
     * 
     * @Description: 获取 Disconf ITEM ZOO Path
     * 
     * @return
     * @return String
     * @author liaoqiqi
     * @date 2013-6-17
     */
    public static String getItemZooPath(String baseUrl) {

        return baseUrl + Constants.SEP_STRING + Constants.STORE_ITEM_URL_KEY;
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
