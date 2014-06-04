package com.baidu.disconf2.demo.model;

import com.baidu.disconf2.common.annotations.DisconfFile;
import com.baidu.disconf2.common.annotations.DisconfFileItem;

/**
 * 1. 分布式配置文件，fileName 是配置文件名<br/>
 * 2. static变量的示例
 * 
 * 
 **/
@DisconfFile(filename = ConfB.filename)
public class ConfB {

    public static final String filename = "confB.properties";
    public static final String keyA = "keyA";

    /**
     * 配置文件中的某Item
     */
    @DisconfFileItem(key = "varA", defaultValue = "5")
    private static int varA;

    public static int getVarA() {
        return varA;
    }

    public static void setVarA(int varA) {
        ConfB.varA = varA;
    }

}
