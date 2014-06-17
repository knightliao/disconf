package com.baidu.ub.common.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author hanxu03
 * 
 *         2013-5-7
 */
public class CookieUtils {

    public static String getCookieValue(HttpServletRequest request,
            String cookieName) {
        if (cookieName == null || request == null) {
            return null;
        }
        Cookie[] cks = request.getCookies();
        if (cks == null) {
            return null;
        }
        for (Cookie cookie : cks) {
            if (cookieName.equals(cookie.getName()))
                return cookie.getValue();
        }
        return null;
    }

    public static Cookie getCookie(HttpServletRequest request, String cookieName) {
        if (cookieName == null || request == null) {
            return null;
        }
        Cookie[] cks = request.getCookies();
        if (cks == null) {
            return null;
        }
        for (Cookie cookie : cks) {
            if (cookieName.equals(cookie.getName())) {
                return cookie;
            }
        }
        return null;
    }
}
