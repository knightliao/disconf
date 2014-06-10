package com.baidu.ub.common.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * 
 * @author liaoqiqi
 * @version 2014-1-15
 */
public class RandomUtil {

    /**
     * 获取一堆数据
     * 
     * @param limit
     * @return
     */
    public static List<Integer> randomSerial(int limit) {
        List<Integer> list = new ArrayList<Integer>(limit);

        for (int ix = 0; ix < limit; ++ix) {
            list.add(ix);
        }

        Collections.shuffle(list, new Random());
        return list;
    }

    public static void main(String[] args) {
        System.out.println((int) 1 / 20 + "," + random(0, 0) + ","
                + random(3, 2) + "," + random(-2, 4));
        List<Integer> a = RandomUtil.randomSerial(10);
        for (int i : a) {
            System.out.println(i);
        }
        // for(int i = 0 ;i < 500 ; i++){
        // System.out.println(random(0, 20));
        // }
    }

    /**
     * [min,max]
     * 
     * @param min
     * @param max
     * @return
     */
    public static int random(int min, int max) {
        if (min < 0 || max < 0) {
            throw new RuntimeException(
                    "illegal argment, min and max must great then zero.");
        }
        if (min > max) {
            int t = max;
            max = min;
            min = t;
        } else if (min == max) {
            return min;
        }

        Random random = new Random();
        return random.nextInt(max) % (max - min + 1) + min;

    }

}
