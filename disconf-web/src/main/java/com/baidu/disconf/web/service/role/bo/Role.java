package com.baidu.disconf.web.service.role.bo;

import com.baidu.dsp.common.dao.Columns;
import com.baidu.dsp.common.dao.DB;
import com.baidu.unbiz.common.genericdao.annotation.Column;
import com.baidu.unbiz.common.genericdao.annotation.Table;
import com.github.knightliao.apollo.db.bo.BaseObject;

/**
 * @author weiwei
 * @date 2013-12-24 下午2:43:37
 */
@Table(db = DB.DB_NAME, keyColumn = Columns.ROLE_ID, name = "role")
public class Role extends BaseObject<Integer> {

    private static final long serialVersionUID = 1L;

    @Column(value = Columns.RoleColumns.ROLE_NAME)
    private String roleName;

    @Column(value = Columns.CREATE_TIME)
    private String createTime;

    @Column(value = Columns.CREATE_BY)
    private String createBy;

    @Column(value = Columns.UPDATE_TIME)
    private String updateTime;

    @Column(value = Columns.UPDATE_BY)
    private String updateBy;

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Override
    public String toString() {
        return "Role [roleName=" + roleName + ", createTime=" + createTime + ", createBy=" + createBy +
                   ", updateTime=" + updateTime + ", updateBy=" + updateBy + "]";
    }

}
