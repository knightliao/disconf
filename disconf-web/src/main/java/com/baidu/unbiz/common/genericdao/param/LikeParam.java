package com.baidu.unbiz.common.genericdao.param;

/**
 * 封装like参数
 *
 * @author Darwin(Tianxin)
 */
public class LikeParam {
    String word;

    public LikeParam(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }
}