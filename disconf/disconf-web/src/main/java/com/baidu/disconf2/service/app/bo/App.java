package com.baidu.disconf2.service.app.bo;

import com.baidu.dsp.common.dao.Columns;
import com.baidu.dsp.common.dao.DB;
import com.baidu.ub.common.generic.annotation.Column;
import com.baidu.ub.common.generic.annotation.Table;
import com.baidu.ub.common.generic.bo.BaseObject;

/**
 * 
 * @author liaoqiqi
 * @version 2014-6-16
 */
@Table(db = DB.DB_NAME, name = "app", keyColumn = Columns.APP_ID)
public class App extends BaseObject<Long> {

    /**
     * 
     */
    private static final long serialVersionUID = -2217832889126331664L;

    /**
     * 
     */
    @Column(value = Columns.NAME)
    private String name;

    /**
     * 
     */
    @Column(value = Columns.DESC)
    private String desc;

    /**
     * 创建时间
     */
    @Column(value = Columns.CREATE_TIME)
    private String createTime;

    /**
     * 更新时间
     */
    @Column(value = Columns.UPDATE_TIME)
    private String updateTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "App [name=" + name + ", desc=" + desc + ", createTime="
                + createTime + ", updateTime=" + updateTime + "]";
    }

}
