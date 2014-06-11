package com.baidu.disconf2.client.test.model;

import com.baidu.disconf2.client.common.annotations.DisconfFile;
import com.baidu.disconf2.client.common.annotations.DisconfFileItem;

/**
 * 1. 分布式配置文件，fileName 是配置文件名<br/>
 * 2. 非static变量示例 <br/>
 * 3. 单例使用方式<br/>
 * 4. 使用自动注入方式
 * 
 **/
@DisconfFile(filename = ConfB.filename)
public class ConfB {

    public static final String filename = "confB.properties";
    public static final String keyA = "keyA";

    private ConfB() {

    }

    /**
     * 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例 没有绑定关系，而且只有被调用到时才会装载，从而实现了延迟加载。
     */
    private static class SingletonHolder {
        /**
         * 静态初始化器，由JVM来保证线程安全
         */
        private static ConfB instance = new ConfB();
    }

    public static ConfB getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 配置文件中的某Item
     */
    @DisconfFileItem
    private int varB = 5;

    public int getVarB() {
        return varB;
    }

    public void setVarB(int varB) {
        this.varB = varB;
    }

}
