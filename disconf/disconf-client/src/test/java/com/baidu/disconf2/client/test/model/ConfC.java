package com.baidu.disconf2.client.test.model;


/**
 * 1. 分布式配置文件，fileName 是配置文件名<br/>
 * 2. 使用静态变量<br/>
 * 3. 使用 自动导入
 * 
 **/
public class ConfC {

    public static final String filename = "confC.properties";

    private static int varC = 15;

    public static int getVarC() {
        return varC;
    }

    public static void setVarC(int varC) {
        ConfC.varC = varC;
    }

}
