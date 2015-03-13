package com.baidu.ub.common.db;

import java.util.ArrayList;
import java.util.List;

/**
 * 页面返回结果
 *
 * @param <T>
 *
 * @author liaoqiqi
 * @version 2014-2-20
 */
public class DaoPageResult<T> {

    private List<T> result = new ArrayList<T>();

    private int totalCount = -1;

    // 统计结果
    private T footResult;

    @Override
    public String toString() {
        return "DaoPageResult [result=" + result + ", totalCount=" + totalCount + ", footResult=" + footResult + "]";
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public T getFootResult() {
        return footResult;
    }

    public void setFootResult(T footResult) {
        this.footResult = footResult;
    }

}
