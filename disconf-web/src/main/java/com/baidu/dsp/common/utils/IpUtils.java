package com.baidu.dsp.common.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author liaoqiqi
 * @version 2014-4-23
 */
public class IpUtils {

    public static String getIp(HttpServletRequest request) {
        if (null == request) {
            return null;
        }
        String ip = request.getHeader("x-forwarded-for");
        if (!validateIp(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
            if (!validateIp(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
                if (!validateIp(ip)) {
                    ip = request.getRemoteAddr();
                }
            }
        }
        return ip;
    }

    private static boolean validateIp(String ip) {
        return (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) ? false : true;
    }

}
