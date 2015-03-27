package com.baidu.dsp.common.form;

import java.io.Serializable;

import javax.validation.Valid;

import com.baidu.dsp.common.constant.FrontEndInterfaceConstant;
import com.baidu.dsp.common.constraint.PageOrderConstraint;
import com.baidu.dsp.common.constraint.validation.PageOrderValidator;
import com.baidu.ub.common.commons.ThreadContext;

/**
 * 所有的List请求的基类
 *
 * @author liaoqiqi
 * @version 2013-12-4
 */
public abstract class RequestListBase implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 7999737352570996142L;

    @Valid
    private Page page = new Page();

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public int getPageNo() {
        return page.pageNo;
    }

    public int getPageSize() {
        return page.pageSize;
    }

    public String getOrderBy() {
        return page.orderBy;
    }

    public String getOrder() {
        return page.order;
    }

    /**
     * Page对象,通过ThreadContext存储FE传进来的参数数据
     *
     * @author liaoqiqi
     * @version 2013-12-4
     */
    public class Page implements Serializable {

        /**
         *
         */
        private static final long serialVersionUID = -5120153163445183415L;

        public static final String ORDER_TOKEN = ",";

        private int pageNo = 0;
        private int pageSize = 99999999;
        private String orderBy = null;

        @PageOrderConstraint
        private String order = PageOrderValidator.DESC;

        public boolean isAsc() {
            return order.equals(PageOrderValidator.ASC);
        }

        public int getPageNo() {
            return pageNo;
        }

        public void setPageNo(int pageNo) {
            this.pageNo = pageNo;
            ThreadContext.putContext(FrontEndInterfaceConstant.PAGE_NO, pageNo);
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
            ThreadContext.putContext(FrontEndInterfaceConstant.PAGE_SIZE, pageSize);
        }

        public String getOrderBy() {
            return orderBy;
        }

        public void setOrderBy(String orderBy) {
            this.orderBy = orderBy;
            ThreadContext.putContext(FrontEndInterfaceConstant.PAGE_ORDER_BY, orderBy);
        }

        public String getOrder() {
            return order;
        }

        public void setOrder(String order) {
            this.order = order;
            ThreadContext.putContext(FrontEndInterfaceConstant.PAGE_ORDER, order);
        }

    }
}
