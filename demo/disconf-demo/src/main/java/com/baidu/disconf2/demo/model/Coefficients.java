package com.baidu.disconf2.demo.model;

import com.baidu.disconf2.client.common.annotations.DisconfFile;
import com.baidu.disconf2.client.common.annotations.DisconfFileItem;

/**
 * 金融系数文件
 * 
 **/
@DisconfFile(filename = Coefficients.filename)
public class Coefficients {

    public static final String filename = "coefficients.properties";

    /**
     * 百度百发的系数
     */
    private double baiFaCoe;

    /**
     * 余额宝系数
     */
    private double yuErBaoCoe;

    @DisconfFileItem
    public double getBaiFaCoe() {
        return baiFaCoe;
    }

    public void setBaiFaCoe(double baiFaCoe) {
        this.baiFaCoe = baiFaCoe;
    }

    @DisconfFileItem
    public double getYuErBaoCoe() {
        return yuErBaoCoe;
    }

    public void setYuErBaoCoe(double yuErBaoCoe) {
        this.yuErBaoCoe = yuErBaoCoe;
    }
}
