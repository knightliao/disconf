package com.baidu.disconf.web.service.roleres.bo;

import com.baidu.dsp.common.dao.Columns;
import com.baidu.dsp.common.dao.DB;
import com.baidu.unbiz.common.genericdao.annotation.Column;
import com.baidu.unbiz.common.genericdao.annotation.Table;
import com.github.knightliao.apollo.db.bo.BaseObject;

/**
 *
 */
@Table(db = DB.DB_NAME, keyColumn = "role_res_id", name = "role_resource")
public class RoleResource extends BaseObject<Integer> {

    private static final long serialVersionUID = 1L;

    @Column(value = Columns.ROLE_ID)
    private Integer roleId;

    @Column(value = "url_pattern")
    private String urlPattern;

    @Column(value = "url_description")
    private String urlDescription;

    @Column(value = "method_mask")
    private String methodMask;

    @Column(value = Columns.UPDATE_TIME)
    private String updateTime;

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getUrlPattern() {
        return urlPattern;
    }

    public void setUrlPattern(String urlPattern) {
        this.urlPattern = urlPattern;
    }

    public String getUrlDescription() {
        return urlDescription;
    }

    public void setUrlDescription(String urlDescription) {
        this.urlDescription = urlDescription;
    }

    public String getMethodMask() {
        return methodMask;
    }

    public void setMethodMask(String methodMask) {
        this.methodMask = methodMask;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "RoleResource [roleId=" + roleId + ", urlPattern=" + urlPattern + ", urlDescription=" + urlDescription +
                   ", methodMask=" + methodMask + ", updateTime=" + updateTime + "]";
    }

}
