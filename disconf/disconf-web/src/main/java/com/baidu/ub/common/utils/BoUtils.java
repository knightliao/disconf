package com.baidu.ub.common.utils;

import java.util.ArrayList;
import java.util.List;

import com.baidu.ub.common.generic.bo.BaseObject;

public class BoUtils {

    public static List<Long> getIdLongList(
            List<? extends BaseObject<Long>> objectList) {
        List<Long> idList = new ArrayList<Long>();

        for (BaseObject<Long> object : objectList) {
            idList.add(object.getId());
        }

        return idList;
    }

    public static List<Integer> getIdIntegerList(
            List<? extends BaseObject<Integer>> objectList) {
        List<Integer> idList = new ArrayList<Integer>();

        for (BaseObject<Integer> object : objectList) {
            idList.add(object.getId());
        }

        return idList;
    }

}
