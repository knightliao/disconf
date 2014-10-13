package com.baidu.disconf.demo.dubbo.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.baidu.disconf.demo.dubbo.service.DubboService;

public class DubboServiceImpl implements DubboService {

    public String printWord(String word) {

        String outWord = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss ]").format(new Date()) + word;
        System.out.println("Dubbo Server: " + outWord);
        return outWord;
    }

}