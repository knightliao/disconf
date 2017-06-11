package com.baidu.disconf.client.test.model;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;

/**
 * 静态配置文件
 *
 * @author liaoqiqi
 * @version 2014-8-14
 */
@DisconfFile(filename = "staticConf.properties")
public class StaticConf {

    private static int staticvar = 40;

    private static double staticvar2 = 50;

    // 用于测试没有setter方法的静态配置文件
    private static int staticvar3 = 60;

    @DisconfFileItem(name = "staticvar", associateField = "staticvar")
    public static int getStaticVar() {
        return staticvar;
    }

    public static void setStaticVar(int staticVar) {
        StaticConf.staticvar = staticVar;
    }

    public static int getStaticvar() {
        return staticvar;
    }

    public static void setStaticvar(int staticvar) {
        StaticConf.staticvar = staticvar;
    }

    @DisconfFileItem(name = "staticvar2", associateField = "staticvar2")
    public static double getStaticvar2() {
        return staticvar2;
    }

    public static void setStaticvar2(double staticvar2) {
        StaticConf.staticvar2 = staticvar2;
    }

    @DisconfFileItem(name = "staticvar3", associateField = "staticvar3")
    public static int getStaticvar3() {
        return staticvar3;
    }
}
