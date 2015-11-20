package com.baidu.disconf.client.test.utils;

import org.junit.Test;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import junit.framework.TestCase;

/**
 * Created by knightliao on 15/11/13.
 */
public class TestAntPathMatcher extends TestCase {

    @Test
    public void testUrlMatch() {

        PathMatcher matcher = new AntPathMatcher();

        //  完全路径url方式路径匹配
        //  String requestPath="http://localhost:8080/pub/login.jsp";//请求路径

        //  String patternPath="**/login.jsp";//路径匹配模式

        //  不完整路径uri方式路径匹配
        //  String requestPath="/app/pub/login.do";//请求路径
        //  String patternPath="/**/login.do";//路径匹配模式
        //  模糊路径方式匹配
        //  String requestPath="/app/pub/login.do";//请求路径
        //  String patternPath="/**/*.do";//路径匹配模式
        //  包含模糊单字符路径匹配

        String requestPath = "/app/pub/login.do";//请求路径
        String patternPath = "/**/lo?in.do";//路径匹配模式

        boolean result = matcher.match(patternPath, requestPath);
        assertTrue(result);
    }
}
