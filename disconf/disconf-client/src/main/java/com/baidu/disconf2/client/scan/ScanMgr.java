package com.baidu.disconf2.client.scan;

import com.baidu.disconf2.client.scan.inner.ScanModel;
import com.baidu.disconf2.client.scan.inner.ScanPack;

/**
 * 扫描模块
 * 
 * @author liaoqiqi
 * @version 2014-6-6
 */
public class ScanMgr {

    /**
     * 
     * 扫描包
     * 
     * @throws Exception
     */
    public static void scan(String packageName) throws Exception {

        // 获取扫描对象
        ScanModel scanModel = ScanPack.scan(packageName);

    }

    /**
     * 
     * @param args
     */
    public static void main(String[] args) {

        try {

            ScanMgr.scan("");

        } catch (Exception e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
