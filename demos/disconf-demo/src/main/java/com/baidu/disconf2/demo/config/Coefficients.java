package com.baidu.disconf2.demo.config;

import com.baidu.disconf2.client.common.annotations.DisconfFile;
import com.baidu.disconf2.client.common.annotations.DisconfFileItem;
import com.baidu.disconf2.client.common.annotations.DisconfItem;

/**
 * 金融系数文件
 * 
 **/
@DisconfFile(filename = Coefficients.filename)
public class Coefficients {

    public static final String filename = "coefficients.properties";
    public static final String key = "discountRate";

    private Double discount = 1.0d;

    private double baiFaCoe;

    private double yuErBaoCoe;

    /**
     * 阿里余额宝的系数, 分布式文件配置
     * 
     * @return
     */
    @DisconfFileItem(name = "coe.baiFaCoe")
    public double getBaiFaCoe() {
        return baiFaCoe;
    }

    public void setBaiFaCoe(double baiFaCoe) {
        this.baiFaCoe = baiFaCoe;
    }

    /**
     * 百度百发的系数, 分布式文件配置
     * 
     * @return
     */
    @DisconfFileItem(name = "coe.yuErBaoCoe")
    public double getYuErBaoCoe() {
        return yuErBaoCoe;
    }

    public void setYuErBaoCoe(double yuErBaoCoe) {
        this.yuErBaoCoe = yuErBaoCoe;
    }

    /**
     * 折扣率，分布式配置
     * 
     * @return
     */
    @DisconfItem(key = key)
    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }
}
