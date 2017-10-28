package com.baidu.disconf.web.service.config.bo;

import com.baidu.dsp.common.dao.Columns;
import com.baidu.dsp.common.dao.DB;
import com.baidu.unbiz.common.genericdao.annotation.Column;
import com.baidu.unbiz.common.genericdao.annotation.Table;
import com.github.knightliao.apollo.db.bo.BaseObject;

import lombok.Data;

@Data
@Table(db = DB.DB_NAME, name = "config_history", keyColumn = "id")
public class ConfigHistory extends BaseObject<Long> {

    /**
     *
     */
    @Column(value = Columns.CONFIG_ID)
    private Long configId;

    /**
     *
     */
    @Column(value = "old_value")
    private String oldValue;

    /**
     *
     */
    @Column(value = "new_value")
    private String newValue;

    /**
     * 创建时间
     */
    @Column(value = Columns.CREATE_TIME)
    private String createTime;

    /**
     *
     */
    @Column(value = Columns.UPDATE_BY)
    private Long updateBy;

	public Long getConfigId() {
		return configId;
	}

	public void setConfigId(Long configId) {
		this.configId = configId;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}
    
    

}
