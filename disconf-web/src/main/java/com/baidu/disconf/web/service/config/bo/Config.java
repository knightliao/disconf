package com.baidu.disconf.web.service.config.bo;

import com.baidu.dsp.common.dao.Columns;
import com.baidu.dsp.common.dao.DB;
import com.baidu.unbiz.common.genericdao.annotation.Column;
import com.baidu.unbiz.common.genericdao.annotation.Table;
import com.github.knightliao.apollo.db.bo.BaseObject;

/**
 * 
 * @author liaoqiqi
 * @version 2014-6-16
 */
@Table(db = DB.DB_NAME, name = "config", keyColumn = Columns.CONFIG_ID)
public class Config extends BaseObject<Long> {

    /**
     * 
     */
    private static final long serialVersionUID = -2217832889126331664L;

    /**
     * 
     */
    @Column(value = Columns.TYPE)
    private Integer type;

    /**
     * 
     */
    @Column(value = Columns.NAME)
    private String name;

    /**
     * 
     */
    @Column(value = Columns.VALUE)
    private String value;

    /**
     * 
     */
    @Column(value = Columns.APP_ID)
    private Long appId;

    /**
     * 
     */
    @Column(value = Columns.VERSION)
    private String version;

    /**
     * 
     */
    @Column(value = Columns.ENV_ID)
    private Long envId;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Long getEnvId() {
        return envId;
    }

    public void setEnvId(Long envId) {
        this.envId = envId;
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
        return "App [type=" + type + ", name=" + name + ", value=" + value + ", appId=" + appId + ", version="
                + version + ", envId=" + envId + ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
    }
}
