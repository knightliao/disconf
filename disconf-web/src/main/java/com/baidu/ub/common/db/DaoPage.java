package com.baidu.ub.common.db;

import java.util.ArrayList;
import java.util.List;

import com.baidu.unbiz.common.genericdao.operator.Order;

/**
 * 专门 用于DAO的Page 外部模块不能使用
 *
 * @author liaoqiqi
 */
public class DaoPage {

    private int pageNo = 1;
    private int pageSize = 99999999;
    private List<Order> orderList = new ArrayList<Order>();

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

}